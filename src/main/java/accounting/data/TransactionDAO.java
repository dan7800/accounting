package accounting.data;

import accounting.models.*;
import accounting.models.Transaction;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.List;

@RegisterMapperFactory(ResultSetMapperFactory.class)
public abstract class TransactionDAO {

    @SqlUpdate("INSERT INTO transactions (timestamp, description) VALUES (NOW(), :description)")
    public abstract long insertTransaction(@Bind("description") String description);

    @SqlQuery("SELECT * FROM transactions WHERE id = :id")
    public abstract Transaction selectTransaction(@Bind("id") long id);

    @SqlUpdate("INSERT INTO entries (transactionId, toAccountId, fromAccountId, amount) VALUES (:transactionId, :toAccountId, :fromAccountId, :amount)")
    public abstract long insertEntry(@Bind("transactionId") long transactionId, @Bind("toAccountId") long toAccountId, @Bind("fromAccountId") long fromAccountId, @Bind("amount") double amount);

    @SqlQuery("SELECT * FROM entries WHERE id = :id")
    public abstract Entry selectEntry(@Bind("id") long id);

    @SqlQuery("SELECT * FROM entries WHERE transactionId = :transactionId")
    public abstract List<Entry> selectEntriesByTransactionId(@Bind("transactionId") long transactionId);

    @org.skife.jdbi.v2.sqlobject.Transaction
    public Transaction get(long id) {
        List<Entry> entries = selectEntriesByTransactionId(id);
        Transaction transaction = selectTransaction(id);
        transaction = new Transaction(id, transaction.getTimestamp(), transaction.getDescription(), entries);

        return transaction;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeSale(SaleRequest saleRequest) {

        return 0;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long payEmployee(PayrollRequest payrollRequest) {

        return 0;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long purchaseInventory(InventoryRequest inventoryRequest) {

        return 0;
    }

    @org.skife.jdbi.v2.sqlobject.Transaction
    public long makeRefund(RefundRequest refundRequest) {

        return 0;
    }
}
