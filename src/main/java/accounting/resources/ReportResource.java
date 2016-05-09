package accounting.resources;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {


    @Inject
    public ReportResource() {
    	
    }
    
    public void generatePDF() throws JRException, IOException {
    	String reportTemplate = "";
    	
    	DateTime month = DateTime.now();
    	month.minusDays(month.getDayOfMonth());
    	month.minusHours(month.getHourOfDay());
    	String reportName = month.monthOfYear().getAsString() + "Report";
    	
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("ReportTitle", reportName);
    	parameters.put("Date", month);
    	JasperPrint jasperprint = JasperFillManager.fillReport(reportTemplate, parameters);
    	
    	//need to change so it goes to downloads folder
    	OutputStream output = new FileOutputStream(new File("c:/output/MonthlyReport.pdf")); 
    	JasperExportManager.exportReportToPdfStream(jasperprint, output); 
    	output.close();
    	
    }

}
