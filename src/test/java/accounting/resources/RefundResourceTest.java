package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.RefundRequest;
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

public class RefundResourceTest {

    private static final TransactionDAO dao = mock(TransactionDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = new ResourceTestRule.Builder()
            .addResource(new RefundResource(dao))
            .build();

    private final RefundRequest refundRequest = new RefundRequest(80.55, 13.37, "Refund $80.55 for $13.37 worth of merchandise.");

    @Before
    public void setup() {
        when(dao.makeRefund(any(RefundRequest.class))).thenReturn(10l);
    }

    @After
    public void teardown() {
        reset(dao);
    }

    @Test
    public void testMakeRefund() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(refundRequest);
        Response response = resources.client()
                .target("/refund")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonString));

        assertThat(response.readEntity(Long.class)).isEqualTo(10l);
    }
}
