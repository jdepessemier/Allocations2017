package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
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
	private WritableSheet sheet ;
	private String [] months= {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	private int row=1;
	private List <Allocation> allocations;
	private double [] totalsMonth={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};


	public ByteArrayOutputStream generateExcelReport(List <Allocation>allocations,String nom) throws IOException, WriteException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		 
		  this.allocations=allocations;
		  
		  WritableWorkbook workBook = Workbook.createWorkbook(outputStream);
		  sheet = workBook.createSheet(nom, 0);
		  
		  doHeaderRow();
		  doProjectMembers();
		  doFooterRow();
		  
		  sheet.setColumnView(0, 50);

		  workBook.write();
		  workBook.close();
		 
		  return outputStream;
		}
	
	public void doHeaderRow() throws WriteException {
		
		WritableFont headerRowFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat headerRowFormat = new WritableCellFormat(headerRowFont);
		headerRowFormat.setBackground(Colour.PALE_BLUE);
		

		sheet.addCell(new Label(0, 0, "", headerRowFormat));
		sheet.addCell(new Label(1, 0, "2017", headerRowFormat));
		
		int columm = 2;
		
		for( int a=0;a<months.length;a++) {
			sheet.addCell(new Label(columm, 0,months[a] , headerRowFormat));
			columm++;
		}
	}

	public void doProjectMembers() throws RowsExceededException, WriteException {
		
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setWrap(true);
		double totalOfProjectRow = 0.00;
			
		for( Allocation allocation:allocations) {
			sheet.addCell(new Label(0, row, allocation.getPersonFullName(), headerInformationFormat));
			int columm =2;

			for(int a=0;a<months.length;a++) {
				sheet.addCell(new Number(columm, row,allocation.getAllocation(a),cf2 ));
				totalOfProjectRow = totalOfProjectRow + allocation.getAllocation(a);
				totalsMonth[columm] = totalsMonth[columm] + allocation.getAllocation(a);
				columm ++;
			}
			
			sheet.addCell(new Number(1, row,totalOfProjectRow,cf2 ));
			totalsMonth[1] = totalsMonth[1] + totalOfProjectRow;
			totalOfProjectRow = 0.00;
			row++;
		}	
	}
	
	public void doFooterRow() throws WriteException	{
		
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setBackground(Colour.YELLOW2);
		headerInformationFormat.setWrap(true);
		
		WritableCellFormat cf = new WritableCellFormat(NumberFormats.FLOAT);
		cf.setBackground(Colour.YELLOW2);
				
		sheet.addCell(new Label(0, row, "Total prestations", headerInformationFormat));
		
		int columm = 1;
		
		for( int a=1;a<=months.length+1;a++) {
			sheet.addCell(new Number(columm, row,totalsMonth[a],cf ));
			columm++;
		}
	}

}
