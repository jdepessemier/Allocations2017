package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

public class ProjectRow extends Row {
	
	private final NumberFormat NUMBER_FORMAT_FOR_PROJECT = NumberFormat.getFormat("0.##");

	public ProjectRow(String title) {
		super(title);

		initArray();
		disabledCells();
	}

	private void initArray() {
		for (int i = 1; i < arrayForARow.length; i++) {
			setElementAt(i, 0);
		}
	}

	private void disabledCells() {
		arrayForARow[0].setEnabled(true);
		arrayForARow[1].setReadOnly(true);
	}

	public boolean setElementAt(int column, double value) {
		boolean isModified = column > 0 && column < arrayForARow.length;
		String stringFormatted = NUMBER_FORMAT_FOR_PROJECT.format(value);

		if (isModified) {
			arrayForARow[column].setText(stringFormatted);
		}

		return isModified;
	}

	public double sumRow() {
		double sumOfRow = 0;

		for (int i = 2; i < arrayForARow.length; i++) {
			String txtNumberOfACell = arrayForARow[i].getText();
			double valueOfACell = Double.valueOf(txtNumberOfACell);
			sumOfRow += valueOfACell;
		}
		String stringFormatted = NUMBER_FORMAT_FOR_PROJECT.format(sumOfRow);
		return Double.valueOf(stringFormatted);
	}
}
