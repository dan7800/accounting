package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.PayrollRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payroll")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PayrollResource {

    private TransactionDAO transactionDAO;
    private String humanResourcesKey;

    @Inject
    public PayrollResource(TransactionDAO transactionDAO, @Named("humanResourcesKey") String humanResourcesKey) {
        this.transactionDAO = transactionDAO;
        this.humanResourcesKey = humanResourcesKey;
    }

    @POST
    public long post(PayrollRequest payrollRequest, @QueryParam("apiKey") String apiKey) {
        if(!humanResourcesKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return transactionDAO.payEmployee(payrollRequest);
    }
}
