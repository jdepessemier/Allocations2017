package miro.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class PersonView extends Composite {

	interface PersonViewUiBinder extends UiBinder<DockLayoutPanel, PersonView> {}
	static PersonViewUiBinder ourUiBinder = GWT.create(PersonViewUiBinder.class);
	DockLayoutPanel rootElement = ourUiBinder.createAndBindUi(this);

	public PersonView() {
		RootLayoutPanel.get().add(rootElement);
	}
}
