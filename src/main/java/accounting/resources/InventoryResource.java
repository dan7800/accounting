package accounting.resources;

import accounting.data.DummyTransactionDAO;
import accounting.data.TransactionDAO;
import accounting.models.InventoryRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private TransactionDAO transactionDAO;

    @Inject
    public InventoryResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @POST
    public long post(InventoryRequest inventoryRequest) {
        return transactionDAO.purchaseInventory(inventoryRequest);
    }
}
