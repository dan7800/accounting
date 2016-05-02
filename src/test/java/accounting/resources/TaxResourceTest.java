package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.SaleRequest;
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

public class TaxResourceTest {

    private static final TransactionDAO dao = mock(TransactionDAO.class);
    private static final String apiKey = "aFakeKey";

    @ClassRule
    public static final ResourceTestRule resources = new ResourceTestRule.Builder()
            .addResource(new TaxResource(dao, apiKey))
            .build();

    private final double salePrice = 100.00;
    private final double expected = 108.00;

    @Test
    public void testMakeSale() throws JsonProcessingException {
        Response response = resources.client()
                .target("/tax/sales/{salePrice}?apiKey=aFakeKey")
                .resolveTemplate("salePrice", salePrice)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.readEntity(Double.class)).isEqualTo(expected);
    }

    @Test
    public void noApiKey() throws JsonProcessingException {
        Response response = resources.client()
                .target("/tax/sales/{salePrice}")
                .resolveTemplate("salePrice", salePrice)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void badApiKey() throws JsonProcessingException {
        Response response = resources.client()
                .target("/tax/sales/{salePrice}?apiKey=badKey")
                .resolveTemplate("salePrice", salePrice)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
