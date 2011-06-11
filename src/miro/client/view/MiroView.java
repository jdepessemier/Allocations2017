package miro.client.view;

import java.util.List;

import miro.client.db.MiroAccessDB;
import miro.shared.Allocation;
import miro.shared.Assignment;
import miro.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents the view throwing when the application starts up.It
 * contains an image and the authentication box
 */

public class MiroView extends Composite implements EventListener {
	
	PersonView personView;

	interface GlobalResources extends ClientBundle {
		@CssResource.NotStrict
		@Source("style.css")
		CssResource css();
	}

	private static MiroViewUiBinder uiBinder = GWT.create(MiroViewUiBinder.class);
	interface MiroViewUiBinder extends UiBinder<DockLayoutPanel, MiroView> {}
	DockLayoutPanel rootElement = uiBinder.createAndBindUi(this);

	public MiroView() {

		RootLayoutPanel.get().add(rootElement);
		GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();
		AuthentificationBox.addEventListener(this);
	}

	@Override
	public void notifyChange(Widget widget) {

		final AsyncCallback<List<Allocation>> GET_ALLOCATIONS_CALLBACK = new AsyncCallback<List<Allocation>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Impossible d'obtenir la liste des allocations!");
			}

			@Override
			public void onSuccess(List<Allocation> result) {
							
				List<Assignment> initialAssignmentList = MiroState.builAssignmentList(result);
				List<Person> initialPersonList = MiroState.buildPersonList(result);
				List<Assignment> finalAssignmentList = MiroState.builFinalAssignmentList(initialPersonList,initialAssignmentList);
				MiroState.updateViewState(finalAssignmentList);

				RootLayoutPanel.get().remove(rootElement);
				personView = new PersonView();
			}
		};		
		MiroAccessDB.getAllocations(GET_ALLOCATIONS_CALLBACK);
	}

}
