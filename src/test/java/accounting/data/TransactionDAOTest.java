package accounting.data;

import accounting.models.*;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDAOTest {
    private TransactionDAO dao;

    @Before
    public void setup() {
        DBI dbi = new DBI("jdbc:mysql://localhost:3306/accounting_test", "accounting_test", "accounting_test");
        this.dao = dbi.onDemand(TransactionDAO.class);
    }

    public void clearAll() {
        dao.clearTransactions();
        dao.clearEntries();
        dao.clearAccounts();
    }

    @Test
    public void testInsertTransaction() {
        clearAll();
        long id = dao.insertTransaction("hello");
        assertThat(dao.get(id)).isNotNull();
    }

    @Test
    public void testMakeSale() {
        clearAll();
        dao.updateAccount(AccountEnum.INVENTORY, 10);

        SaleRequest request = new SaleRequest(10, 100, "some description");
        long id = dao.makeSale(request);

        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("some description");
        assertThat(transaction.getEntries().size()).isEqualTo(3);

        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getSalePrice());
        assertThat(first.getFromAccountId()).isEqualTo(AccountEnum.REVENUES.getState());
        assertThat(first.getToAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        Entry second = transaction.getEntries().get(1);
        assertThat(second.getAmount()).isEqualTo(request.getCostOfGoodsSold());
        assertThat(second.getFromAccountId()).isEqualTo(AccountEnum.INVENTORY.getState());
        assertThat(second.getToAccountId()).isEqualTo(AccountEnum.COGS.getState());
        assertThat(second.getTransactionId()).isEqualTo(id);

        Entry third = transaction.getEntries().get(2);
        assertThat(third.getAmount()).isEqualTo(request.getSalePrice() * 0.08);
        assertThat(third.getFromAccountId()).isEqualTo(AccountEnum.SALES_TAX_PAYABLE.getState());
        assertThat(third.getToAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(third.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(AccountEnum.CASH)).isEqualTo(108);
        assertThat(dao.getAccountBalance(AccountEnum.REVENUES)).isEqualTo(-100);
        assertThat(dao.getAccountBalance(AccountEnum.COGS)).isEqualTo(10);
        assertThat(dao.getAccountBalance(AccountEnum.INVENTORY)).isEqualTo(0);
        assertThat(dao.getAccountBalance(AccountEnum.SALES_TAX_PAYABLE)).isEqualTo(-8);
    }

    @Test
    public void testPayEmployee() {
        clearAll();
        dao.updateAccount(AccountEnum.CASH, 100);

        PayrollRequest request = new PayrollRequest(100, "you gotta pay up!");
        long id = dao.payEmployee(request);

        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("you gotta pay up!");
        assertThat(transaction.getEntries().size()).isEqualTo(1);

        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getPay());
        assertThat(first.getFromAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(first.getToAccountId()).isEqualTo(AccountEnum.EMPLOYEES.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(AccountEnum.CASH)).isEqualTo(0);
        assertThat(dao.getAccountBalance(AccountEnum.EMPLOYEES)).isEqualTo(100);
    }

    @Test
    public void testPurchaseInventory() {
        clearAll();
        dao.updateAccount(AccountEnum.CASH, 50);

        InventoryRequest request = new InventoryRequest(50, "gimme phones");
        long id = dao.purchaseInventory(request);

        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("gimme phones");
        assertThat(transaction.getEntries().size()).isEqualTo(1);

        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getCostOfGoods());
        assertThat(first.getFromAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(first.getToAccountId()).isEqualTo(AccountEnum.INVENTORY.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(AccountEnum.CASH)).isEqualTo(0);
        assertThat(dao.getAccountBalance(AccountEnum.INVENTORY)).isEqualTo(50);
    }

    @Test
    public void testMakeRefund() {
        clearAll();
        dao.updateAccount(AccountEnum.CASH, 108);

        RefundRequest request = new RefundRequest(100, 10, "this phone sucks");
        long id = dao.makeRefund(request);

        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("this phone sucks");
        assertThat(transaction.getEntries().size()).isEqualTo(3);

        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getRefundAmount());
        assertThat(first.getFromAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(first.getToAccountId()).isEqualTo(AccountEnum.REFUNDS_PAID.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        Entry second = transaction.getEntries().get(1);
        assertThat(second.getAmount()).isEqualTo(request.getValueOfReturns());
        assertThat(second.getFromAccountId()).isEqualTo(AccountEnum.COGS.getState());
        assertThat(second.getToAccountId()).isEqualTo(AccountEnum.INVENTORY.getState());
        assertThat(second.getTransactionId()).isEqualTo(id);

        Entry third = transaction.getEntries().get(2);
        assertThat(third.getAmount()).isEqualTo(request.getRefundAmount() * 0.08);
        assertThat(third.getFromAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(third.getToAccountId()).isEqualTo(AccountEnum.SALES_TAX_PAYABLE.getState());
        assertThat(third.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(AccountEnum.CASH)).isEqualTo(0);
        assertThat(dao.getAccountBalance(AccountEnum.REFUNDS_PAID)).isEqualTo(100);
        assertThat(dao.getAccountBalance(AccountEnum.COGS)).isEqualTo(-10);
        assertThat(dao.getAccountBalance(AccountEnum.INVENTORY)).isEqualTo(10);
        assertThat(dao.getAccountBalance(AccountEnum.SALES_TAX_PAYABLE)).isEqualTo(8);
    }

    @Test
    public void testInvest() {
        clearAll();

        InvestmentRequest request = new InvestmentRequest(1, "1 dollar");
        long id = dao.invest(request);

        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("1 dollar");
        assertThat(transaction.getEntries().size()).isEqualTo(1);

        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getAmount());
        assertThat(first.getFromAccountId()).isEqualTo(AccountEnum.INVESTMENT.getState());
        assertThat(first.getToAccountId()).isEqualTo(AccountEnum.CASH.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(AccountEnum.CASH)).isEqualTo(1);
        assertThat(dao.getAccountBalance(AccountEnum.INVESTMENT)).isEqualTo(-1);
    }
}
