package main.java.latexee.utils;

import main.java.latexee.declareast.OperatorDeclaration;

/**
 * DeclarationComparator class contains method for comparing priorities of declarations. Used for sorting.
 */
public class DeclarationComparator implements java.util.Comparator<OperatorDeclaration>{

	/**
	 * Override method which implements java.util.Comparator required to do sorting on operator declarations
	 * @param o1 first OperatorDeclaration instance
	 * @param o2 second OperatorDeclaration instance
	 * @return returns 1 if the type of o2 is higher (infix the highest, postfix the lowest),
	 *  -1 if o2's type is higher. In case of equal types a positive integer is returned when 
	 *  the operator of o2 is longer, negative if shorter and 0 if they are of equal lengths.
	 */
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
