package miro.client.view;

import miro.shared.Person;
import miro.shared.Project;

public class PartagedDataBetweenPanel {

	static boolean hasChangedView = true;
	static boolean isImporting = false;
	static boolean isReadOnly = false;
	static boolean isSaving = false;
	static boolean isExcel = false;
	static ViewType viewType = ViewType.PERSON_VIEW;

	static Person currentPerson = null;
	static Project currentProject = null;
	static String currentUser = "";
}
