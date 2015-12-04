package main.java.latexee.utils;

import main.java.latexee.declareast.OperatorDeclaration;

public class DeclarationComparator implements java.util.Comparator<OperatorDeclaration>{
	
	@Override
	public int compare(OperatorDeclaration o1, OperatorDeclaration o2) {
		int p1 = o1.getPriority();
		int p2 = o2.getPriority();
		if (p1 == p2) {
			if (o1.getType() == "infix")
				return 1;
			return 0;
		}
		return p1-p2;
	}


}
