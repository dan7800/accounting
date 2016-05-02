package accounting.resources;

import accounting.data.TransactionDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tax")
@Produces(MediaType.APPLICATION_JSON)
public class TaxResource {

    private TransactionDAO transactionDAO;
    private String salesKey;

    @Inject
    public TaxResource(TransactionDAO transactionDAO, @Named("salesKey") String salesKey) {
        this.transactionDAO = transactionDAO;
        this.salesKey = salesKey;
    }

    @GET
    @Path("/sales/{salePrice}")
    public double get(@PathParam("salePrice") double salePrice, @QueryParam("apiKey") String apiKey) {
        if(!salesKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return salePrice*1.08;
    }
}
