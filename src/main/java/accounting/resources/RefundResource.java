package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.RefundRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/refund")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RefundResource {

    private TransactionDAO transactionDAO;

    @Inject
    public RefundResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @POST
    public long post(RefundRequest refundRequest) {
        return transactionDAO.makeRefund(refundRequest);
    }
}
