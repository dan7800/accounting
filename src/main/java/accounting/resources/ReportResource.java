package accounting.resources;

import io.dropwizard.db.DataSourceFactory;
import net.sf.jasperreports.engine.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Path("/pdf-report")
public class ReportResource {

	private DataSourceFactory dsf;

    @Inject
    public ReportResource(DataSourceFactory dsf) {
    	this.dsf = dsf;
    }

    @GET
    @Produces({"application/pdf"})
	public StreamingOutput generatePDF() throws JRException, IOException, SQLException {
        String reportTemplate = "src/main/resources/ReportTemplate.jrxml";

        LocalDate month = LocalDate.now();
        month = month.withDayOfMonth(1);

        String reportName = (month.getMonth().getValue()) + "/" + (month.getYear()) + " Report";

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ReportTitle", reportName);
        parameters.put("Date", Date.from(month.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        JasperReport compiledReport = JasperCompileManager.compileReport(reportTemplate);

        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                try {
                    Connection connection = DriverManager.getConnection(dsf.getUrl(), dsf.getUser(), dsf.getPassword());
                    JasperPrint jasperprint = JasperFillManager.fillReport(compiledReport, parameters, connection);

                    JasperExportManager.exportReportToPdfStream(jasperprint, outputStream);
                } catch ( JRException e ) {
                    e.printStackTrace();
                } catch ( SQLException e ) {
                    e.printStackTrace();
                }
            }
        };
    }

}
