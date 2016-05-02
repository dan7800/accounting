package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.PayrollRequest;
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

public class PayrollResourceTest {

    private static final TransactionDAO dao = mock(TransactionDAO.class);
    private static final String apiKey = "aFakeKey";

    @ClassRule
    public static final ResourceTestRule resources = new ResourceTestRule.Builder()
            .addResource(new PayrollResource(dao, apiKey))
            .build();

    private final PayrollRequest payrollRequest = new PayrollRequest(15.00, "Paid an employee");

    @Before
    public void setup() {
        when(dao.payEmployee(any(PayrollRequest.class))).thenReturn(33l); //Note that that is the number 1 then the letter l
    }

    @After
    public void teardown() {
        reset(dao);
    }

    @Test
    public void testMakeSale() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payrollRequest);
        Response response = resources.client()
                .target("/payroll?apiKey=aFakeKey")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonString));

        assertThat(response.readEntity(Long.class)).isEqualTo(33l);
    }

    @Test
    public void noApiKey() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payrollRequest);
        Response response = resources.client()
                .target("/payroll")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonString));

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void badApiKey() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(payrollRequest);
        Response response = resources.client()
                .target("/payroll?apiKey=badKey")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonString));

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
