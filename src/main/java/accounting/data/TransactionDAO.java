package accounting.data;

import accounting.models.InventoryRequest;
import accounting.models.PayrollRequest;
import accounting.models.SaleRequest;
import accounting.models.Transaction;

public interface TransactionDAO {

    // TODO sql should go in here in the form of an @SqlUpdate("INSERT...") annotation
    public long makeSale(SaleRequest saleRequest);

    public Transaction get(long id);

    public long payEmployee(PayrollRequest payrollRequest);

    public long purchaseInventory(InventoryRequest inventoryRequest);
}
