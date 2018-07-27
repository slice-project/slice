package org.etri.slice.tools.adl.ui.contentassist;

import org.eclipse.jdt.core.Flags;
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;

public class AcceptInterfaceFilter  implements ITypesProposalProvider.Filter{
	
	private String currentFQN;
	
	public AcceptInterfaceFilter(String currentFQN) {
		this.currentFQN = currentFQN;
	}
	
	@Override
	public boolean accept(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
			String path) {
		StringBuilder sb = new StringBuilder();		
		sb.append(packageName).append(".").append(simpleTypeName);	

		return !sb.toString().equals(currentFQN) && Flags.isInterface(modifiers) && !Flags.isAnnotation(modifiers);
	}

	@Override
	public int getSearchFor() {
		return 0;
	}

}
