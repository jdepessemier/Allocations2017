package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

public class TitleRow extends Row {

	private final NumberFormat NUMBER_FORMAT_FOR_TITLEROW = NumberFormat.getFormat("0.00");
	private boolean isEnabledAllCells;

	public TitleRow(String titleOfRow, boolean isEnabled) {
		super(titleOfRow);

		initArray();
		disabledFirstCells();

		isEnabledAllCells = isEnabled;

		if (!isEnabledAllCells)
			disabledAllCells();
	}

	private void disabledFirstCells() {
		arrayForARow[0].setEnabled(false);

		arrayForARow[1].setReadOnly(true);
	}

	private void disabledAllCells() {
		for (int i = 1; i < arrayForARow.length; i++) {
			arrayForARow[i].setReadOnly(true);
		}
	}

	private void initArray() {

		for (int i = 1; i < arrayForARow.length; i++) {
			setElementAt(i, 0);
		}
	}

	public boolean setElementAt(int index, double value) {
		boolean isModified = index > 0 && index < arrayForARow.length
				&& value >= 0;
		String stringFormatted = NUMBER_FORMAT_FOR_TITLEROW.format(value);

		if (isModified) {
			arrayForARow[index].setText(stringFormatted);
		}
		return isModified;
	}

	public double sumRow() {
		double sumOfRow = 0;

		for (int i = 2; i < arrayForARow.length; i++) {
			String txtNumberOfACell = arrayForARow[i].getText();
			double numberOfACell = Double.valueOf(txtNumberOfACell);

			sumOfRow += numberOfACell;
		}
		String stringFormatted = NUMBER_FORMAT_FOR_TITLEROW.format(sumOfRow);

		return Double.valueOf(stringFormatted);
	}
}