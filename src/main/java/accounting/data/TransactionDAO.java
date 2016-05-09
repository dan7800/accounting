package accounting.data;

import accounting.models.*;
import accounting.models.Transaction;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterMapperFactory(RosettaMapperFactory.class)
public abstract class TransactionDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO transactions (timestamp, description) VALUES (NOW(), :description)")
    protected abstract long insertTransaction(@Bind("description") String description);

    @SqlQuery("SELECT * FROM transactions WHERE id = :id")
    protected abstract Transaction selectTransaction(@Bind("id") long id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO entries (transactionId, toAccountId, fromAccountId, amount) VALUES (:transactionId, :toAccountId.state, :fromAccountId.state, :amount)")
    protected abstract long insertEntry(@Bind("transactionId") long transactionId, @BindBean("toAccountId") Account toAccountId, @BindBean("fromAccountId") Account fromAccountId, @Bind("amount") double amount);

    @SqlQuery("SELECT * FROM entries WHERE id = :id")
    protected abstract Entry selectEntry(@Bind("id") long id);

    @SqlQuery("SELECT * FROM entries WHERE transactionId = :transactionId")
    protected abstract List<Entry> selectEntriesByTransactionId(@Bind("transactionId") long transactionId);

    @SqlUpdate("UPDATE accounts SET balance = balance + :balance WHERE id = :account.state")
    public abstract void updateAccount(@BindBean("account") Account account, @Bind("balance") double balance);

    @SqlQuery("SELECT balance FROM accounts WHERE id = :account.state")
    public abstract double getAccountBalance(@BindBean("account") Account account);

    @SqlUpdate("DELETE from transactions")
    public abstract void clearTransactions();

    @SqlUpdate("DELETE from entries")
    public abstract void clearEntries();

    @SqlUpdate("UPDATE accounts SET balance = 0")
    public abstract void clearAccounts();

    @org.skife.jdbi.v2.sqlobject.Transaction
    public Transaction get(long id) {
        List<Entry> entries = selectEntriesByTransactionId(id);
        Transaction transaction = selectTransaction(id);
        transaction = new Transaction(id, transaction.getTimestamp(), transaction.getDescription(), entries);

        return transaction;
    }

    private long insertEntryAndUpdateAccounts(long id, Account toAccount, Account fromAccount, double amount) {
        updateAccount(toAccount, amount);
        updateAccount(fromAccount, -1*amount);
        return insertEntry(id, toAccount, fromAccount, amount);
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeSale(SaleRequest saleRequest) {

        if(getAccountBalance(Account.INVENTORY) < saleRequest.getCostOfGoodsSold()) {
            throw new WebApplicationException("Not enough inventory to complete the sale", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(saleRequest.getDescription());

        insertEntryAndUpdateAccounts(id, Account.CASH, Account.REVENUES, saleRequest.getSalePrice());
        insertEntryAndUpdateAccounts(id, Account.COGS, Account.INVENTORY, saleRequest.getCostOfGoodsSold());
        insertEntryAndUpdateAccounts(id, Account.CASH, Account.SALES_TAX_PAYABLE, saleRequest.getSalePrice() * 0.08); // TODO make this a config thing

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long payEmployee(PayrollRequest payrollRequest) {

        if(getAccountBalance(Account.CASH) < payrollRequest.getPay()) {
            throw new WebApplicationException("Not enough cash to pay employee(s)", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(payrollRequest.getDescription());

        insertEntryAndUpdateAccounts(id, Account.EMPLOYEES, Account.CASH, payrollRequest.getPay());

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long purchaseInventory(InventoryRequest inventoryRequest) {

        if(getAccountBalance(Account.CASH) < inventoryRequest.getCostOfGoods()) {
            throw new WebApplicationException("Not enough cash to buy inventory", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(inventoryRequest.getDescription());

        insertEntryAndUpdateAccounts(id, Account.INVENTORY, Account.CASH, inventoryRequest.getCostOfGoods());

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeRefund(RefundRequest refundRequest) {

        if(getAccountBalance(Account.CASH) < refundRequest.getRefundAmount() + refundRequest.getRefundAmount() * 0.08) {
            throw new WebApplicationException("Not enough cash to complete refund", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(refundRequest.getDescription());

        insertEntryAndUpdateAccounts(id, Account.REFUNDS_PAID, Account.CASH, refundRequest.getRefundAmount());
        insertEntryAndUpdateAccounts(id, Account.INVENTORY, Account.COGS, refundRequest.getValueOfReturns());
        insertEntryAndUpdateAccounts(id, Account.SALES_TAX_PAYABLE, Account.CASH, refundRequest.getRefundAmount() * 0.08);

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long invest(InvestmentRequest investmentRequest) {
        long id = insertTransaction(investmentRequest.getDescription());

        insertEntryAndUpdateAccounts(id, Account.CASH, Account.INVESTMENT, investmentRequest.getAmount());

        return id;
    }
}
