package org.etri.slice.tools.jmxconsole.swt.provider;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableContentProvider implements IStructuredContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null) {
			if (inputElement instanceof List<?>) {
				final List<?> list_ = (List<?>) inputElement;
				return list_.toArray();
			} else if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			} else if (inputElement instanceof Map<?, ?>) {
				final Map<?, ?> map_ = (Map<?, ?>) inputElement;
				return map_.values().toArray();
			}
		}
		return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(final Viewer viewer, Object oldInput,
			Object newInput) {
	}
}
