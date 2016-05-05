package accounting.resources;

import accounting.AccountingView;
import accounting.data.TransactionDAO;
import accounting.models.Transaction;
import freemarker.template.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ViewResource {

    private static TransactionDAO transactionDAO;
    private static List<Transaction> transactionList = null;
    private static Date lastUpdated = null;

    private List<Transaction> getTransactionList() {
        if ( transactionList == null ) transactionList = new ArrayList<Transaction>();

        Date current = new Date();
        int msBuffer = 1000; // only update transaction list every <msBuffer> ms
        // if last update was more than msBuffer milliseconds ago, update list
        if ( lastUpdated == null || lastUpdated.getTime() + msBuffer < current.getTime() ) {
            // update transaction list
            transactionList = transactionDAO.getAll();

            // update lastUpdated
            lastUpdated = current;
        }

        // return the up to <msBuffer> ms old transaction list
        return transactionList;
    }

    @Inject
    public ViewResource(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String get() {
        Writer stringWriter = new StringWriter();
        Template template = AccountingView.getTemplate();
        DataModel model = new DataModel(getTransactionList());
        try {
            // process the template with the data model output to stringWriter
            template.process(model, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    /*
        Data used in the page template
     */
    private class DataModel extends WrappingTemplateModel implements TemplateSequenceModel {
        List<Transaction> transactions;

        public DataModel( List<Transaction> transactions ) {
            this.transactions = transactions;
        }

        public TemplateModel get(int i) throws TemplateModelException {
            return new TransactionTemplateModel(transactions.get(i));
        }

        public int size() throws TemplateModelException {
            return transactions.size();
        }

        private class TransactionTemplateModel implements TemplateModel{

            Transaction transaction;

            public TransactionTemplateModel(Transaction transaction) {
                this.transaction = transaction;
            }

            long getId() {
                return transaction.getId();
            }

            String getDescription() {
                return transaction.toString();
            }
        }
    }
}
