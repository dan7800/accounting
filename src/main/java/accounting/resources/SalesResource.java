package accounting.resources;

import accounting.data.DummyTransactionDAO;
import accounting.data.TransactionDAO;
import accounting.models.SaleRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    // Dependencies are injected
    @Inject
    public SalesResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @POST
    public long post(SaleRequest saleRequest) {
        return transactionDAO.makeSale(saleRequest);
    }
}
