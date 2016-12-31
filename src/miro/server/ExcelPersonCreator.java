package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import miro.shared.Allocation;

public class ExcelPersonCreator 
{
	WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	
	private static final String FORMATION="Formations";
	private static final String ABSENCES="Congés & Absences";
	private static final String HORS_PROJETS="Activités Hors Projets";
	private WritableSheet sheet ;
	private String [] months= {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	private List<String> projectNoms= new ArrayList <String>();
	private Allocation formationAllocation, absencesAllocation,hors_projetsAllocation;
	private List<Allocation> projects= new ArrayList <Allocation>();
	private double  [] totalJours = {260.00,22.00,20.00,23.00,20.00,23.00,22.00,21.00,23.00,21.00,22.00,22.00,21.00};
	private  double [] joursDisponiblesProjets;
	private double [] totalsMonth={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
	public ByteArrayOutputStream generateExcelReport(List <Allocation>allocations,String nom) throws IOException, WriteException {
		  // Stream containing excel data
		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		 
		  doInit(allocations);
		 
		  
		  // Create Excel WorkBook and Sheet
		  WritableWorkbook workBook = Workbook.createWorkbook(outputStream);
		  sheet = workBook.createSheet(nom, 0);
		  
		  doHeaderRow();
		  doInformations();
		 
		  // Write & Close Excel WorkBook
		  workBook.write();
		  workBook.close();
		 
		  return outputStream;
		}
	
	public void doInit(List<Allocation> allocations) {
		
		joursDisponiblesProjets=totalJours;
		
		for (Allocation allocation:allocations) {
			
			if(allocation.getActivityName().equals(FORMATION)) {
				formationAllocation =allocation;
			}
			else if(allocation.getActivityName().equals(ABSENCES)) {
				absencesAllocation =allocation;
			}
			else if(allocation.getActivityName().equals(HORS_PROJETS)) {
				hors_projetsAllocation =allocation;
			}
			else {
				projectNoms.add(allocation.getMissionName());
				projects.add(allocation);
			}
		}
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
	
	public void doInformations() throws WriteException {
		
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setWrap(true);
		
		WritableFont header1Font = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat header1Format = new WritableCellFormat(header1Font);
		header1Format.setBackground(Colour.LIGHT_GREEN);
		header1Format.setWrap(true);
		WritableCellFormat cfh1 = new WritableCellFormat(NumberFormats.FLOAT);
		cfh1.setBackground(Colour.LIGHT_GREEN);

		WritableFont header2Font = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat header2Format = new WritableCellFormat(header2Font);
		header2Format.setBackground(Colour.LIGHT_ORANGE);
		header2Format.setWrap(true);
		WritableCellFormat cfh2 = new WritableCellFormat(NumberFormats.FLOAT);
		cfh2.setBackground(Colour.LIGHT_ORANGE);
		
		WritableFont header3Font = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		WritableCellFormat header3Format = new WritableCellFormat(header3Font);
		header3Format.setBackground(Colour.YELLOW2);
		header3Format.setWrap(true);
		WritableCellFormat cfh3 = new WritableCellFormat(NumberFormats.FLOAT);
		cfh3.setBackground(Colour.YELLOW2);
		
		int columm = 1;		
		
		sheet.addCell(new Label(0, 1, "Nombre Total de Jours", header1Format));
		sheet.addCell(new Label(0, 2, ABSENCES, headerInformationFormat));
		sheet.addCell(new Label(0, 3, FORMATION, headerInformationFormat));
		sheet.addCell(new Label(0, 4, HORS_PROJETS, headerInformationFormat));
		
		//Nombre Total de Jours
		for( int a=0;a<totalJours.length;a++) {
			sheet.addCell(new Number(columm,1,totalJours[a],cfh1 ));
			columm++;
		}
				
		//ABSENCES
		columm = 2;
		double totalAbsences=0.00;
		
		for( int a=0;a<12;a++) { 
			
			double absence = absencesAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-absence;
			totalAbsences = totalAbsences+absence;
			sheet.addCell(new Number(columm, 2,absence,cf2 ));
			columm++;
		}
		sheet.addCell(new Number(1, 2,totalAbsences,cf2 ));
		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalAbsences;
		
		// FORMATION
		columm = 2;
		double totalFormation=0.00;
		
		for( int a=0;a<12;a++) {
			
			double formation = formationAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-formation;
			totalFormation=totalFormation+formation;
			sheet.addCell(new Number(columm, 3,formation,cf2 ));
			columm++;
		}
		sheet.addCell(new Number(1, 3,totalFormation,cf2 ));
		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalFormation;
		
		//HORS_PROJETS
		columm = 2;
		double totalHors_projets= 0.00;
		
		for( int a=0;a<12;a++) {
			
			double hors_projets = hors_projetsAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-hors_projets;
			totalHors_projets=totalHors_projets+hors_projets;
			sheet.addCell(new Number(columm, 4,hors_projets,cf2 ));
			columm++;
		}
		sheet.addCell(new Number(1, 4,totalHors_projets,cf2 ));
		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalHors_projets;
		
		//Jours disponibles projets
		columm = 1;
		sheet.addCell(new Label(0, 5, "Jours disponibles projets", header2Format));
		 
		for( int a=0;a<joursDisponiblesProjets.length;a++) {
			
			sheet.addCell(new Number(columm, 5,joursDisponiblesProjets[a],cfh2 ));
			columm++;
		}
		
		//projets
		int row = 6;
		
		for( String nom:projectNoms) {
			
			columm = 2;
			sheet.addCell(new Label(0, row,"  " +nom, headerInformationFormat));
			Allocation allocation = projects.get(row-6);			
			double totalProject=0.0;
			
			for( int a=0;a<12;a++) {	
				double projectMonth =allocation.getAllocation(a);
				sheet.addCell(new Number(columm, row,projectMonth,cf2 ));
				totalProject = totalProject+projectMonth;
				totalsMonth[columm]=totalsMonth[columm]+projectMonth;
				columm++;
			}
			sheet.addCell(new Number(1, row,totalProject,cf2 ));
			totalsMonth[1] = totalsMonth[1] + totalProject;
			totalProject = 0.00;
			row++;
		}
		
		//Total Projets
		columm = 1;
		sheet.addCell(new Label(0, row, "Total Projets", header3Format));
					
		for( int a=1;a<totalsMonth.length;a++)
		{
			sheet.addCell(new Number(columm, row,totalsMonth[a],cfh3 ));
			columm++;
		}

		sheet.setColumnView(0, 50);
	}

}
