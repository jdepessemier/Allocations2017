package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

public class CalcRow extends Row {

	private final NumberFormat NUMBER_FORMAT_IN_POURCENT = NumberFormat
	.getFormat("0.00");
	private boolean printPercentageSymbol;

	public CalcRow(String title, boolean printPercentageSymbol) {
		super(title);

		this.printPercentageSymbol = printPercentageSymbol;

		initArray();
		disabledCells();
	}

	private void initArray() {

		for (int i = 1; i < arrayForARow.length; i++) {
			setElementAt(i, 0);
		}
	}

	private void disabledCells() {
		arrayForARow[0].setEnabled(false);

		for (int i = 1; i < arrayForARow.length; i++) {
			arrayForARow[i].setReadOnly(true);
		}
	}

	public boolean setElementAt(int column, double value) {
		boolean isModified = column > 0 && column < arrayForARow.length;
		String stringFormatted = NUMBER_FORMAT_IN_POURCENT.format(value);

		if (isModified) {
			if (printPercentageSymbol)
				stringFormatted += "%";

			arrayForARow[column].setText(stringFormatted);
		}
		return isModified;
	}

	public double sumRow() {
		double sumOfRow = 0;

		for (int i = 2; i < arrayForARow.length; i++) {
			String txtNumberOfACell = arrayForARow[i].getText();
			txtNumberOfACell = txtNumberOfACell.substring(0, txtNumberOfACell.length() - 1);

			double valueOfTheCell = Double.valueOf(txtNumberOfACell);
			sumOfRow += valueOfTheCell;
		}

		if (printPercentageSymbol)
			sumOfRow /= 12;

		return sumOfRow;
	}
}