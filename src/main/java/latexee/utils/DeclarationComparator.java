package main.java.latexee.utils;

import main.java.latexee.declareast.OperatorDeclaration;

public class DeclarationComparator implements java.util.Comparator<OperatorDeclaration>{
	
	@Override
	//sorts first by type (first infix, then prefix, then postfix), if types are equal, then by length (longer ones first).
	public int compare(OperatorDeclaration o1, OperatorDeclaration o2) {
		String t1 = o1.getType();
		String t2 = o2.getType();
		if (t1.equals(t2))
			return o2.getOperator().length() - o1.getOperator().length();
		else if (t1.equals("infix") || t1.equals("prefix") && t2.equals("postfix"))
			return -1;
		return 1;		
	}

}
