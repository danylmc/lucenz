package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for to use when drawing a cell in any of the tables in the input
 * pane. Main purpose is so that uneditable cells are grayed out.
 */
@SuppressWarnings("serial")
public class CellRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Call default renderer first so cells actually draw
		DefaultTableCellRenderer c = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Highlight clicked cells
		if(((InputTableModel) table.getModel()).isCellClicked(row, column)) {
			this.setBackground(Color.YELLOW);
			this.setForeground(Color.BLACK);
		}
		// Standard editable cell
		else if(table.getModel().isCellEditable(row, column)) {
			this.setBackground(Color.WHITE);
			this.setForeground(Color.BLACK);
		}
		// Uneditable cell
		else {
			this.setBackground(Color.LIGHT_GRAY);
			this.setForeground(Color.GRAY);
		}

		return c;
	}
}