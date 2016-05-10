package accounting.data;

import accounting.models.*;
import accounting.models.Transaction;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RegisterMapperFactory(RosettaMapperFactory.class)
public abstract class TransactionDAO {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO transactions (timestamp, description) VALUES (NOW(), :description)")
    protected abstract long insertTransaction(@Bind("description") String description);

    @SqlQuery("SELECT * FROM transactions WHERE id = :id")
    protected abstract Transaction selectTransaction(@Bind("id") long id);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO entries (transactionId, toAccountId, fromAccountId, amount) VALUES (:transactionId, :toAccountId.state, :fromAccountId.state, :amount)")
    protected abstract long insertEntry(@Bind("transactionId") long transactionId, @BindBean("toAccountId") AccountEnum toAccountId, @BindBean("fromAccountId") AccountEnum fromAccountId, @Bind("amount") double amount);

    @SqlQuery("SELECT * FROM entries WHERE id = :id")
    protected abstract Entry selectEntry(@Bind("id") long id);

    @SqlQuery("SELECT * FROM entries WHERE transactionId = :transactionId")
    protected abstract List<Entry> selectEntriesByTransactionId(@Bind("transactionId") long transactionId);

    @SqlUpdate("UPDATE accounts SET balance = balance + :balance WHERE id = :account.state")
    public abstract void updateAccount(@BindBean("account") AccountEnum account, @Bind("balance") double balance);

    @SqlQuery("SELECT balance FROM accounts WHERE id = :account.state")
    public abstract double getAccountBalance(@BindBean("account") AccountEnum account);

    @SqlUpdate("DELETE from transactions")
    public abstract void clearTransactions();

    @SqlUpdate("DELETE from entries")
    public abstract void clearEntries();

    @SqlUpdate("UPDATE accounts SET balance = 0")
    public abstract void clearAccounts();

    @SqlQuery("SELECT * FROM accounts")
    public abstract List<Account> selectAllAccounts();

    @org.skife.jdbi.v2.sqlobject.Transaction
    public Transaction get(long id) {
        return getHelper(id);
    }

    private Transaction getHelper(long id) {
        List<Entry> entries = selectEntriesByTransactionId(id);
        Transaction transaction = selectTransaction(id);
        if (Objects.isNull(transaction)) {
            throw new WebApplicationException("No transaction exists for this id.", Response.Status.NO_CONTENT);
        }
        transaction = new Transaction(id, transaction.getTimestamp(), transaction.getDescription(), entries);

        return transaction;
    }

    private long insertEntryAndUpdateAccounts(long id, AccountEnum toAccount, AccountEnum fromAccount, double amount) {
        updateAccount(toAccount, amount);
        updateAccount(fromAccount, -1*amount);
        return insertEntry(id, toAccount, fromAccount, amount);
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeSale(SaleRequest saleRequest) {

        if(getAccountBalance(AccountEnum.INVENTORY) < saleRequest.getCostOfGoodsSold()) {
            throw new WebApplicationException("Not enough inventory to complete the sale", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(saleRequest.getDescription());

        insertEntryAndUpdateAccounts(id, AccountEnum.CASH, AccountEnum.REVENUES, saleRequest.getSalePrice());
        insertEntryAndUpdateAccounts(id, AccountEnum.COGS, AccountEnum.INVENTORY, saleRequest.getCostOfGoodsSold());
        insertEntryAndUpdateAccounts(id, AccountEnum.CASH, AccountEnum.SALES_TAX_PAYABLE, saleRequest.getSalePrice() * 0.08); // TODO make this a config thing

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long payEmployee(PayrollRequest payrollRequest) {

        if(getAccountBalance(AccountEnum.CASH) < payrollRequest.getPay()) {
            throw new WebApplicationException("Not enough cash to pay employee(s)", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(payrollRequest.getDescription());

        insertEntryAndUpdateAccounts(id, AccountEnum.EMPLOYEES, AccountEnum.CASH, payrollRequest.getPay());

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long purchaseInventory(InventoryRequest inventoryRequest) {

        if(getAccountBalance(AccountEnum.CASH) < inventoryRequest.getCostOfGoods()) {
            throw new WebApplicationException("Not enough cash to buy inventory", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(inventoryRequest.getDescription());

        insertEntryAndUpdateAccounts(id, AccountEnum.INVENTORY, AccountEnum.CASH, inventoryRequest.getCostOfGoods());

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeRefund(RefundRequest refundRequest) {

        if(getAccountBalance(AccountEnum.CASH) < refundRequest.getRefundAmount() + refundRequest.getRefundAmount() * 0.08) {
            throw new WebApplicationException("Not enough cash to complete refund", Response.Status.FORBIDDEN);
        }

        long id = insertTransaction(refundRequest.getDescription());

        insertEntryAndUpdateAccounts(id, AccountEnum.REFUNDS_PAID, AccountEnum.CASH, refundRequest.getRefundAmount());
        insertEntryAndUpdateAccounts(id, AccountEnum.INVENTORY, AccountEnum.COGS, refundRequest.getValueOfReturns());
        insertEntryAndUpdateAccounts(id, AccountEnum.SALES_TAX_PAYABLE, AccountEnum.CASH, refundRequest.getRefundAmount() * 0.08);

        return id;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long invest(InvestmentRequest investmentRequest) {
        long id = insertTransaction(investmentRequest.getDescription());

        insertEntryAndUpdateAccounts(id, AccountEnum.CASH, AccountEnum.INVESTMENT, investmentRequest.getAmount());

        return id;
    }

    @SqlQuery("SELECT id FROM transactions")
    protected abstract List<Long> selectAllTransactionIds();

    @org.skife.jdbi.v2.sqlobject.Transaction
    public List<Transaction> getAllTransactions() {
        // list of transactions to return
        List<Transaction> transactions = new ArrayList<>();
        // get all transaction Ids
        List<Long> transactionIds = selectAllTransactionIds();
        // get each transaction individually so that entries are loaded into the transaction
        for (long id : transactionIds ) {
            transactions.add(getHelper(id));
        }
        // return the list of transactions
        return transactions;
    }
}
