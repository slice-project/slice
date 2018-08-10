package org.etri.slice.tools.adl.ui.contentassist;

import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider.Filter;
import org.eclipse.xtext.common.types.xtext.ui.TypeMatchFilters;

public class AcceptableInstanceFilter implements ITypesProposalProvider.Filter{
	
	Filter instanceFiltetr = TypeMatchFilters.canInstantiate();
	
	@Override
	public boolean accept(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
			String path) {	
		StringBuilder sb = new StringBuilder();		
		sb.append(packageName).append(".").append(simpleTypeName);	

		if(!instanceFiltetr.accept(modifiers, packageName, simpleTypeName, enclosingTypeNames, path)
				|| sb.toString().endsWith(".Field"))
			return false;
		
		return true;
	}

	@Override
	public int getSearchFor() {
		// TODO Auto-generated method stub
		return 0;
	}

}
