package accounting.resources;

import accounting.data.DummyTransactionDAO;
import accounting.data.TransactionDAO;
import accounting.models.Transaction;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private TransactionDAO transactionDAO;

    @Inject
    public TransactionResource(TransactionDAO transactionDAO) {
        this.transactionDAO = new DummyTransactionDAO();
    }

    @GET
    @Path("/{id}")
    public Transaction get(@PathParam("id") long id) {
        return transactionDAO.get(id);
    }
}
