package miro.client;

import miro.client.view.MiroView;

/**
 * This class represents the entry point of the application
 */
public class MiroV2 {

	private MiroView miroView;

	/**
	 * Entry point of the application
	 */
	public void onModuleLoad() {
		miroView = new MiroView();
	}
}
