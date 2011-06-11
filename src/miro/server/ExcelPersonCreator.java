package miro.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
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
	
	private DecimalFormat df = new DecimalFormat("#.##");
	private static final String FORMATION="Formations";
	private static final String ABSENCES="Congés & Absences";
	private static final String HORS_PROJETS="Activités Hors Projets";
	private WritableSheet sheet ;
	private String [] months= {"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Decembre"};
	private List<String> projectNoms= new ArrayList <String>();
	private List<String> projectsGenerique= new ArrayList <String>();
	private Allocation formationAllocation, absencesAllocation,hors_projetsAllocation;
	private List<Allocation> projects= new ArrayList <Allocation>();
	private double  [] totalJours = {260.00,21.00,20.00,23.00,21.00,22.00,22.00,21.00,23.00,22.00,21.00,22.00,22.00};
	private double [] congesLegaux = {15.00,0.00,0.00,0.00,1.00,0.00,3.00,2.00,1.00,0.00,1.00,2.00,5.00};
	private  double [] joursDisponiblesProjets;
	private  double multiply = 100.00;
	private double [] totalsMonth={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	private String [] columms={"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};
	
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
	
	public void doInit(List<Allocation> allocations)
	{
		joursDisponiblesProjets=totalJours;
		
		for (Allocation allocation:allocations)
		{
			
			if(allocation.getActivityName().equals(FORMATION))
			{
				formationAllocation =allocation;

			}
			else
				if(allocation.getActivityName().equals(ABSENCES))
				{
					absencesAllocation =allocation;
				}
				else
					if(allocation.getActivityName().equals(HORS_PROJETS))
					{
						hors_projetsAllocation =allocation;
					}
					else
					{
						projectNoms.add(allocation.getMissionName());
						projects.add(allocation);
					}
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
	
	
	
	public void doInformations() throws WriteException
	{
		WritableFont headerInformationFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.NO_BOLD);
		WritableCellFormat headerInformationFormat = new WritableCellFormat(headerInformationFont);
		headerInformationFormat.setWrap(true);
		int columm = 1;
		
		
		sheet.addCell(new Label(0, 1, "Nombre Total de Jours", headerInformationFormat));
		sheet.addCell(new Label(0, 2, "Conges legaux & CIRB", headerInformationFormat));
		sheet.addCell(new Label(0, 3, ABSENCES, headerInformationFormat));
		sheet.addCell(new Label(0, 4, FORMATION, headerInformationFormat));
		sheet.addCell(new Label(0, 5, HORS_PROJETS, headerInformationFormat));
		
		//Nombre Total de Jours
		for( int a=0;a<totalJours.length;a++)
		{
			sheet.addCell(new Number(columm,1,totalJours[a],cf2 ));
			columm++;
		}
		
		
		//Conges legaux & CIRB"
		columm = 1;
		for( int a=0;a<congesLegaux.length;a++)
		{
			sheet.addCell(new Number(columm,2,congesLegaux[a],cf2 ));
			columm++;
		}
	
		//ABSENCES
		columm = 2;
		double totalAbsences=0.00;
		for( int a=0;a<12;a++)
		{
			double absence = absencesAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-absence;
			totalAbsences = totalAbsences+absence;
			sheet.addCell(new Number(columm, 3,absence,cf2 ));

			columm++;
		}
		sheet.addCell(new Number(1, 3,totalAbsences,cf2 ));
		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalAbsences;
		// FORMATION
		columm = 2;
		double totalFormation= 0.00;
		for( int a=0;a<12;a++)
		{
			double formation = formationAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-formation;
			totalFormation=totalFormation+formation;
			sheet.addCell(new Number(columm, 4,formation,cf2 ));

			columm++;
		}
		sheet.addCell(new Number(1, 4,totalFormation,cf2 ));

		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalFormation;
		
		//HORS_PROJETS
		
		columm = 2;
		double totalHors_projets= 0.00;
		for( int a=0;a<12;a++)
		{
			double hors_projets = hors_projetsAllocation.getAllocation(a);
			joursDisponiblesProjets[a+1]=joursDisponiblesProjets[a+1]-hors_projets;
			totalHors_projets=totalHors_projets+hors_projets;
			sheet.addCell(new Number(columm, 5,hors_projets,cf2 ));
			columm++;
		}
		sheet.addCell(new Number(1, 5,totalHors_projets,cf2 ));
		joursDisponiblesProjets[0]=joursDisponiblesProjets[0]-totalHors_projets;
		
	
		//Jours disponibles projets
		columm = 1;
		sheet.addCell(new Label(0, 6, "Jours disponibles projets", headerInformationFormat));
		for( int a=0;a<joursDisponiblesProjets.length;a++)
		{
			sheet.addCell(new Number(columm, 6,joursDisponiblesProjets[a],cf2 ));
			columm++;
		}
		
		//projets
		int row = 7;
		for( String nom:projectNoms)
		{
			columm = 2;
			
			sheet.addCell(new Label(0, row,"  " +nom, headerInformationFormat));
			Allocation allocation = projects.get(row-7);
			
			double totalProject=0.0;
			for( int a=0;a<12;a++)
			{
				double projectMonth =allocation.getAllocation(a);
				totalProject=totalProject+projectMonth;
				totalsMonth[a+1]=totalsMonth[a+1]+projectMonth;
				totalsMonth[0]=	totalsMonth[0]+projectMonth;
				sheet.addCell(new Number(columm, row,projectMonth,cf2 ));
				columm++;
			}
			sheet.addCell(new Number(1, row,totalProject,cf2 ));

			row++;
		}
		//Total Projets
		columm = 1;
		sheet.addCell(new Label(0, row, "Total Projets", headerInformationFormat));
		for( int a=0;a<totalsMonth.length;a++)
		{
			sheet.addCell(new Formula(columm,row,"SUM("+columms[columm]+"8:"+columms[columm]+String.valueOf(row)+")", cf2));
			columm++;
		}
		
		NumberFormat dp3 = new NumberFormat("0.00");
		WritableCellFormat dp3cell = new WritableCellFormat(dp3);
		    
		columm = 1;
		//% ABSENCES
		sheet.addCell(new Label(0, row+1, "% "+ABSENCES, headerInformationFormat));
		//% FORMATION
		sheet.addCell(new Label(0, row+2, "% "+FORMATION, headerInformationFormat));
		//% HORS_PROJETS
		sheet.addCell(new Label(0, row+3, "% "+HORS_PROJETS, headerInformationFormat));
		//% PROJETS
		sheet.addCell(new Label(0, row+4, "% Projets", headerInformationFormat));
		//Total en %
		sheet.addCell(new Label(0, row+5, "Total en %", headerInformationFormat));
		//KPI
		sheet.addCell(new Label(0, row+6, "KPI - % Affectation Projets", headerInformationFormat));
		for( int a=1;a<columms.length;a++)
		{
			//% ABSENCES
			sheet.addCell(new Formula(columm, row+1,"("+columms[columm]+"4/"+columms[columm]+"2)*100", dp3cell));
			//% FORMATION
			sheet.addCell(new Formula(columm, row+2,"("+columms[columm]+"5/"+columms[columm]+"2)*100", dp3cell));
			//% HORS_PROJETS
			sheet.addCell(new Formula(columm, row+3,"("+columms[columm]+"6/"+columms[columm]+"2)*100", dp3cell));
			//% PROJETS
			sheet.addCell(new Formula(columm, row+4,"("+columms[columm]+String.valueOf(row+1)+"/"+columms[columm]+"2)*100", dp3cell));
			//Total en %
			sheet.addCell(new Formula(columm, row+5,"SUM("+columms[columm]+String.valueOf(row+2)+":"+columms[columm]+String.valueOf(row+5) +")", dp3cell));
			//KPI
			sheet.addCell(new Formula(columm, row+6,
					"("+columms[columm]+String.valueOf(row+1)+
					"/("+columms[columm]+"5+"
					+columms[columm]+"6+"
					+String.valueOf(joursDisponiblesProjets[columm-1])
					+"))*100", dp3cell));			
			columm++;
		}
		sheet.setColumnView(0, 50);
	}

}
