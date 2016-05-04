package accounting.data;

import accounting.models.*;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.List;

@RegisterMapperFactory(ResultSetMapperFactory.class)
public interface TransactionDAO {

    @SqlUpdate("INSERT INTO transactions (timestamp, description) VALUES (NOW(), :description)")
    public long insertTransaction(@Bind("description") String description);

    @SqlQuery("SELECT * FROM transactions WHERE id = :id")
    public Transaction selectTransaction(@Bind("id") long id);

    @SqlUpdate("INSERT INTO entries (transactionId, toAccountId, fromAccountId, amount) VALUES (:transactionId, :toAccountId, :fromAccountId, :amount)")
    public long insertEntry(@Bind("transactionId") long transactionId, @Bind("toAccountId") long toAccountId, @Bind("fromAccountId") long fromAccountId, @Bind("amount") double amount);

    @SqlQuery("SELECT * FROM entries WHERE id = :id")
    public Entry selectEntry(@Bind("id") long id);

    @SqlQuery("SELECT * FROM entries WHERE transactionId = :transactionId")
    public List<Entry> selectEntriesByTransactionId(@Bind("transactionId") long transactionId);


    // TODO take these out and implement creating transactions and entries within the resource classes where these are called
    public long makeSale(SaleRequest saleRequest);

    public Transaction get(long id);

    public long payEmployee(PayrollRequest payrollRequest);

    public long purchaseInventory(InventoryRequest inventoryRequest);

    public long makeRefund(RefundRequest refundRequest);
}
