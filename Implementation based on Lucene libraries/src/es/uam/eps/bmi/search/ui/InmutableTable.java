/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ui;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Javi
 */
public class InmutableTable extends JTable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that calls super (the constructor of JTable)
	 * 
	 * @param dm
	 *            the table model to initialize the JTable
	 */
	public InmutableTable(TableModel dm) {
		super(dm);
	}

    InmutableTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}

