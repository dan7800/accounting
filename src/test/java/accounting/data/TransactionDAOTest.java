package accounting.data;

import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDAOTest {
    private TransactionDAO dao;

    @Before
    public void setup() {
        DBI dbi = new DBI("jdbc:mysql://localhost:3306/accounting", "root", "root");
        this.dao = dbi.onDemand(TransactionDAO.class);
    }

    @Test
    public void insertTransaction() {
        long id = dao.insertTransaction("hello");
        assertThat(dao.get(id)).isNotNull();
    }
}
