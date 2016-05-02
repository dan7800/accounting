package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.Transaction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private TransactionDAO transactionDAO;
    private String inventoryKey;
    private String humanResourcesKey;
    private String salesKey;

    @Inject
    public TransactionResource(TransactionDAO transactionDAO,
                               @Named("inventoryKey") String inventoryKey,
                               @Named("humanResourcesKey") String humanResourcesKey,
                               @Named("salesKey") String salesKey) {
        this.transactionDAO = transactionDAO;
        this.inventoryKey = inventoryKey;
        this.humanResourcesKey = humanResourcesKey;
        this.salesKey = salesKey;
    }

    @GET
    @Path("/{id}")
    public Transaction get(@PathParam("id") long id, @QueryParam("apiKey") String apiKey) {
        if((!inventoryKey.equals(apiKey)) &&
            (!humanResourcesKey.equals(apiKey)) &&
            (!salesKey.equals(apiKey))) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return transactionDAO.get(id);
    }
}
