package main.java.latexee.utils;

import main.java.latexee.declareast.OperatorDeclaration;

/**
 * DeclarationComparator class contains method for comparing priorities of declarations. Used for sorting.
 */
public class DeclarationComparator implements java.util.Comparator<OperatorDeclaration>{

	/**
	 * Override method which implements java.util.Comparator required to do sorting on operator declaratinos
	 * @param o1 first OperatorDeclaration instance
	 * @param o2 second OperatorDeclaration instance
	 * @return returns positive integer if the priority of o1 higher, 0 if same and negative integer o2's priority is higher.
	 */
	@Override
	public int compare(OperatorDeclaration o1, OperatorDeclaration o2) {
		int p1 = o1.getPriority();
		int p2 = o2.getPriority();
		if (p1 == p2) {
			if (o1.getType().equals("infix"))
				return 1;
			return 0;
		}
		return p1-p2;
	}


}
