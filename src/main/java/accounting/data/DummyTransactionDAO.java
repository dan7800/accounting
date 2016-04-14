package accounting.data;

import accounting.models.*;

// This is a dummy class.  The actual implementation will be written in sql in the interface.
public class DummyTransactionDAO implements TransactionDAO {

    public long makeSale(SaleRequest saleRequest) {
        return 0;
    }

    public Transaction get(long id) {
        return new Transaction(id);
    }

    public long payEmployee(PayrollRequest payrollRequest) {
        return 1;
    }

    public long purchaseInventory(InventoryRequest inventoryRequest) {
        return 2;
    }

    @Override
    public long makeRefund(RefundRequest refundRequest) {
        return 3;
    }
}
