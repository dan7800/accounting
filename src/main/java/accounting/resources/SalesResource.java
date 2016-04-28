package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.SaleRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The resource for the sale endpoint.  Each resource just has each endpoint
 * annotated with its proper type.
 */
@Path("/sale")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalesResource {

    // Each endpoint will likely need the DAO to change the database.
    private TransactionDAO transactionDAO;
    private String salesKey;

    // Dependencies are injected
    @Inject
    public SalesResource(TransactionDAO transactionDAO, @Named("salesKey") String salesKey) {
        this.transactionDAO = transactionDAO;
        this.salesKey = salesKey;
    }

    @POST
    public long post(SaleRequest saleRequest, @QueryParam("apiKey") String apiKey) {
        if(!salesKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return transactionDAO.makeSale(saleRequest);
    }
}
