package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import miro.shared.Allocation;

public class ExcelProjectCreator 
{
	WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	private DecimalFormat df = new DecimalFormat("#.##");
	private WritableSheet sheet ;
	private String [] months= {"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
	private int row=1;
	private List <Allocation>allocations;
	private String [] columms={"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

	public ByteArrayOutputStream generateExcelReport(List <Allocation>allocations,String nom) throws IOException, WriteException {
		  // Stream containing excel data
		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		 
		  this.allocations=allocations;
		  
		  // Create Excel WorkBook and Sheet
		  WritableWorkbook workBook = Workbook.createWorkbook(outputStream);
		  sheet = workBook.createSheet(nom, 0);
		  
		  doHeaderRow();
		  doProjectMembers();
		  doFooterRow();
		  
		  sheet.setColumnView(0, 50);

		  // Write & Close Excel WorkBook
		  workBook.write();
		  workBook.close();
		 
		  return outputStream;
		}

	public void doProjectMembers() throws RowsExceededException, WriteException 
	{
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setWrap(true);
		
		for( Allocation allocation:allocations)
		{
			sheet.addCell(new Label(0, row, allocation.getPersonFullName(), headerInformationFormat));
			int columm =2;
			sheet.addCell(new Formula(1,row,"SUM( C"+String.valueOf(row+1)+":N" +String.valueOf(row+1)+")",cf2 ));

			for(int a=0;a<months.length;a++)
			{
				sheet.addCell(new Number(columm, row,allocation.getAllocation(a),cf2 ));
				columm ++;
			}
			row++;
		}
		
	}
	
	public void doHeaderRow() throws WriteException
	{
		WritableFont headerRowFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat headerRowFormat = new WritableCellFormat(headerRowFont);
		headerRowFormat.setBackground(Colour.PALE_BLUE);
		
		sheet.addCell(new Label(1, 0, "2011", headerRowFormat));
		
		int columm = 2;
		
		for( int a=0;a<months.length;a++)
		{
			sheet.addCell(new Label(columm, 0,months[a] , headerRowFormat));
			columm++;
		}
		
	}
	
	public void doFooterRow() throws WriteException
	{
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setWrap(true);
		
		
		sheet.addCell(new Label(0, row, "Total prestations", headerInformationFormat));
		
		int columm = 1;
		
		for( int a=0;a<=months.length;a++)
		{
			sheet.addCell(new Formula(columm, row,"SUM("+columms[columm] +"1:"+columms[columm] +String.valueOf(row)+")" , cf2));
			columm++;
		}
		
	}


}
