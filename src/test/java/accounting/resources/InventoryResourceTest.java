package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.InventoryRequest;
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

public class InventoryResourceTest {

    private static final TransactionDAO dao = mock(TransactionDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = new ResourceTestRule.Builder()
            .addResource(new InventoryResource(dao))
            .build();

    private final InventoryRequest inventoryRequest = new InventoryRequest(3.77, "Bought some inventory");

    @Before
    public void setup() {
        when(dao.purchaseInventory(any(InventoryRequest.class))).thenReturn(5l);
    }

    @After
    public void teardown() {
        reset(dao);
    }

    @Test
    public void testMakeSale() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(inventoryRequest);
        Response response = resources.client()
                .target("/inventory")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonString));

        assertThat(response.readEntity(Long.class)).isEqualTo(5l);
    }
}
