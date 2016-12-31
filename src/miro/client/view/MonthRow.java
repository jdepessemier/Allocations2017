package miro.client.view;

import com.google.gwt.user.client.ui.TextBox;

public class MonthRow extends Row {
	
	private int currentYear;

	public MonthRow(int year) {
		super();
		this.currentYear = year;
		initArray();
		disableCells();
	}

	private void initArray() {

		for (int index = 0; index < arrayForARow.length; index++) {
			arrayForARow[index] = new TextBox();
		}

		arrayForARow[1].setText("" + currentYear);
		arrayForARow[2].setText("Janvier");
		arrayForARow[3].setText("Fevrier");
		arrayForARow[4].setText("Mars");
		arrayForARow[5].setText("Avril");
		arrayForARow[6].setText("Mai");
		arrayForARow[7].setText("Juin");
		arrayForARow[8].setText("Juillet");
		arrayForARow[9].setText("Aout");
		arrayForARow[10].setText("Septembre");
		arrayForARow[11].setText("Octobre");
		arrayForARow[12].setText("Novembre");
		arrayForARow[13].setText("Decembre");
	}

	private void disableCells() {
		for (int index = 0; index < arrayForARow.length; index++) {
			arrayForARow[index].setEnabled(false);
		}
	}
}