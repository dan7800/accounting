package accounting.resources;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import io.dropwizard.db.DataSourceFactory;


@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

	private DataSourceFactory dsf;

    @Inject
    public ReportResource(DataSourceFactory dsf) {
    	this.dsf = dsf;
    }
    
    @SuppressWarnings("deprecation")
	public void generatePDF() throws JRException, IOException, SQLException {
    	String reportTemplate = "src/main/resources/ReportTemplate.jrxml";
    	
    	Date month = new Date();
    	month.setDate(1);
		String reportName = month.getMonth() + " Report";
    	
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("ReportTitle", reportName);
    	parameters.put("Date", month);
    	JasperReport compiledReport = JasperCompileManager.compileReport(reportTemplate);
    	Connection connection = DriverManager.getConnection(dsf.getUrl(), dsf.getUser(), dsf.getPassword());
    	JasperPrint jasperprint = JasperFillManager.fillReport(compiledReport, parameters, connection);
    	
    	OutputStream output = new FileOutputStream(new File(System.getProperty("user.home") + "/Downloads/MonthlyReport.pdf")); 
    	JasperExportManager.exportReportToPdfStream(jasperprint, output); 
    	output.close();
    	
    }

}
