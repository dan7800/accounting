package accounting.data;

import accounting.models.Account;
import accounting.models.Entry;
import accounting.models.SaleRequest;
import accounting.models.Transaction;
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
        dao.clearTransactions();
        dao.clearEntries();
        dao.clearAccounts();
    }

    @Test
    public void testInsertTransaction() {
        long id = dao.insertTransaction("hello");
        assertThat(dao.get(id)).isNotNull();
    }

    @Test
    public void testMakeSale() {
        dao.updateAccount(Account.INVENTORY, 10);

        SaleRequest request = new SaleRequest(10, 100, "some description");
        long id = dao.makeSale(request);
        Transaction transaction = dao.get(id);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("some description");
        assertThat(transaction.getEntries().size()).isEqualTo(3);
        Entry first = transaction.getEntries().get(0);
        assertThat(first.getAmount()).isEqualTo(request.getSalePrice());
        assertThat(first.getFromAccountId()).isEqualTo(Account.REVENUES.getState());
        assertThat(first.getToAccountId()).isEqualTo(Account.CASH.getState());
        assertThat(first.getTransactionId()).isEqualTo(id);

        Entry second = transaction.getEntries().get(1);
        assertThat(second.getAmount()).isEqualTo(request.getCostOfGoodsSold());
        assertThat(second.getFromAccountId()).isEqualTo(Account.INVENTORY.getState());
        assertThat(second.getToAccountId()).isEqualTo(Account.COGS.getState());
        assertThat(second.getTransactionId()).isEqualTo(id);

        Entry third = transaction.getEntries().get(2);
        assertThat(third.getAmount()).isEqualTo(request.getSalePrice() * 0.08);
        assertThat(third.getFromAccountId()).isEqualTo(Account.SALES_TAX_PAYABLE.getState());
        assertThat(third.getToAccountId()).isEqualTo(Account.CASH.getState());
        assertThat(third.getTransactionId()).isEqualTo(id);

        assertThat(dao.getAccountBalance(Account.CASH)).isEqualTo(108);
        assertThat(dao.getAccountBalance(Account.REVENUES)).isEqualTo(-100);
        assertThat(dao.getAccountBalance(Account.COGS)).isEqualTo(10);
        assertThat(dao.getAccountBalance(Account.INVENTORY)).isEqualTo(0);
        assertThat(dao.getAccountBalance(Account.SALES_TAX_PAYABLE)).isEqualTo(-8);
    }
}
