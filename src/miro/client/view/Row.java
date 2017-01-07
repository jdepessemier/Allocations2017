package miro.client.view;

import com.google.gwt.user.client.ui.TextBox;

public class Row {
	private String title;
	private final int SIZE_OF_ARRAY = 14;
	protected TextBox[] arrayForARow;
	
//    public static class OrderByTitle implements Comparator<Row> {
//
//        @Override
//        public int compare(Row o1, Row o2) {
//            return o1.title.compareTo(o2.title);
//        }
//    }

	public Row() {
		arrayForARow = new TextBox[SIZE_OF_ARRAY];
		title = "";
		initArray();
	}

	public Row(String titleOfRow) {
		arrayForARow = new TextBox[SIZE_OF_ARRAY];
		title = titleOfRow;
		initArray();
	}

	private void initArray() {

		for (int i = 0; i < arrayForARow.length; i++) {
			arrayForARow[i] = new TextBox();
		}
		arrayForARow[0].setText(title);
	}

	public Object getElementAt(int column) {
		if (column < 0 || column >= arrayForARow.length)
			return null;
		return arrayForARow[column];
	}

	public boolean setElementAt(int column, String value) {
		boolean isModified = column > 0 && column < arrayForARow.length;

		if (isModified) {
			arrayForARow[column].setText(value);
		}
		return isModified;
		
	}

	public int length() {
		return arrayForARow.length;
	}

	public void setTitle(String newTitle) {
		this.title = newTitle;
		arrayForARow[0].setText(newTitle);
	}

	public String getTitle() {
		return title;
	}
	
	public int compareTo(Object arg0) {

		if (arg0 instanceof Row) {
			Row row = (Row) arg0;
			int compare = title.compareTo(row.title);

			if (compare > 0)
				return 1;
			if (compare < 0)
				return -1;
			return 0;
		}
		return 1;
	}
	
}
