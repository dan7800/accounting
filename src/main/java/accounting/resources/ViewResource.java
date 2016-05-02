package accounting.resources;

import accounting.data.TransactionDAO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ViewResource {

    private TransactionDAO transactionDAO;

    @Inject
    public ViewResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String get() {
        return "";
    }
}
