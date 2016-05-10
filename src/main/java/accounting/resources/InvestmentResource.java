package accounting.resources;

import accounting.data.TransactionDAO;
import accounting.models.InvestmentRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/investment")
@Produces(MediaType.APPLICATION_JSON)
public class InvestmentResource {

    private TransactionDAO transactionDAO;
    private String investmentKey;

    @Inject
    public InvestmentResource(TransactionDAO transactionDAO, @Named("investmentKey") String investmentKey) {
        this.transactionDAO = transactionDAO;
        this.investmentKey = investmentKey;
    }

    @POST
    public long post(@Valid InvestmentRequest investmentRequest, @QueryParam("apiKey") String apiKey) {
        if (!investmentKey.equals(apiKey)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return transactionDAO.invest(investmentRequest);
    }
}
