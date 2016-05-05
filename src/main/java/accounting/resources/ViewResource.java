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
import java.util.HashMap;
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
        HashMap<String,Object> map = new HashMap<>();
        map.put("transactions",getTransactionList());
        try {
            // process the template with the data model output to stringWriter
            template.process(map, stringWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

}
