package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import miro.shared.Allocation;



public class DBdumpCreator {

	private List <Allocation> allocations;
	private WritableSheet sheet ;


	public ByteArrayOutputStream generateDump(List<Allocation> allocations) throws IOException, WriteException {
		
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		 
		  this.allocations=allocations;
		  
		  // Create Excel WorkBook and Sheet
		  WritableWorkbook workBook = Workbook.createWorkbook(outputStream);
		  sheet = workBook.createSheet("dump", 0);
		  
		  WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		  WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
			
		 //ACC Fullname;Year of D Date;Month of D Date;TS Mission Type Name (a);TS Mission Name (Actual);TS Activity Name;F Works In Days
		  
		 sheet.addCell(new Label(0, 0, "ACC Fullname", headerInformationFormat));
		 sheet.addCell(new Label(1, 0, "Year of D Date", headerInformationFormat));
		 sheet.addCell(new Label(2, 0, "Month of D Date", headerInformationFormat));
		 sheet.addCell(new Label(3, 0, "TS Mission Type Name (a)", headerInformationFormat));
		 sheet.addCell(new Label(4, 0, "TS Mission Name (Actual)", headerInformationFormat));
		 sheet.addCell(new Label(5, 0, "TS Activity Name", headerInformationFormat));
		 sheet.addCell(new Label(6, 0, "F Works In Days", headerInformationFormat));
		  int row =1;
		  for(Allocation allocation:allocations)
		  {
			  for(int i=0;i<12;i++)
			  {
					sheet.addCell(new Label(0, row, allocation.getPersonFullName(), headerInformationFormat));
					sheet.addCell(new Label(1, row, "2017", headerInformationFormat));
					sheet.addCell(new Label(2, row, String.valueOf(i+1), headerInformationFormat));
					
					String type="";
					
					if(allocation.getMissionName().startsWith("TTR P"))
						type="Projet";
					else
						if(allocation.getMissionName().startsWith("TTR S"))
							type="Service";
						else
							if(allocation.getMissionName().startsWith("TTR G"))
								type="Générique";
					
					sheet.addCell(new Label(3, row, type, headerInformationFormat));
					sheet.addCell(new Label(4, row, allocation.getMissionName(), headerInformationFormat));
					
					String activityName=allocation.getActivityName();
					
					  if (activityName.equalsIgnoreCase("Projets")){
				        	activityName ="Total" ;
				        }
					  else
				        if (activityName.equalsIgnoreCase( "Congés & Absences")){
				        	activityName = "Absence";
				        }
				        else
				        if (activityName.equalsIgnoreCase("Activités Hors Projets")){
				        	activityName = "OTHERS";
				        }
				 
					sheet.addCell(new Label(5, row, activityName, headerInformationFormat));
					sheet.addCell(new Label(6, row, String.valueOf(allocation.getAllocation(i)), headerInformationFormat));
					row ++;	
			  }
		  }
		
		  // Write & Close Excel WorkBook
		  workBook.write();
		  workBook.close();
		 
		  return outputStream;
		}

}
