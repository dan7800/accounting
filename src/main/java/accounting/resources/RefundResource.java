package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.RefundRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/refund")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RefundResource {

    private TransactionDAO transactionDAO;
    private String salesKey;

    @Inject
    public RefundResource(TransactionDAO transactionDAO, @Named("salesKey") String salesKey) {
        this.transactionDAO = transactionDAO;
        this.salesKey = salesKey;
    }

    @POST
    public long post(RefundRequest refundRequest, @QueryParam("apiKey") String apiKey) {
        if(!salesKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return transactionDAO.makeRefund(refundRequest);
    }
}
