package org.etri.slice.tools.adl.ui.contentassist;

import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;

public class AcceptableSuperTypeFilter implements ITypesProposalProvider.Filter{

	private String currentFQN;
	private String baseFQN;
	
	public AcceptableSuperTypeFilter(String currentFQN, String baseFQN)	{
		this.currentFQN = currentFQN;
		this.baseFQN = baseFQN;
	}
	
	@Override
	public boolean accept(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
			String path) {	
		StringBuilder sb = new StringBuilder();
		
		sb.append(packageName).append(".").append(simpleTypeName);	

		System.out.println("Interface Name = " + sb.toString() + ", baseFQN = " + baseFQN);	
		if(sb.toString().equals(currentFQN) || sb.toString().equals(baseFQN))
			return false;
		
		if(sb.toString().endsWith(".Field"))
			return false;
		
		return true;
	}

	@Override
	public int getSearchFor() {
		// TODO Auto-generated method stub
		return 0;
	}

}
