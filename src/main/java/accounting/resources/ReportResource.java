package accounting.resources;

import net.sf.jasperreports.*;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {


    @Inject
    public ReportResource() {
    	
    }

}
