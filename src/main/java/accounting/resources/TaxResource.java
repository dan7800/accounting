package accounting.resources;

import accounting.data.DummyTransactionDAO;
import accounting.data.TransactionDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tax")
@Produces(MediaType.APPLICATION_JSON)
public class TaxResource {

    private TransactionDAO transactionDAO;

    @Inject
    public TaxResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @GET
    @Path("/sales/{salePrice}")
    public double get(@PathParam("salePrice") double salePrice) {
        return salePrice*1.08;
    }
}
