package main.java.latexee.utils;


/**
 * StringLengthComparator class contains method for comparing strings by length. Used for sorting arrays of strings.
 */
public class StringLengthComparator implements java.util.Comparator<String> {

	/**
	 * Compare method to sort a list of strings by their length from shorter to longer.
	 * @param s1 first input string
	 * @param s2 second input string
	 * @return returns positive integer if s1 riority is higher, 0 if same and negative integer if s2 priority is higheer
	 */
	//sorts a list of strings by their lengths, shorter ones first.
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}

}
