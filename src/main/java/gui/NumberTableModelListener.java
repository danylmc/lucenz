package gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * This class is a table model lister class that only allows numbers to
 * be entered into the cell. When strings which can't be converted into doubles
 * are entered the cell is set back to blank to indicate what was entered in invalid.
 *
 */
public class NumberTableModelListener implements TableModelListener{

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int col = e.getColumn();
		TableModel model = (TableModel)e.getSource();
		String s = (String) model.getValueAt(row, col);
		try {
			double d = Double.parseDouble(s);
		}
		catch (NumberFormatException err) {
			if(s.equals("")) {
				return;
			}
			model.setValueAt("", row, col);
		}
	}

}
