package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.Person;
import miro.shared.Project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TopPanel extends Composite implements EventListener {

	interface TopPanelUiBinder extends UiBinder<AbsolutePanel, TopPanel> {}
	static TopPanelUiBinder ourUiBinder = GWT.create(TopPanelUiBinder.class);
	
	@UiField
	Image cirbImage;

	@UiField
	Label labelSelectPersonOrProject;

	@UiField
	ListBox personOrProjectListBox;

	@UiField
	FormPanel formPanel;

	@UiField
	AbsolutePanel absolutePanel;

	private List<Person> personList;
	private List<Project> projectList;

	private Button importButton = new Button("IMPORT");
	
	@UiField
	Image excelImage;
	
	@UiField
	Image csvImage;
	
	private static List<EventListener> eventListenerList = new ArrayList<EventListener>();

	public TopPanel() {

		initWidget(ourUiBinder.createAndBindUi(this));
		initTopPanel();
		CenterPanel.addEventListener(this);	
	}

	private void initTopPanel() {
		
		initAbsolutePanel();

		boolean isReadOnly = PartagedDataBetweenPanel.isReadOnly;

		formPanel.setVisible(!isReadOnly);
		if (PartagedDataBetweenPanel.currentUser.equals("PROJECT")) {
			formPanel.setVisible(false);
			csvImage.setVisible(false);
		}

		refreshPersonOrProjectChoiceList();
		initListeners();	
	}

	public static void addEventListener(EventListener eventListener) {
		
		eventListenerList.add(eventListener);
	}

	private void notifyListeners() {
		
		for (EventListener eventListener : eventListenerList) {
			eventListener.notifyChange(this);
		}
	}

	private void initAbsolutePanel() {
		
		absolutePanel.setSize("1280px", "96px");
		absolutePanel.setWidgetPosition(csvImage, 0, 15);
		absolutePanel.setWidgetPosition(labelSelectPersonOrProject, 345, 15);
		absolutePanel.setWidgetPosition(personOrProjectListBox, 450, 15);
		absolutePanel.setWidgetPosition(formPanel, 800, 15);
		absolutePanel.setWidgetPosition(excelImage, 1200, 20);
		absolutePanel.setWidgetPosition(csvImage, 1240, 15);
		
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("uploadFormElement");

		excelImage.setVisible(false);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(fileUpload);
		importButton.setEnabled(true);
		horizontalPanel.add(importButton);

		formPanel.setAction(GWT.getModuleBaseURL() + "SampleUploadServlet");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setWidget(horizontalPanel);
	}

	private void refreshLabPersonOrProjectChoice() {

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			labelSelectPersonOrProject.setText("Personne ? : ");
			break;
		case PROJECT_VIEW:
			labelSelectPersonOrProject.setText("Projet ? : ");
			break;
		}
	}

	private void setSelectedItemFromPersonOrProjectChoiceList() {
		
		String txt = "";
		excelImage.setVisible(false);

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			if (PartagedDataBetweenPanel.currentPerson != null) {
				String lastName = PartagedDataBetweenPanel.currentPerson.getName();
				txt = lastName;
				excelImage.setVisible(true);
			}
			break;
		case PROJECT_VIEW:
			if (PartagedDataBetweenPanel.currentProject != null) {
				txt = PartagedDataBetweenPanel.currentProject.getName();
				excelImage.setVisible(true);

			}
			break;
		}
		
		boolean isFound = false;
		int index = 1;
		while (!isFound && index < personOrProjectListBox.getItemCount()) {
			isFound = personOrProjectListBox.getItemText(index).equals(txt);
			index++;
		}
		if (isFound)
			personOrProjectListBox.setSelectedIndex(index - 1);
	}

	private void initListeners() {
		
		personOrProjectListBox.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent changeEvent) {
				
				int selectedIndexPersonChoiceList = personOrProjectListBox.getSelectedIndex();

				if (selectedIndexPersonChoiceList != 0) {
					excelImage.setVisible(true);
					switch (PartagedDataBetweenPanel.viewType) {
					case PERSON_VIEW:
						PartagedDataBetweenPanel.currentPerson = personList.get(selectedIndexPersonChoiceList - 1);
						break;
					case PROJECT_VIEW:
						PartagedDataBetweenPanel.currentProject = projectList.get(selectedIndexPersonChoiceList - 1);
						break;
					}
				} else {
					excelImage.setVisible(false);
					switch (PartagedDataBetweenPanel.viewType) {
					case PERSON_VIEW:
						PartagedDataBetweenPanel.currentPerson = null;
						break;
					case PROJECT_VIEW:
						PartagedDataBetweenPanel.currentProject = null;
						break;
					}
				}
				notifyListeners();
			}
		});

		importButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PartagedDataBetweenPanel.isImporting = true;
				formPanel.submit();
				notifyListeners();
			}
		});
		
		excelImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(excelImage.isVisible())
				{
					switch (PartagedDataBetweenPanel.viewType) {
					case PERSON_VIEW:
//						Window.alert(PartagedDataBetweenPanel.currentPerson.getName());
						Window.Location.replace(GWT.getModuleBaseURL()+"export?name="+PartagedDataBetweenPanel.currentPerson.getName());
						break;
					case PROJECT_VIEW:
//						Window.alert(PartagedDataBetweenPanel.currentProject.getName());
						Window.Location.replace(GWT.getModuleBaseURL()+"export?name="+PartagedDataBetweenPanel.currentProject.getName());
						break;
					}
				}	    
			}
		});
		
		csvImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(csvImage.isVisible())
				Window.Location.replace(GWT.getModuleBaseURL()+"export?name=fullDB");
			}
		});

		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				PartagedDataBetweenPanel.isImporting = false;

				final AsyncCallback<List<Allocation>> GET_ALLOCATIONS_CALLBACK = new AsyncCallback<List<Allocation>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Impossible d'obtenir les assignements ! ");
					}

					@Override
					public void onSuccess(List<Allocation> result) {
						
						List<Assignment> initialAssignmentList = MiroState.builAssignmentList(result);
						List<Person> initialPersonList = MiroState.buildPersonList(result);
						List<Assignment> finalAssignmentList = MiroState.builFinalAssignmentList(initialPersonList,initialAssignmentList);
						MiroState.updateViewState(finalAssignmentList);
						refreshPersonOrProjectChoiceList();
						setSelectedItemFromPersonOrProjectChoiceList();
						notifyListeners();
						Window.alert("Mise à jour et rafraîchissement effectués");
					}
				};
				MiroAccessDB.getAllocations(GET_ALLOCATIONS_CALLBACK);
			}
		});
	}

	private void refreshPersonOrProjectChoiceList() {
		
		personOrProjectListBox.clear();
		personOrProjectListBox.addItem("Selectionnez");

		switch (PartagedDataBetweenPanel.viewType) {
		case PERSON_VIEW:
			personList = MiroState.getPersonList();

			for (Person person : personList) {
				String NameOfPerson = person.getName();;
				String item = NameOfPerson;
				personOrProjectListBox.addItem(item);
			}
			break;
		case PROJECT_VIEW:
			projectList = MiroState.getProjectList();

			for (Project project : projectList) {
				String nameOfProject = project.getName();
				personOrProjectListBox.addItem(nameOfProject);
			}
			break;
		}
	}

	@Override
	public void notifyChange(Widget widget) {

		if (PartagedDataBetweenPanel.isReadOnly) {
			formPanel.setVisible(false);
		} 
		refreshLabPersonOrProjectChoice();
		refreshPersonOrProjectChoiceList();
		setSelectedItemFromPersonOrProjectChoiceList();
	}
}