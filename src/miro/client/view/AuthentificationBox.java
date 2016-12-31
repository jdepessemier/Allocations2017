package miro.client.view;

import java.util.ArrayList;
import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Connection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the authentication box of Miro Application
 */

public class AuthentificationBox extends Composite {

	private static AuthentificationBoxUiBinder uiBinder = GWT.create(AuthentificationBoxUiBinder.class);

	interface AuthentificationBoxUiBinder extends UiBinder<Widget, AuthentificationBox> {}

	@UiField
	DialogBox dialogBox;

	private Grid componentsGrid = new Grid(3, 3);

	private Label labUsername = new Label("Nom d'utilisateur : ");
	private Label labPassword = new Label("Mot de passe : ");

	private ListBox usernameChoiceList = new ListBox();
	private PasswordTextBox passwordField = new PasswordTextBox();

	private Button validateBtn = new Button("Valider");

	private static List<EventListener> listenersList = new ArrayList<EventListener>();
	private List<Connection> connectionList;

	/**
	 * Defines an AuthentificationBox
	 */
	
	public AuthentificationBox() {
		initWidget(uiBinder.createAndBindUi(this));
		initAuthentificationBox();
	}

	public static void addEventListener(EventListener eventListener) {
		listenersList.add(eventListener);
	}

	private void notifyListeners() {
		for (EventListener eventListener : listenersList) {
			eventListener.notifyChange(this);
		}
	}

	private void initUsernameList() {
		final AsyncCallback<List<Connection>> GET_CONNECTIONS_CALLBACK = new AsyncCallback<List<Connection>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(List<Connection> result) {
				connectionList = result;

				for (Connection connection : connectionList) {
					String lastName = connection.getLastName();
					String firstName = connection.getFirstName();
					usernameChoiceList.addItem(lastName + " " + firstName);
				}
			}
		};
		
		MiroAccessDB.getConnections(GET_CONNECTIONS_CALLBACK);
	}

	private void initAuthentificationBox() {
		initComponentsGrid();
		initComponents();
		initListeners();
		dialogBox.setWidget(componentsGrid);
	}

	private void initComponentsGrid() {
		
		componentsGrid.setWidget(0, 0, labUsername);
		componentsGrid.setWidget(0, 1, usernameChoiceList);
		componentsGrid.setWidget(1, 0, labPassword);
		componentsGrid.setWidget(1, 1, passwordField);
		componentsGrid.setWidget(2, 0, validateBtn);
	}

	private void initComponents() {

		usernameChoiceList.addItem("SELECTIONNEZ");
		passwordField.setEnabled(false);
		validateBtn.setEnabled(false);
		dialogBox.setModal(true);

		initUsernameList();
	}

	private void initListeners() {
		
		usernameChoiceList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				boolean isFirstIndex = usernameChoiceList.getSelectedIndex() == 0;

				passwordField.setEnabled(!isFirstIndex);
				validateBtn.setEnabled(!isFirstIndex);
			}
		});

		validateBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				manageClickOnValidateButton();
			}
		});
			
		SubmitListener sl = new SubmitListener();
		passwordField.addKeyboardListener(sl);
	}

	private boolean isCorrectPassword(String lastName, String firstName) {
		int index = connectionList.indexOf(new Connection(lastName, firstName, ""));
		String pwd = passwordField.getText();

		return connectionList.get(index).getPwd().equals(pwd);
	}

	private void manageClickOnValidateButton() {
		// if an user request an anonymous connection,read-only access

			int selectedIndex = usernameChoiceList.getSelectedIndex();
			String lastAndFirstName = usernameChoiceList.getItemText(selectedIndex);

			int indexSpacing = lastAndFirstName.lastIndexOf(" ");
			String lastName = lastAndFirstName.substring(0, indexSpacing);
			String firstName = lastAndFirstName.substring(indexSpacing + 1);

			if (isCorrectPassword(lastName, firstName)) {
				PartagedDataBetweenPanel.currentUser = lastName;
				notifyListeners();
			} else {
				Window.alert("Mot de passe incorrect !");
			}
	}
	
	@SuppressWarnings("deprecation")
	private class SubmitListener extends KeyboardListenerAdapter {
	    public void onKeyPress(Widget sender, char key, int mods) {
	      if (KeyboardListener.KEY_ENTER == key)
	    	  validateBtn.click();
	    }
	  }
}
