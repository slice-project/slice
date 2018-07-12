package org.etri.slice.tools.jmxconsole.swt.editor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class CompositeDataTableEditor extends CellEditor{
	TableViewer tableViewer;
	
	public CompositeDataTableEditor(Composite parent, int style)
	{
		super(parent, style);
	}

	public CompositeDataTableEditor() {}
	
	@Override
	protected Control createControl(Composite parent) {		
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setSize(700, 500);
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		
		Composite tabularDataNavComposite = createTabularDataNavigationButtons(composite);
		Composite compositeDataNavComposite = createCompositeDataNavigationButtons(composite);
		tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		Table table = tableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);	
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(20, true));
		tableLayout.addColumnData(new ColumnWeightData(80, true));
		table.setLayout(tableLayout);		
			
		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Value");
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		FormData formData = new FormData();
		formData = new FormData();
		formData.top = new FormAttachment(compositeDataNavComposite);
		formData.bottom = new FormAttachment(100);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		tableViewer.getTable().setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(tabularDataNavComposite);
		formData.bottom = new FormAttachment(tableViewer.getTable());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		compositeDataNavComposite.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(0,0);
		formData.bottom = new FormAttachment(compositeDataNavComposite);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		tabularDataNavComposite.setLayoutData(formData);

		return composite;
	}

	/**
	 * Map 과 같은 데이터 타입 편집에 사용함
	 */
	private Composite createTabularDataNavigationButtons(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NULL);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.spacing = 5;
		composite.setLayout(fillLayout);
		
		Button prev = new Button(composite, SWT.PUSH);
		prev.setText("<");
		prev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		Label tabularDataIndexLabel = new Label(composite,SWT.NONE);
		tabularDataIndexLabel.setText("Tabular Data Navigation");
		Button next = new Button(parent, SWT.PUSH);
		next.setText(">");
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		return composite;
	}
	
	/**
	 * 일반 모델 객체/해당 객체의 배열 등의 편집에 사용함
	 */
	private Composite createCompositeDataNavigationButtons(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NULL);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.spacing = 5;
		composite.setLayout(fillLayout);
		
		Button prev = new Button(parent, SWT.PUSH);
		prev.setText("<");
		prev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		Label compositeDataIndexLabel = new Label(parent,SWT.NONE);
		compositeDataIndexLabel.setText("Composite Data Navigation");
		Button next = new Button(parent, SWT.PUSH);
		next.setText(">");
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		return composite;
	}
	
	
	@Override
	protected Object doGetValue() {		
		return null;
	}

	@Override
	protected void doSetFocus() {
		
	}

	@Override
	protected void doSetValue(Object value) {
	}	
}
