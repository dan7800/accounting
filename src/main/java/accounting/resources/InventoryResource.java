package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.InventoryRequest;
import accounting.models.Transaction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private TransactionDAO transactionDAO;
    private String inventoryKey;

    @Inject
    public InventoryResource(TransactionDAO transactionDAO, @Named("inventoryKey") String inventoryKey) {
        this.transactionDAO = transactionDAO;
        this.inventoryKey = inventoryKey;
    }

    @POST
    public long post(InventoryRequest inventoryRequest, @QueryParam("apiKey") String apiKey) {
        if(!inventoryKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return transactionDAO.purchaseInventory(inventoryRequest);
    }
}
