package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import miro.shared.Allocation;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ExportServlet extends HttpServlet {
 

	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    try {
		 String nom = request.getParameter("name");
		 
	      // Get Excel Data
	      ByteArrayOutputStream bytes=null;
	      List<Allocation> allocations;
	      if(nom.startsWith("TTR") )
	      {
	    	  allocations = getAllocationsForProject(nom);
	    	  bytes= new ExcelProjectCreator().generateExcelReport(allocations,nom);
	      }
	      else
	      if(nom.equals("fullDB") )
	      {
	    	  allocations = getAllAllocations();
	    	  bytes= new DBdumpCreator().generateDump(allocations);
	      }
	      else
	      {
	    	  allocations = getAllocationsForPerson(nom);
		      bytes= new ExcelPersonCreator().generateExcelReport(allocations,nom);
  
	      }
	      // Initialize Http Response Headers
	     response.setHeader("Content-disposition", "attachment; filename="+nom+".xls");
	     response.setContentType("application/vnd.ms-excel");
	   
	      // Write data on response output stream
	      if (bytes != null) 
	        response.getOutputStream().write(bytes.toByteArray());
	     
	    } catch (WriteException e) {
	      response.setContentType("text/plain");
	      response.getWriter().print("An error as occured");
	    }
	  }
	  
	  public List<Allocation> getAllocationsForProject(String projectNom) {
			
			List<Allocation> allocationsList = new ArrayList<Allocation>();
			
			ObjectifyService.register(Allocation.class);

			Objectify ofy = ObjectifyService.begin();

			Query<Allocation> query = ofy.query(Allocation.class);
			query.filter("missionName =", projectNom);
			for (Allocation allocationFromQuery : query) {
				allocationsList.add(allocationFromQuery);
			}
			return allocationsList;
		}
	  
	  public List<Allocation> getAllocationsForPerson(String nom) {
			
			List<Allocation> allocationsList = new ArrayList<Allocation>();
			
			ObjectifyService.register(Allocation.class);

			Objectify ofy = ObjectifyService.begin();

			Query<Allocation> query = ofy.query(Allocation.class);
			query.filter("personFullName =", nom);
			for (Allocation allocationFromQuery : query) {
				allocationsList.add(allocationFromQuery);
			}
			return allocationsList;
		}
	  
	  public List<Allocation> getAllAllocations() {
			
			List<Allocation> allocationsList = new ArrayList<Allocation>();
			
			ObjectifyService.register(Allocation.class);

			Objectify ofy = ObjectifyService.begin();

			Query<Allocation> query = ofy.query(Allocation.class);
			query.order("personFullName");
			for (Allocation allocationFromQuery : query) {
				allocationsList.add(allocationFromQuery);
			}
			return allocationsList;
		}
	}