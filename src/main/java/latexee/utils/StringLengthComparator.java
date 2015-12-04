package main.java.latexee.utils;

public class StringLengthComparator implements java.util.Comparator<String> {
	
	//sorts a list of strings by their lengths, shorter ones first.
	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}

}
