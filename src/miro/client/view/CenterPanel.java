package miro.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.OfficialInformation;
import miro.shared.Person;
import miro.shared.Project;
import miro.shared.Record;
import miro.shared.Time;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CenterPanel extends Composite implements EventListener {

	interface GlobalResources extends ClientBundle {
		@CssResource.NotStrict
		@Source("style.css")
		CssResource css();
	}

	interface CenterPanelUiBinder extends UiBinder<ScrollPanel, CenterPanel> {
	}

	private static CenterPanelUiBinder ourUiBinder = GWT.create(CenterPanelUiBinder.class);
	private MonthRow monthRow = new MonthRow(OfficialInformation.CURRENT_YEAR);
	private CalcRow calcRowForProjectView = new CalcRow("Total prestations",false);
	private TitleRow[] titlesRowArray = new TitleRow[4];
	private CalcRow[] calcRowArray = new CalcRow[2];
	private List<ProjectRow> projectRowList = new ArrayList<ProjectRow>();
	private static List<EventListener> eventListenerList = new ArrayList<EventListener>();
	private int startIndex;
	private int currentMonth;

	@UiField
	FlexTable personArray;

	public CenterPanel() {

		GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();
		initWidget(ourUiBinder.createAndBindUi(this));

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				currentMonth = Integer.parseInt(result);
				refreshCenterPanel();
			}
		};
		MiroAccessDB.getMonthOfDate(callback);
		TopPanel.addEventListener(this);		
	}

	public static void addEventListener(EventListener eventListener) {
		eventListenerList.add(eventListener);
	}

	public void refreshCenterPanel() {
		
		if (PartagedDataBetweenPanel.hasChangedView) {
			
			PartagedDataBetweenPanel.hasChangedView = false;
			initStyleNameOfMonthRow();
			
			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				startIndex = 7;
				initTitleRowList();
				initTitleRowListeners();
				initCalcRowList();
				refreshTitleRowList();
				refreshProjectRowList();
				refreshCalcRowList();
				refreshStyleOfSumColumn();
				break;
			case PROJECT_VIEW:
				startIndex = 1;
				refreshProjectRowList();
				refreshCalcRowForProjectView();
				refreshStyleOfSumColumn();
				break;
			}
		} else {
			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				refreshTitleRowList();
				refreshProjectRowList();
				refreshCalcRowList();
				refreshStyleOfSumColumn();
				break;
			case PROJECT_VIEW:
				refreshProjectRowList();
				refreshCalcRowForProjectView();
				refreshStyleOfSumColumn();
				break;
			}
		}
		refreshArray();
	}
	
	private void initStyleNameOfMonthRow() {
		
		((TextBox) monthRow.getElementAt(0)).setStyleName("titleOfFirstColumn");

		for (int i = 1; i < monthRow.length(); i++) {
			((TextBox) monthRow.getElementAt(i)).removeStyleName("titleOfFirstRow");
			((TextBox) monthRow.getElementAt(i)).addStyleName("titleOfFirstRow");
		}
	
	}	
	
	private void initTitleRowList() {
		
		Record[] recordOfNumberDaysByMonth = OfficialInformation.numberDaysByMonthArray;

		titlesRowArray[0] = new TitleRow("Nombre Total de Jours", false);
		titlesRowArray[1] = new TitleRow("Conges et Absences", true);
		titlesRowArray[2] = new TitleRow("Formations", true);
		titlesRowArray[3] = new TitleRow("Activites Hors-Projets", true);

		for (int i = 2; i < titlesRowArray[0].length(); i++) {
			double valueOfNumberDaysByMonthArray = round(recordOfNumberDaysByMonth[i - 2].getNumber(),2);

			titlesRowArray[0].setElementAt(i, valueOfNumberDaysByMonthArray);
		}

		titlesRowArray[0].setElementAt(1, titlesRowArray[0].sumRow());
		
		((TextBox) titlesRowArray[0].getElementAt(0)).setStyleName("titleOfTotalNumberDays");
		
		for (int i = 1; i < titlesRowArray.length; i++) {
			((TextBox) titlesRowArray[i].getElementAt(0)).setStyleName("titleOfFirstColumn");
		}
	}
	
	private void initTitleRowListeners() {

		for (int i = 0; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				TextBox textboxOfTitleList = (TextBox) titlesRowArray[i].getElementAt(j);
				double valueOfTextbox = Double.valueOf(textboxOfTitleList.getText());
				textboxOfTitleList.addChangeHandler(new MyTitleListener(i + 1,j, valueOfTextbox));
			}
		}
	}
	
	private void initCalcRowList() {
		
		calcRowArray[0] = new CalcRow("Jours disponibles projets", false);
		calcRowArray[1] = new CalcRow("Total Projets", false);

		for (int i = 0; i < calcRowArray.length; i++) {
				((TextBox) calcRowArray[i].getElementAt(0)).setStyleName("titleOfFirstColumnCalculated ");
				((TextBox) calcRowArray[i].getElementAt(1)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(2)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(3)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(4)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(5)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(6)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(7)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(8)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(9)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(10)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(11)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(12)).setStyleName("titleOfCalculated ");
				((TextBox) calcRowArray[i].getElementAt(13)).setStyleName("titleOfCalculated ");
		}
	}
	
	private void refreshTitleRowList() {

		for (int i = 1; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				Record record = new Record();
				Time time = new Time(j - 1, OfficialInformation.CURRENT_YEAR);

				if (PartagedDataBetweenPanel.currentPerson != null) {
					switch (i) {
					case 1:
						record = PartagedDataBetweenPanel.currentPerson.getHoliday(time);
						break;
					case 2:
						record = PartagedDataBetweenPanel.currentPerson.getTraining(time);
						break;
					case 3:
						record = PartagedDataBetweenPanel.currentPerson.getOther(time);
						break;
					}
				}
				titlesRowArray[i].setElementAt(j, record.getNumber());
			}
			double sumOfTheTitleRow = round(titlesRowArray[i].sumRow(),2);
			titlesRowArray[i].setElementAt(1, sumOfTheTitleRow);
		}
	
	}
	
	private void refreshProjectRowList() {
		
		List<Assignment> assignmentList;

		projectRowList.clear();

		// Getting the assignment for the current person or project
		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			assignmentList = MiroState.getAssignments(PartagedDataBetweenPanel.currentPerson);
			break;
		case PROJECT_VIEW:
			assignmentList = MiroState.getAssignments(PartagedDataBetweenPanel.currentProject);
			break;
		default:
			assignmentList = new ArrayList<Assignment>();
		}

		// Browsing the assignment list to put in the ProjecRow list
		for (int i = 0; i < assignmentList.size(); i++) {
			Assignment assignment = assignmentList.get(i);
			String personOrProjectName = "";

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				personOrProjectName = assignment.getProject().getName();
				break;
			case PROJECT_VIEW:
				personOrProjectName = assignment.getPerson().getName();
				break;
			}
			ProjectRow projectRow = new ProjectRow(personOrProjectName);

			((TextBox) projectRow.getElementAt(0)).setStyleName("titleOfFirstColumnProject");
			((TextBox) projectRow.getElementAt(0)).addStyleName("textbox-nameOfProjectOrPerson");
			((TextBox) projectRow.getElementAt(0)).addClickHandler(new switchingViewListener(personOrProjectName));

			for (int j = 0; j < 12; j++) {
				Record record = assignment.getPrestation(j);
				int sizeProjectRowList = projectRowList.size();
				MyProjectListener projectListener = new MyProjectListener(sizeProjectRowList, j + 1, record.getNumber());
				TextBox textBox = (TextBox) projectRow.getElementAt(j + 2);
				textBox.addChangeHandler(projectListener);
				projectRow.setElementAt(j + 2, record.getNumber());
			}
			double sumOfTheProjectRow = round(projectRow.sumRow(),2);
			projectRow.setElementAt(1, sumOfTheProjectRow);
						
			projectRowList.add(projectRow);
			
			Collections.sort(projectRowList, new ProjectRow.OrderByTitle());
		}
	
	}
	
	private void refreshCalcRowList() {
		
		for (int i = 0; i < calcRowArray.length; i++) {
			for (int j = 2; j < calcRowArray[i].length(); j++) {
				switch (i) {
				case 0:
					manageProjectDaysAvailabilityRow(i, j);
					break;
				case 1:
					manageProjectTotalRow(i, j);
					break;
				}
				double sumOfTheCalcRow = round(calcRowArray[i].sumRow(),2);
				calcRowArray[i].setElementAt(1, sumOfTheCalcRow);
			}
		}
	
	}
	
	private void refreshCalcRowForProjectView() {
		
		double sumOfRow = round(0,2);

		((TextBox) calcRowForProjectView.getElementAt(0)).setStyleName("titleOfFirstColumn");

		for (int i = 2; i < 14; i++) {
			double sum = round(0,2);
			for (int j = 0; j < projectRowList.size(); j++) {
				TextBox txtBoxOfProjectRowList = (TextBox) (projectRowList.get(j).getElementAt(i));
				String txtOfTextBox = txtBoxOfProjectRowList.getText();
				double valueOfCell = round(Double.valueOf(txtOfTextBox),2);
				sum += valueOfCell;
			}
			sumOfRow += sum;
			calcRowForProjectView.setElementAt(i, sum);
		}
		calcRowForProjectView.setElementAt(1, sumOfRow);
	
	}

	private void refreshStyleOfSumColumn() {

		((TextBox) monthRow.getElementAt(0)).addStyleName("columnOfRowsSum0");
		((TextBox) monthRow.getElementAt(1)).addStyleName("columnOfRowsSum0");

		for (int i = 1; i < 14; i++) {
		((TextBox) titlesRowArray[0].getElementAt(i)).addStyleName("columnOfRowsSum1");
		}
		
		for (int i = 1; i < titlesRowArray.length; i++) {
			((TextBox) titlesRowArray[i].getElementAt(1)).addStyleName("columnOfRowsSum2");
		}

		for (int i = 0; i < calcRowArray.length; i++) {
			((TextBox) calcRowArray[i].getElementAt(1)).addStyleName("columnOfRowsSum3");
		}

		for (int i = 0; i < projectRowList.size(); i++) {
			((TextBox) projectRowList.get(i).getElementAt(1)).addStyleName("columnOfRowsSum4");
		}

		((TextBox) calcRowForProjectView.getElementAt(1)).addStyleName("columnOfRowsSum2");
	}
	
	private void refreshArray() {
		
		clearArray();
		addRowToArray(RowType.MONTHROW);

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			addRowToArray(RowType.TITLEROW);
			addRowToArray(RowType.CALCROW);
			addRowToArray(RowType.PROJECTROW);
			if (PartagedDataBetweenPanel.currentPerson == null) {
				disableAllTitleRow();
			} else {
				enableAllTitleRow();
			}
			break;
		case PROJECT_VIEW:
			addRowToArray(RowType.PROJECTROW);
			addRowToArray(RowType.CALCROW);
			break;
		}

		disableColumnsOfPreviousMonth();

		if (PartagedDataBetweenPanel.isReadOnly || 
			PartagedDataBetweenPanel.isImporting || 
			PartagedDataBetweenPanel.isSaving) {
			disableAllTitleRow();
			disableAllProjectRow();
		}
	}

	private void addRowToArray(RowType rowType) {
		
		TextBox textbox;

		switch (rowType) {

		case MONTHROW:
			for (int i = 0; i < monthRow.length(); i++) {
				textbox = (TextBox) monthRow.getElementAt(i);
				personArray.setWidget(0, i, textbox);
			}
			break;
		case TITLEROW:
			for (int i = 0; i < titlesRowArray.length; i++) {
				for (int j = 0; j < titlesRowArray[i].length(); j++) {
					textbox = (TextBox) titlesRowArray[i].getElementAt(j);
					personArray.setWidget(i + 1, j, textbox);
				}
			}
			break;
		case CALCROW:
			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				for (int j = 0; j < calcRowArray[0].length(); j++) {
					textbox = (TextBox) calcRowArray[0].getElementAt(j);
					personArray.setWidget(6, j, textbox);
				}

				for (int i = 1; i < calcRowArray.length; i++) {
					for (int j = 0; j < calcRowArray[i].length(); j++) {
						textbox = (TextBox) calcRowArray[i].getElementAt(j);
						personArray.setWidget(i + 7, j, textbox);
					}
				}
				break;
			case PROJECT_VIEW:
				for (int i = 0; i < calcRowForProjectView.length(); i++) {
					textbox = (TextBox) calcRowForProjectView.getElementAt(i);
					personArray.setWidget(startIndex + projectRowList.size(), i, textbox);
				}
				break;
			}
			break;
		case PROJECTROW:
			personArray.insertRow(startIndex);
			
			Collections.sort(projectRowList, new ProjectRow.OrderByTitle());
			
//			for (int i = 0; i < (projectRowList.size()); i++) {
//				Window.alert(projectRowList.get(i).getTitle());
//			}
						
			for (int i = startIndex; i < (startIndex + projectRowList.size()); i++) {
				personArray.insertRow(i);

				ProjectRow projectRowFromList = projectRowList.get(i - startIndex);

				for (int j = 0; j < projectRowFromList.length(); j++) {
					TextBox elementFromProjectRow = (TextBox) projectRowFromList.getElementAt(j);
					personArray.setWidget(i, j, elementFromProjectRow);
				}
			}
			break;
		}
	}

	private void disableColumnsOfPreviousMonth() {
		
		for (int i = 1; i < titlesRowArray.length; i++) {
			for (int j = 2; j < currentMonth; j++) {
				((TextBox) (titlesRowArray[i].getElementAt(j))).setReadOnly(true);
			}
		}

		for (int i = 0; i < projectRowList.size(); i++) {
			for (int j = 2; j < currentMonth; j++) {
				((TextBox) (projectRowList.get(i).getElementAt(j))).setReadOnly(true);
			}
		}

		for (int i = 0; i < calcRowArray.length; i++) {
			for (int j = 2; j < currentMonth; j++) {
				((TextBox) (calcRowArray[i].getElementAt(j))).setReadOnly(true);
			}
		}
		
	}
	
	private void disableAllTitleRow() {

		for (int i = 1; i < titlesRowArray.length; i++) {
			for (int j = 1; j < titlesRowArray[i].length(); j++) {
				((TextBox) titlesRowArray[i].getElementAt(j)).setReadOnly(true);
			}
		}
	
	}

	private void enableAllTitleRow() {

		for (int i = 1; i < titlesRowArray.length; i++) {
			for (int j = 2; j < titlesRowArray[i].length(); j++) {
				((TextBox) titlesRowArray[i].getElementAt(j)).setReadOnly(false);
			}
		}
	
	}

	private void manageProjectDaysAvailabilityRow(int i, int j) {
	
		String numberDaysForMonthTxt = ((TextBox) (titlesRowArray[0].getElementAt(j))).getText();
		String numberHolidaysForMonthTxt = ((TextBox) (titlesRowArray[1].getElementAt(j))).getText();
		String numberTrainingForMonthTxt = ((TextBox) (titlesRowArray[2].getElementAt(j))).getText();
		String numberOutProjectActivitiesForMonthTxt = ((TextBox) (titlesRowArray[3].getElementAt(j))).getText();

		double numberDaysForMonth = round(Double.valueOf(numberDaysForMonthTxt),2);
		double numberHolidaysForMonth = round(Double.valueOf(numberHolidaysForMonthTxt),2);
		double numberTrainingForMonth = round(Double.valueOf(numberTrainingForMonthTxt),2);
		double numberOutProjectActivitiesForMonth = round(Double.valueOf(numberOutProjectActivitiesForMonthTxt),2);

		double numberAvailabilityProjectDays = round(numberDaysForMonth - 
											   numberHolidaysForMonth - 
											   numberTrainingForMonth - 
											   numberOutProjectActivitiesForMonth,2);
		calcRowArray[i].setElementAt(j, numberAvailabilityProjectDays);	
	}

	private void manageProjectTotalRow(int i, int j) {
	
		double sum = round(0,2);

		for (int index = 0; index < projectRowList.size(); index++) {
			String prestationNumberForTheProject = ((TextBox) projectRowList.get(index).getElementAt(j)).getText();
			double valueOfPrestation = round(Double.valueOf(prestationNumberForTheProject),2);
			sum += valueOfPrestation;
		}
		calcRowArray[i].setElementAt(j, sum);
	
	}

	private void disableAllProjectRow() {
		for (int i = 0; i < projectRowList.size(); i++) {
			for (int j = 1; j < projectRowList.get(i).length(); j++) {
				((TextBox) projectRowList.get(i).getElementAt(j))
						.setReadOnly(true);
			}
		}
	}

	private void clearArray() {

		while (personArray.getRowCount() != 0) {
			personArray.removeRow(0);
		}
		
	}

	// All events listeners  --------------------------------------------------------------------
	
	@Override
	public void notifyChange(Widget widget) {
		
		refreshCenterPanel();
	}

	private void notifyListeners() {
		
		for (EventListener eventListener : eventListenerList) {
			eventListener.notifyChange(this);
		}
		
	}

	private class MyTitleListener implements ChangeHandler {

		int row;
		int column;
		double value = round(0,2);

		public MyTitleListener(int row, int column, double oldValue) {			
			this.row = row;
			this.column = column;
			value = round(oldValue,2);
		}

		public void onChange(ChangeEvent changeEvent) {
			
			TextBox textbox = (TextBox) changeEvent.getSource();
			double valueOfTextBox = round(Double.valueOf(textbox.getText()),2);

			if (row > 0 && row < 5) {
				if (valueOfTextBox < 0) {
					Window.alert("Valeur negative interdite !");
					textbox.setText("" + value);
				} else {
					value = valueOfTextBox;
					manageTitleRow();
					refreshTitleRowList();
					refreshCalcRowList();
					refreshStyleOfSumColumn();
				}
			}	
		}

		private void manageTitleRow() {
			
			int monthNumber = column - 1;
			Time time = new Time(monthNumber, OfficialInformation.CURRENT_YEAR);
			
			switch (row) {
				case 2:{
					PartagedDataBetweenPanel.currentPerson.getHoliday(time).setNumber(value);
					String activity = "Congés & Absences";
					String mission = "TTR GÉNÉRIQUE";
					
					final AsyncCallback<Allocation> PUT_PERSON_ALLOCATION_CALLBACK = new AsyncCallback<Allocation>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Impossible de mettre à jour!");
						}
	
						@Override
						public void onSuccess(Allocation result) {	
						};
					};		
	//				Window.alert(PartagedDataBetweenPanel.currentPerson.getName()+" "+monthNumber+" "+value);
					MiroAccessDB.putPersonAllocation(PartagedDataBetweenPanel.currentPerson.getName(),
													 mission,
													 activity,
													 monthNumber,
													 value,
													 PUT_PERSON_ALLOCATION_CALLBACK);
					break;
				}
				case 3:{
					PartagedDataBetweenPanel.currentPerson.getTraining(time).setNumber(value);
					String activity = "Formations";
					String mission = "TTR GÉNÉRIQUE";
					
					final AsyncCallback<Allocation> PUT_PERSON_ALLOCATION_CALLBACK = new AsyncCallback<Allocation>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Impossible de mettre à jour!");
						}
	
						@Override
						public void onSuccess(Allocation result) {	
						};
					};		
	//				Window.alert(PartagedDataBetweenPanel.currentPerson.getName()+" "+monthNumber+" "+value);
					MiroAccessDB.putPersonAllocation(PartagedDataBetweenPanel.currentPerson.getName(),
													 mission,
													 activity,
													 monthNumber,
													 value,
													 PUT_PERSON_ALLOCATION_CALLBACK);
					break;
				}
				case 4:{
					PartagedDataBetweenPanel.currentPerson.getOther(time).setNumber(value);
					String activity = "Activités Hors Projets";
					String mission = "TTR GÉNÉRIQUE";
					
					final AsyncCallback<Allocation> PUT_PERSON_ALLOCATION_CALLBACK = new AsyncCallback<Allocation>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Impossible de mettre à jour!");
						}
	
						@Override
						public void onSuccess(Allocation result) {	
						};
					};		
	//				Window.alert(PartagedDataBetweenPanel.currentPerson.getName()+" "+monthNumber+" "+value);
					MiroAccessDB.putPersonAllocation(PartagedDataBetweenPanel.currentPerson.getName(),
													 mission,
													 activity,
													 monthNumber,
													 value,
													 PUT_PERSON_ALLOCATION_CALLBACK);				
					break;
				}
			}	
		}		
	}
	
	private class MyProjectListener implements ChangeHandler {
		
		int rowNumber = 0;
		int columnNumber = 0;
		double oldValue = round(0,2);

		public MyProjectListener(int rowNumber, int columnNumber, double oldValue) {	
			this.rowNumber = rowNumber;
			this.columnNumber = columnNumber;
			this.oldValue = round(oldValue,2);
		}

		public void onChange(ChangeEvent changeEvent) {
			
			TextBox textBoxOfEvent = (TextBox) (changeEvent.getSource());
			String valueOfTextBox = textBoxOfEvent.getText();
			
			double value = round(Double.valueOf(valueOfTextBox),2);

			if (value < 0) {
				Window.alert("Valeur negative interdite !");
				textBoxOfEvent.setText("" + oldValue);
			} else {
				oldValue = value;
				treatmentOfProject();
			}		
		}

		private void treatmentOfProject() {
			
			Time time = new Time(columnNumber, OfficialInformation.CURRENT_YEAR);
			int month = time.getMonth();
			Record record = new Record(oldValue, time);
			Assignment assignment = null;

			ProjectRow projectRow = projectRowList.get(rowNumber);
			String titleOfRow = projectRow.getTitle();

			switch (PartagedDataBetweenPanel.viewType) {
				case PERSON_VIEW:{
					assignment = MiroState.getAssignment(PartagedDataBetweenPanel.currentPerson, new Project(titleOfRow));
					assignment.setPrestation(columnNumber - 1, record);
					refreshCalcRowList();
					refreshStyleOfSumColumn();
					
					String activity = "Projets";
					String mission = titleOfRow;
					
					final AsyncCallback<Allocation> PUT_PERSON_ALLOCATION_CALLBACK = new AsyncCallback<Allocation>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Impossible de mettre à jour!");
						}
	
						@Override
						public void onSuccess(Allocation result) {	
						};
					};		
	//				Window.alert(PartagedDataBetweenPanel.currentPerson.getName()+" "+mission+" "+month+" "+oldValue);
					MiroAccessDB.putPersonAllocation(PartagedDataBetweenPanel.currentPerson.getName(),
													 mission,
													 activity,
													 month,
													 oldValue,
													 PUT_PERSON_ALLOCATION_CALLBACK);				
					break;
				}
				case PROJECT_VIEW:{
					String personName = titleOfRow;
					assignment = MiroState.getAssignment(new Person(personName), PartagedDataBetweenPanel.currentProject);
					assignment.setPrestation(columnNumber - 1, record);
					refreshCalcRowForProjectView();
					refreshStyleOfSumColumn();
					
					String activity = "Projets";
					String mission = PartagedDataBetweenPanel.currentProject.getName();
					
					final AsyncCallback<Allocation> PUT_PERSON_ALLOCATION_CALLBACK = new AsyncCallback<Allocation>() {
	
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Impossible de mettre à jour!");
						}
	
						@Override
						public void onSuccess(Allocation result) {	
						};
					};		
	//				Window.alert(personName+" "+mission+" "+month+" "+oldValue);
					MiroAccessDB.putPersonAllocation(personName,
													 mission,
													 activity,
													 month,
													 oldValue,
													 PUT_PERSON_ALLOCATION_CALLBACK);				
					
					break;
				}
			}
			double sumOfTheProjectRow = round(projectRow.sumRow(),2);
			projectRow.setElementAt(1, sumOfTheProjectRow);
		}
	}

	private class switchingViewListener implements ClickHandler {
		
		String nameOfPersonOrProject;

		public switchingViewListener(String nameOfPersonOrProject) {
			
			this.nameOfPersonOrProject = nameOfPersonOrProject;
		}

		@Override
		public void onClick(ClickEvent event) {
				
			PartagedDataBetweenPanel.hasChangedView = true;

			switch (PartagedDataBetweenPanel.viewType) {
			case PERSON_VIEW:
				PartagedDataBetweenPanel.currentPerson = null;
				PartagedDataBetweenPanel.currentProject = MiroState.getProject(nameOfPersonOrProject);
				PartagedDataBetweenPanel.viewType = ViewType.PROJECT_VIEW;
				break;
			case PROJECT_VIEW:
				PartagedDataBetweenPanel.currentProject = null;
				PartagedDataBetweenPanel.currentPerson = MiroState.getPerson(nameOfPersonOrProject);
				PartagedDataBetweenPanel.viewType = ViewType.PERSON_VIEW;
				break;
			}
			refreshCenterPanel();
			notifyListeners();		
		}
	}
	
    private double round(double what, int howmuch) {
    	return (double)( (int)(what * Math.pow(10,howmuch) + .5) ) / Math.pow(10,howmuch);
    }
}