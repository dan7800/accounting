package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.SaleRequest;
import accounting.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class TransactionResourceTest {

    private static final TransactionDAO dao = mock(TransactionDAO.class);
    private static final String apiKey = "aFakeKey";

    @ClassRule
    public static final ResourceTestRule resources = new ResourceTestRule.Builder()
            .addResource(new TransactionResource(dao, apiKey, apiKey, apiKey))
            .build();

    private final Transaction transaction = new Transaction(5l);

    @Before
    public void setup() {
        when(dao.get(5l)).thenReturn(transaction);
    }

    @After
    public void teardown() {
        reset(dao);
    }

    @Test
    public void testMakeSale() throws JsonProcessingException {
        Response response = resources.client()
                .target("/transactions/{id}?apiKey=aFakeKey")
                .resolveTemplate("id", 5l)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.readEntity(Transaction.class)).isEqualTo(transaction);
    }

    @Test
    public void noApiKey() throws JsonProcessingException {
        Response response = resources.client()
                .target("/transactions/{id}")
                .resolveTemplate("id", 5l)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void badApiKey() throws JsonProcessingException {
        Response response = resources.client()
                .target("/transactions/{id}?apiKey=badKey")
                .resolveTemplate("id", 5l)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
