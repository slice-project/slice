/**
 * 
 */
package org.etri.slice.tools.jmxconsole.editors;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.etri.slice.tools.jmxconsole.jmx.JMXClient;
import org.etri.slice.tools.jmxconsole.model.Field;
import org.etri.slice.tools.jmxconsole.model.JMXConnectionInfo;
import org.etri.slice.tools.jmxconsole.model.JMXDomain;
import org.etri.slice.tools.jmxconsole.model.JMXTreeInput;
import org.etri.slice.tools.jmxconsole.model.MBeanAttribute;
import org.etri.slice.tools.jmxconsole.model.MBeanObject;
import org.etri.slice.tools.jmxconsole.swt.provider.AbstractTableLabelProvider;
import org.etri.slice.tools.jmxconsole.swt.provider.JMXMBeanTreeContentProvider;
import org.etri.slice.tools.jmxconsole.swt.provider.JMXMBeanTreeLabelProvider;
import org.etri.slice.tools.jmxconsole.swt.provider.TableContentProvider;

/**
 * @author Administrator
 *
 */
public class SLICEJMXEditor extends EditorPart {

	public static String ID = "org.etri.slice.tools.jmxconsole.editors";
	
	JMXClient jmxClient;
	
	TreeViewer domainAndObjectTree;
	Composite objectEditComposite;
	
	Composite attributeDetailComposite;
	
	/**
	 * Attribute Value TableViewer
	 */
	TableViewer attributeValueTableViewer;
	
	/**
	 *  Attribute Info TableViewer
	 */
	TableViewer attributeInfoTableViewer;
	
	/**
	 * Descriptor TableViewer
	 */
	TableViewer descriptorTableViewer;
	
	
	List<ObjectName> sliceMBeans;
	
	/**
	 * 현재 트리에서 선택된 속성
	 */
	MBeanAttribute selectedAttribute = null;

	private Composite primitiveComposite;

	private Composite openTypesComposite;

	private Button prevTabularData;

	private Label tabularDataIndexLabel;

	private Button nextTabularData;

	private Button upCompositeData;

	private Button prevCompositeData;

	private Label compositeDataIndexLabel;

	private Button nextCompositeData;

	private TableViewer compositeDataTableViewer;
	
	/**
	 * 
	 */
	public SLICEJMXEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName("JMX Console : " + getName());
		
		jmxClient = new JMXClient(getURL());
		//jmxClient.printSLICEMBeans();		
	}

	private String getName()
	{
		return ((JMXEditorInput)getEditorInput()).getName();
	}
	
	private String getURL()
	{
		return ((JMXEditorInput)getEditorInput()).getURL();
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}


	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		SashForm form = new SashForm(parent, SWT.HORIZONTAL);	
		
		form.setSashWidth(3);
		form.setLayout(new GridLayout());
		domainAndObjectTree = new TreeViewer(form, SWT.SINGLE);

		domainAndObjectTree.addDoubleClickListener( new IDoubleClickListener()
	    {
	        @Override
	        public void doubleClick( DoubleClickEvent event )
	        {
	            ISelection selection = event.getSelection();

	            if( selection instanceof ITreeSelection )
	            {
	            	TreePath[] paths= ((ITreeSelection) selection).getPaths();

	                for (int i= 0; i < paths.length; i++) 
	                	domainAndObjectTree.setExpandedState(paths[i], !domainAndObjectTree.getExpandedState(paths[i]));
	            }
	        }
	    });
		
		domainAndObjectTree.addSelectionChangedListener(new ISelectionChangedListener() {
			   public void selectionChanged(SelectionChangedEvent event) {
			       if(event.getSelection().isEmpty())
			           return;
			       
			       if(event.getSelection() instanceof IStructuredSelection) {
			           IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			           
			           Object selectedElement = selection.getFirstElement();
			           
			           if(selectedElement instanceof MBeanAttribute)
			           {
			        	   selectedAttribute = (MBeanAttribute)selectedElement;
			        	   
			        	   setSelectedAttributeInDetails((MBeanAttribute)selectedElement);
//			        	   prepareAttributeValueTableCellEditing(selectedAttribute);
			        	   attributeDetailComposite.setVisible(true);
			           }
			           else
			        	   attributeDetailComposite.setVisible(false);
			       }
			   }
			});
		
		objectEditComposite = new Composite(form, SWT.NULL);
		form.setWeights(new int[] {30, 70});
		
		attributeDetailComposite = createAttributeDetailsComposite(objectEditComposite);
		
		domainAndObjectTree.setContentProvider(new JMXMBeanTreeContentProvider());
		domainAndObjectTree.setLabelProvider(new JMXMBeanTreeLabelProvider());
				
		List<JMXDomain> domains = new ArrayList<JMXDomain>();
		
		JMXDomain domain = new JMXDomain("org.etri.slice");
		List<MBeanObject> children = new ArrayList<MBeanObject>();
		
		sliceMBeans = jmxClient.getSLICEMBeans();
		
		if(null == sliceMBeans)
		{
			MessageDialog.openInformation(
					this.getSite().getShell(),
					"SLICE JMX Console View Error",
					"Cannot get MBean for " + "org.etri.slice domain.");
			
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);			
			return;
		}
		
		for(ObjectName bean : sliceMBeans)
		{	
			MBeanObject mbeanObject = new MBeanObject(domain, bean);
					
			MBeanAttributeInfo[] attributeInfoArray = jmxClient.getAttributes(bean);
			
			if(null == attributeInfoArray)
				return;
			
			MBeanAttribute[] attributes = new MBeanAttribute[attributeInfoArray.length];
			
			int i = 0;
			for(MBeanAttributeInfo attributeInfo : attributeInfoArray)
				attributes[i++] = new MBeanAttribute(mbeanObject, attributeInfo);

			mbeanObject.setAttributes(attributes);
			
			children.add(mbeanObject);
		}

		domain.setChildren(children.toArray());

		domains.add(domain);
		JMXTreeInput input = new JMXTreeInput(domains.toArray());
		
		domainAndObjectTree.setInput(input);
		domainAndObjectTree.expandToLevel(2);
