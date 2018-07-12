package org.etri.slice.tools.jmxconsole.swt.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("unchecked")
public abstract class AbstractTableLabelProvider<T> extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return columnImage((T) element, columnIndex);
	}
	
	protected abstract Image columnImage(T element, int columnIndex);

	@Override
	public String getColumnText(Object element, int columnIndex) {
		return columnText((T) element, columnIndex);
	}
	
	protected abstract String columnText(T element, int columnIndex);

}