//		domainAndObjectTree.expandAll();
	}
	
	class DescriptorViewLabelProvider extends AbstractTableLabelProvider<JMXConnectionInfo> {
		@Override
		protected Image columnImage(JMXConnectionInfo element, int columnIndex) {
			return getImage(element);
		}

		@Override
		protected String columnText(JMXConnectionInfo element, int columnIndex) {
			JMXConnectionInfo consoleWebInfo = (JMXConnectionInfo) element;
			
			switch(columnIndex)
			{
				case 0:	return consoleWebInfo.getName();
				case 1:	return consoleWebInfo.getURL();
			}
			
			return "-";
		}	
	}
	
	private Composite createAttributeDetailsComposite(Composite parent)
	{
		parent.setLayout(new GridLayout());
		
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setVisible(false);
		
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		FormLayout layout = new FormLayout();		
		composite.setLayout(layout);
		
		Group attributeValueGroup = new Group(composite, SWT.SHADOW_IN);
		attributeValueGroup.setText("Attribute Value");
		attributeValueGroup.setLayout(new GridLayout());
		
		Group attributeInfoGroup = new Group(composite, SWT.SHADOW_IN);
		attributeInfoGroup.setText("Attribute Info");
		attributeInfoGroup.setLayout(new GridLayout());
		
		Group descriptorGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		descriptorGroup.setText("Descriptor");
		descriptorGroup.setLayout(new GridLayout());
		
		createAttributeValueComposite(attributeValueGroup);
				
		attributeInfoTableViewer = new TableViewer(attributeInfoGroup, SWT.BORDER | SWT.FULL_SELECTION);
		attributeInfoTableViewer.getTable().setHeaderVisible(true);
		attributeInfoTableViewer.getTable().setLinesVisible(true);	

		attributeInfoTableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		Table table = attributeInfoTableViewer.getTable();	
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
		
		attributeInfoTableViewer.setContentProvider(new TableContentProvider());
		attributeInfoTableViewer.setLabelProvider(new AbstractTableLabelProvider<Field>() {
			@Override
			protected Image columnImage(Field element, int columnIndex) {
				return getImage(element);
			}

			@Override
			protected String columnText(Field element, int columnIndex) {
				
				switch(columnIndex)
				{
					case 0:	return element.getKey();
					case 1:	return element.getValue().toString();
				}
				
				return "-";
			}	
		});
		
		descriptorTableViewer = new TableViewer(descriptorGroup, SWT.BORDER | SWT.FULL_SELECTION);
		descriptorTableViewer.getTable().setHeaderVisible(true);
		descriptorTableViewer.getTable().setLinesVisible(true);
		descriptorTableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
				
		table = descriptorTableViewer.getTable();	
		
		tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(20, true));
		tableLayout.addColumnData(new ColumnWeightData(80, true));
		table.setLayout(tableLayout);	
		
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Value");
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		descriptorTableViewer.setContentProvider(new TableContentProvider());
		descriptorTableViewer.setLabelProvider(new AbstractTableLabelProvider<String>() {
			@Override
			protected Image columnImage(String element, int columnIndex) {
				return getImage(element);
			}

			@Override
			protected String columnText(String element, int columnIndex) {
				String field = (String) element;
												
				switch(columnIndex)
				{
					case 0:	return field.substring(0, field.indexOf("="));
					case 1:	return field.substring(field.indexOf("=") + 1);
				}
				
				return "-";
			}	
		});
		
		FormData formData = new FormData();
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(34);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		attributeValueGroup.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(attributeValueGroup);
		formData.bottom = new FormAttachment(67);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		attributeInfoGroup.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(attributeInfoGroup);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		
		descriptorGroup.setLayoutData(formData);	

		return composite;
	}
	
	/**
	 * primitive를 위한 테이블 뷰어와 open-type 들을 위한 Composite로 구성된다.
	 * 
	 * @param parent
	 */
	private Composite createAttributeValueComposite(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		
		// for primitives
		primitiveComposite = new Composite(composite, SWT.NULL);
		primitiveComposite.setLayout(new GridLayout());
		
		attributeValueTableViewer = new TableViewer(primitiveComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = attributeValueTableViewer.getTable();		
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		attributeValueTableViewer.setColumnProperties(new String[] {"Key", "Value"});
				
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(20, true));
		tableLayout.addColumnData(new ColumnWeightData(80, true));
		table.setLayout(tableLayout);		
		
		TableViewerColumn column = new TableViewerColumn(attributeValueTableViewer, SWT.LEFT);
		  column.getColumn().setText("Name");
		  column.getColumn().setResizable(true);
		  column.getColumn().setMoveable(false);
		  
		  column = new TableViewerColumn(attributeValueTableViewer, SWT.LEFT);
		  column.getColumn().setText("Value");
		  column.getColumn().setResizable(true);
		  column.getColumn().setMoveable(false);
		
		attributeValueTableViewer.setContentProvider(new TableContentProvider());
		attributeValueTableViewer.setLabelProvider(new AbstractTableLabelProvider<Field>() {
			@Override
			protected Image columnImage(Field element, int columnIndex) {
				return getImage(element);
			}

			@Override
			protected String columnText(Field element, int columnIndex) {
				
				switch(columnIndex)
				{
					case 0:	return element.getKey();
					case 1:	return element.getValue().toString();
				}
				
				return "-";
			}
		});
		
		attributeValueTableViewer.setCellEditors(new CellEditor[] {new TextCellEditor(table), new TextCellEditor(table)});
		
		attributeValueTableViewer.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {								
				// Write JMX Attribute
				jmxClient.writeAttribute(selectedAttribute.getMbean().getObjectName(), selectedAttribute.getAttribute(), (String)value);
				
				// Read(Refresh) JMX Attribute
				refreshAttributeValue(selectedAttribute);
			}
			
			@Override
			public Object getValue(Object element, String property) {
				Field field = (Field) element;
				
				if(property.equals("Value"))
					return field.getValue().toString();
				
				return "";
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				return property.equals("Value");
			}
		});
		
		// for open-types
		openTypesComposite = new Composite(composite, SWT.NULL);
		formLayout = new FormLayout();
		openTypesComposite.setLayout(formLayout);
		openTypesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite tabularDataNavComposite = createTabularDataNavigationButtons(openTypesComposite);
		Composite compositeDataNavComposite = createCompositeDataNavigationButtons(openTypesComposite);
		compositeDataTableViewer = new TableViewer(openTypesComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		
		compositeDataTableViewer.setContentProvider(new TableContentProvider());
		compositeDataTableViewer.setLabelProvider(new AbstractTableLabelProvider<Field>() {
			@Override
			protected Image columnImage(Field element, int columnIndex) {
				return getImage(element);
			}

			@Override
			protected String columnText(Field element, int columnIndex) {
				
				switch(columnIndex)
				{
					case 0:	return element.getKey();
					case 1:	return element.getValue().toString();
				}
				
				return "-";
			}
		});
		
		table = compositeDataTableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);	
		
		tableLayout = new TableLayout();
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
		formData.top = new FormAttachment(0,0);
		formData.left = new FormAttachment(0,0);
		formData.right = new FormAttachment(100);
		
		tabularDataNavComposite.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(tabularDataNavComposite);
		formData.left = new FormAttachment(0,0);
		formData.right = new FormAttachment(100);
		
		compositeDataNavComposite.setLayoutData(formData);
		
		formData = new FormData();
		formData.top = new FormAttachment(compositeDataNavComposite);
		formData.bottom = new FormAttachment(100);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		
		table.setLayoutData(formData);
		
		// primitive / open type 화면은 동일한 위치에 중첩하고 그 중에 하나보 보이도록 한다.
		formData = new FormData();
		formData.top = new FormAttachment(0,0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		
		primitiveComposite.setLayoutData(formData);	
		
		formData = new FormData();
		formData.top = new FormAttachment(0,0);
		formData.left = new FormAttachment(0,0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		
		openTypesComposite.setLayoutData(formData);	
		return composite;
	}
	
	/**
	 * Map 과 같은 데이터 타입 편집에 사용함
	 */
	private Composite createTabularDataNavigationButtons(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NULL);
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 10;
		rowLayout.pack = true;	
		rowLayout.justify = false;
		
		composite.setLayout(rowLayout);
		
		prevTabularData = new Button(composite, SWT.PUSH);
		prevTabularData.setText("<");
		prevTabularData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		tabularDataIndexLabel = new Label(composite,SWT.NONE);
		tabularDataIndexLabel.setText("Tabular Data Navigation");
		nextTabularData = new Button(composite, SWT.PUSH);
		nextTabularData.setText(">");
		nextTabularData.addSelectionListener(new SelectionAdapter() {
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
		RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 5;
		rowLayout.pack = true;	
		rowLayout.justify = false;
		composite.setLayout(rowLayout);
		
		upCompositeData = new Button(composite, SWT.PUSH);
		upCompositeData.setText("<<");
		upCompositeData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		prevCompositeData = new Button(composite, SWT.PUSH);
		prevCompositeData.setText("<");
		prevCompositeData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		compositeDataIndexLabel = new Label(composite,SWT.NONE);
		compositeDataIndexLabel.setText("Composite Data Navigation");
		
		nextCompositeData = new Button(composite, SWT.PUSH);
		nextCompositeData.setText(">");
		nextCompositeData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		return composite;
	}
	
	/**
	 * 속성의 값을 읽어 온다.
	 */
	private void refreshAttributeValue(MBeanAttribute attribute)
	{
		Object value = jmxClient.readAttribute(attribute.getMbean().getObjectName(), attribute.getAttribute().getName());
		
		// Attribute Values Table Input
		List<Field> attributeValueInput = new ArrayList<Field>();		
					
		if(value instanceof CompositeDataSupport)
		{
			CompositeDataSupport support = (CompositeDataSupport)value;
						
			for(String itemName : support.getCompositeType().keySet())
			{
				attributeValueInput.add(new Field(itemName, support.get(itemName)));		
			}

			compositeDataTableViewer.setInput(attributeValueInput);
		}
		else
		{
			attributeValueInput.add(new Field(attribute.getAttribute().getName(), value));			
			attributeValueTableViewer.setInput(attributeValueInput);
		}
	}
	
	
	private void setEnableTabularControl(boolean enabled)
	{
		prevTabularData.setEnabled(false);
		tabularDataIndexLabel.setEnabled(enabled);
		nextTabularData.setEnabled(false);		
	}
	
	private void setEnableCompositeControl(boolean enabled)
	{
		upCompositeData.setEnabled(false);
		prevCompositeData.setEnabled(false);
		compositeDataIndexLabel.setEnabled(enabled);
		nextCompositeData.setEnabled(false);
	}
	
	/**
	 * 속성 상세 보기 창에 속성을 설정한다.
	 * 
	 * @param attribute
	 */
	private void setSelectedAttributeInDetails(MBeanAttribute attribute)
	{
		// Attribute Values Table Input
		String attributeType = attribute.getAttribute().getType();
		// Composite
		if(attributeType.contains("Composite"))
		{
			primitiveComposite.setVisible(false);
			openTypesComposite.setVisible(true);
			
			setEnableTabularControl(false);
			setEnableCompositeControl(true);
		}
		// Tabular
		else if(attributeType.contains("Tabular"))
		{
			primitiveComposite.setVisible(false);
			openTypesComposite.setVisible(true);
			
			setEnableTabularControl(true);
			setEnableCompositeControl(false);
		}
		else
		{
			primitiveComposite.setVisible(true);
			openTypesComposite.setVisible(false);
		}
			
		refreshAttributeValue(attribute);
		
		
		// Attribute Info Table Input
		List<Field> attributeInfoInput = new ArrayList<Field>();
		attributeInfoInput.add(new Field("Name", attribute.getAttribute().getName()));
		attributeInfoInput.add(new Field("Description", attribute.getAttribute().getDescription()));
		attributeInfoInput.add(new Field("Readable", attribute.getAttribute().isReadable() ? "true" : "false"));
		attributeInfoInput.add(new Field("Writable", attribute.getAttribute().isWritable() ? "true" : "false"));
		attributeInfoInput.add(new Field("Is", attribute.getAttribute().isIs() ? "true" : "false"));
		attributeInfoInput.add(new Field("Type", attribute.getAttribute().getType()));
		
		attributeInfoTableViewer.setInput(attributeInfoInput);
				
		// Attribute Descriptor Table Input
		// 각 필드문자열은 key=value 형식
		String[] descriptorInput = attribute.getAttribute().getDescriptor().getFields();
		descriptorTableViewer.setInput(descriptorInput);
	}
	
	@Override
	public void setFocus() {

	}
}
