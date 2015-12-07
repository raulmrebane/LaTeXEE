package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.logging.Logger;

/*
 * AmbiguityChekcer.java is a class for checking whether a formula is ambiguous or not (could be parsed in several ways).
 * The ParseTrees of every given formula with its declarations are checked to locate any ambiguity, then
 * all used operators are gathered and checked further by String comparison. 
 */

public class AmbiguityChecker {
	
	/*
	 * This method checks recursively whether a prefix or postfix operator has a child that is an operator with the type infix. 
	 * If such a child is found, its operator is returned, otherwise an empty String.
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 * @param priority - a non-negative Integer if the checking is done with priorities, otherwise -1
	 * @return - the operator of the found infix child or an empty String if one was not found 
	 */
	public static String getInfixChild(ParseTree formula, Map<String, DeclareNode> declarations, int priority) {
		String s = "";
		for (int i = 0; i < formula.getChildCount(); i++) {
			ParseTree childNode = formula.getChild(i);
			String treeName = childNode.getClass().getSimpleName();
			if(treeName.contains("Context")){
				String noContext = treeName.substring(0, treeName.length()-7);
				DeclareNode declaration = declarations.get(noContext);
				if(declaration!=null && declaration instanceof OperatorDeclaration){
					OperatorDeclaration castDec = (OperatorDeclaration) declaration;
					if (castDec.getType().equals("infix") && (castDec.getPriority() == priority || priority == -1)) {
						s = castDec.getOperator();
						break;
					}
				}
			}
			s += getInfixChild(childNode, declarations, priority);
		}
		return s;
	}
	
	//
	/*
	 * This method gathers all consecutive prefix or postfix operators starting from the current node into an ArrayList
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 * @param priority - a non-negative Integer if the checking is done with priorities, otherwise -1
	 * @param sufixList - the resulting list where the operators are gathered
	 * @param sufix - either "prefix" or "postfix"
	 * @return - an ArrayList<String> with all gathered prefix or postfix operators
	 */
	public static List<String> gatherSufixes(ParseTree formula, Map<String, DeclareNode> declarations, int priority, List<String> sufixList, String sufix) {
		for (int i = 0; i < formula.getChildCount(); i++) {
			ParseTree childNode = formula.getChild(i);
			String treeName = childNode.getClass().getSimpleName();
			if(treeName.contains("Context")){
				String noContext = treeName.substring(0, treeName.length()-7);
				DeclareNode declaration = declarations.get(noContext);
				if(declaration!=null && declaration instanceof OperatorDeclaration){
					OperatorDeclaration castDec = (OperatorDeclaration) declaration;
					if (castDec.getType().equals(sufix) && (castDec.getPriority() == priority || priority == -1)) {
						sufixList = gatherSufixes(childNode, declarations, priority, sufixList, sufix);
						sufixList.add(castDec.getOperator());
					}
				}
			}
			List<String> tempList = new ArrayList<>();
			sufixList.addAll(gatherSufixes(childNode, declarations, priority, tempList, sufix));
		}
		return sufixList;
	}
	
	/*
	 * This is the main method of the class where used operators are gathered and checking is called out, based on
	 * whether the checking is done with or without priorities.
	 * @param withPriority - a boolean value that determines if we are currently checking with priorities or not
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 */
	public static void checkAmbiguity(boolean withPriority, ParseTree formula, Map<String,DeclareNode> declarations) {	
		
		//Find all used operators in the formula. 
		//This lets us cap the max string length for when we find the combinations
		//Which can greatly improve efficiency (for example if there was one unused operator with an absurd length)
		Set<OperatorDeclaration> usedOperations = getUsedOperations(formula, declarations, withPriority);
		
		if (withPriority) {
			
			Map<Integer, Set<OperatorDeclaration>> priorityMap = new HashMap<>();
		
			for (OperatorDeclaration usedOperator : usedOperations)
				addToMap(usedOperator, priorityMap);
			
			Map<Integer, Map<String, DeclareNode>> priorityDeclarations = createPriorityDeclarations(declarations);
			
			for (int p : priorityMap.keySet()) {
				Set<OperatorDeclaration> prioritySet = priorityMap.get(p);
				int maxLength = getMaxLength(prioritySet);
				Map<String, DeclareNode> decs = priorityDeclarations.get(p);
				check(formula, decs, maxLength, prioritySet);
			}
			
		}
		else {
			int maxLength = getMaxLength(usedOperations);
			check(formula, declarations, maxLength, usedOperations);
		}
		
	}
	
	/*
	 * This method takes all declarations and returns a map where all OperatorDeclarations have been sorted by priority
	 * @ declarations - all declarations declared for the formula
	 * @return - a HashMap with priorities of operators as keys
	 */
	public static Map<Integer, Map<String, DeclareNode>> createPriorityDeclarations(Map<String, DeclareNode> declarations) {
		Map<Integer, Map<String, DeclareNode>> priorityMap = new HashMap<>();
		for (String key : declarations.keySet()) {
			DeclareNode dn = declarations.get(key);
			if (dn instanceof OperatorDeclaration) {
				int priority = ((OperatorDeclaration) dn).getPriority();
				if (priorityMap.containsKey(priority))
					priorityMap.get(priority).put(key, dn);
				else {
					Map<String, DeclareNode> decs = new HashMap<>();
					decs.put(key, dn);
					priorityMap.put(priority, decs);
				}
			}
		}
		return priorityMap;
	}
	
	/*
	 * This method takes a set of operators and returns the length of the longest one
	 * @param usedOperators - a set of operators
	 * @return - the length of the longest operator
	 */
	public static int getMaxLength(Set<OperatorDeclaration> usedOperations){
		int maxLength = 0;
		for (OperatorDeclaration operatorDeclaration : usedOperations) {
			int opLength = operatorDeclaration.getOperator().length();
			if(opLength>maxLength){
				maxLength=opLength;
			}
		}
		return maxLength;
	}
	
	
	/*
	 * This method is responsible for checking ambiguity for most cases. If an ambiguous operaotr is found, it is reported.
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 * @param maxLength - the length of the longest operator used in the formula
	 * @param usedOperations - the set of used operations
	 */
	public static void check(ParseTree formula, Map<String,DeclareNode> declarations, int maxLength, Set<OperatorDeclaration> usedOperations){
		
		//Sort all operators by their type so I can make combinations based off of them.
		//Type has to be taken into account because combinations with two infix operators next to each other
		//Or  prefix next to a postfix are invalid and could possibly give false positives.
		
		List<String> prefixOperators = new ArrayList<String>();
		List<String> postfixOperators = new ArrayList<String>();
		List<String> infixOperators = new ArrayList<String>();
		
		for (DeclareNode node : declarations.values()) {
			if(node instanceof OperatorDeclaration){
				OperatorDeclaration castNode = (OperatorDeclaration) node;
				String nodeType = castNode.getType();
				switch(nodeType){
				case "infix":
					infixOperators.add(castNode.getOperator());
					break;
				case "prefix":
					prefixOperators.add(castNode.getOperator());
					break;
				case "postfix":
					postfixOperators.add(castNode.getOperator());
					break;
				default:
					Logger.log("BUG: Found declared operator that is not infix/postfix/prefix and is not weeded out.");
					break;
				}
			}
		}
		List<String> initialCombinations = Arrays.asList("");
		Map<Integer,List<String>> initialMap = new HashMap<Integer,List<String>>();
		initialMap.put(0, initialCombinations);
		
		Map<Integer,List<String>> postfixCombinations = findCombinations(postfixOperators,initialMap,maxLength,0);
		Map<Integer,List<String>> prefixCombinations = findCombinations(prefixOperators,initialMap,maxLength,0);
		
		for (OperatorDeclaration usedOperator : usedOperations) {
			String opString = usedOperator.getOperator();
			String type = usedOperator.getType();
			//We find multiple ways to get the same operator string
			int counter = 0;
			switch(type){
			case "prefix":
				//For prefix operators, it is enough to see if the combinations we found earlier 
				//contain multiple of the operation as then they would have been made in different ways
				Integer prefixOpLen = opString.length();
				List<String> prefixSameLen = prefixCombinations.get(prefixOpLen);
				for (String string : prefixSameLen) {
					if(string.equals(opString))
						counter++;
				}
				break;
			case "postfix":
				//Postfix is completely analogous to prefix
				Integer postfixOpLen = opString.length();
				List<String> postfixSameLen = postfixCombinations.get(postfixOpLen);
				for (String string : postfixSameLen) {
					if(string.equals(opString))
						counter++;
				}
				break;
			case "infix":
				//For infix operators, there has to be a prefix string followed by SOME infix operator and a postfix string
				//Prefix and postfix strings can be empty.
				//Infix strings can't be empty because then every operator that has a unary version of itself would be
				//considered ambiguous due to invisible times
				//We find every possible version of the prefix+infix+postfix and see if we find multiple that match the operation
				
				//that's a lot of loops.
				Integer infixLen = opString.length();
				for (String inf : infixOperators) {
					Integer fillerPortion = infixLen-inf.length();
					for(int i=0;i<=fillerPortion;i++){
						List<String> prefixes = prefixCombinations.get(i);
						List<String> postfixes = postfixCombinations.get(fillerPortion-i);
						if(prefixes!=null&&postfixes!=null){
							for (String pre : prefixes) {
								for (String post : postfixes) {
									String complete = post + inf + pre;
									if(complete.equals(opString))
										counter++;
								}
							}
						}
					}
				}
				break;
			default:
				Logger.log("BUG: Found declared operator that is not infix/postfix/prefix and is not weeded out.");
				break;
			}
			if(counter>1){
				System.out.println("Operator \""+opString+"\" is ambiguous in formula "+formula.getText() + "\n");
			}
		}
		
	}
	

	/*
	 * This method gathers all used operators from all declared operators and also performs some initial checking.
	 * If an ambiguous operator is found, it is reported.
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 * @param withPriority - a boolean value that determines if we are currently checking with priorities or not
	 * @return - the set of all operators used within the formula
	 */
	private static Set<OperatorDeclaration> getUsedOperations(ParseTree formula, Map<String,DeclareNode> declarations, boolean withPriority){
		HashSet<OperatorDeclaration> operations = new HashSet<OperatorDeclaration>();	
		//Take the context name, see if it matches an operator usage
		String treeName = formula.getClass().getSimpleName();
		//Operator usages (among macro usages) all have Context in their name
		if(treeName.contains("Context")){
			String noContext = treeName.substring(0, treeName.length()-7); 
			
			//Find the appropriate declaration
			DeclareNode declaration = declarations.get(noContext);
			if(declaration!=null && declaration instanceof OperatorDeclaration){
				//Add it to result if it's an operator-type declaration
				OperatorDeclaration castDec = (OperatorDeclaration) declaration;
				operations.add(castDec);
				switch (castDec.getType()) {
				case "infix":
					String i = "";
					String i2 = "";
					if (withPriority) {
						i = getLongerInfix(formula, declarations, castDec.getPriority(), "postfix");
						i2 = getLongerInfix(formula, declarations, castDec.getPriority(), "prefix");
					}
					else {
						i = getLongerInfix(formula, declarations, -1, "postfix");
						i2 = getLongerInfix(formula, declarations, -1, "prefix");
					}
					if (!i.equals(""))
						operations.add(new OperatorDeclaration("", "infix", castDec.getPriority(), i + castDec.getOperator(), castDec.getAssociativity()));
					//formality - in case it's possible to parse a-+b with an infix - and prefix + (with infix + & postfix - also available)
					if (!i2.equals(""))
						operations.add(new OperatorDeclaration("", "infix", castDec.getPriority(), castDec.getOperator() + i2, castDec.getAssociativity()));
					break;
				case "prefix":
					int priority = -1;
					if (withPriority)
						priority = castDec.getPriority();
					String infixChild = getInfixChild(formula, declarations, priority);
					if (!infixChild.equals("")) {
						List<String> prefixOps = gatherSufixes(formula, declarations, priority, new ArrayList<>(), "prefix");
						prefixOps.add(0, castDec.getOperator());
						StringBuilder sb = new StringBuilder("");
						for (String o : prefixOps) {
							sb.append(o);
							if (sb.toString().equals(infixChild)) {
								System.out.println("Operator \""+infixChild+"\" is ambiguous in formula "+formula.getText() + "\n");
								//TODO: break it off.
							}
						}
					}
					break;
				case "postfix":
					int priority2 = -1;
					if (withPriority)
						priority2 = castDec.getPriority();
					String infixChild2 = getInfixChild(formula, declarations, priority2);
					if (!infixChild2.equals("")) {
						List<String> postfixOps = gatherSufixes(formula, declarations, priority2, new ArrayList<>(), "postfix");
						StringBuilder sb = new StringBuilder("");
						Collections.reverse(postfixOps);
						postfixOps.add(castDec.getOperator());
						for (String o : postfixOps) {
							sb.append(o);
							if (sb.toString().equals(infixChild2)) {
								System.out.println("Operator \""+infixChild2+"\" is ambiguous in formula "+formula.getText() + "\n");
								//TODO: break it off
							}
						}
					}
					break;
				}
			}
		}
		//Find operators from children
		for(int i=0;i<formula.getChildCount();i++){
			ParseTree childNode = formula.getChild(i);
			operations.addAll(getUsedOperations(childNode, declarations, withPriority));
		}
		
		return operations;
	}
	
	/*
	 * This is a helper function to create a map of prefix/postfix strings.
	 * @param operators - a list of operators which are going to be used for making combinations
	 * @param combinations - the resulting map with combination lengths as keys and lists with combinations of that length as values
	 * @param maxLength - the maximum length of the combinations
	 * @param currentLength - the current length of combinations
	 * @return - a map with combination lengths as keys and lists with combinations of that length as values
	 */
	private static Map<Integer,List<String>> findCombinations(List<String> operators, Map<Integer,List<String>> combinations, int maxLength, int currentLength){
		if(currentLength==maxLength){
			return combinations;
		}
		currentLength++;
		List<String> newPieces = new ArrayList<String>();
		for (String combinationPiece : combinations.get(currentLength-1)) {
			for (String operator : operators) {
				String newPiece = combinationPiece+operator;
				if(newPiece.length()<=maxLength){
					newPieces.add(newPiece);
				}
			}
		}
		
		if(!newPieces.isEmpty()){
			Map<Integer,List<String>> thisLevel = new HashMap<Integer,List<String>>(combinations);
			thisLevel.put(currentLength, newPieces);
			Map<Integer,List<String>> newValues = findCombinations(
					operators, 
					thisLevel, 
					maxLength, 
					currentLength);
			return newValues;
		} else {
			return combinations;
		}
	}
	
	//adds OperatorDeclarations to map by priority
	/*
	 * This function takes an OperatorDeclaration and a map of OperationDeclarations sorted by priorities and adds the 
	 * OperationDeclaration to the map.
	 * @param op - an OperatorDeclaration
	 * @param map - a map with operator priorities as keys and maps of OperatorDeclarations as values
	 */
	public static void addToMap(OperatorDeclaration op, Map<Integer, Set<OperatorDeclaration>> map) {
		int priority = op.getPriority();
		if (map.containsKey(priority))
			map.get(priority).add(op);
		else {
			Set<OperatorDeclaration> prioritySet = new HashSet<>();
			prioritySet.add(op);
			map.put(priority, prioritySet);
		}
	}
	
	/*
	 * This function returns the operator of a prefix or postfix child and an empty String if such is not found
	 * @param formula - the current node of a prefix or postfix operator
	 * @param declarations - all declarations declared for the formula
	 * @param priority - a non-negative Integer if the checking is done with priorities, otherwise -1
	 * @param sufix - either "prefix" or "postfix"
	 */
	//the -+ case: - is postfix, + infix, returns a new infix operator -+
	public static String getLongerInfix(ParseTree formula, Map<String, DeclareNode> declarations, int priority, String sufix) {	
		String s = "";
		for (int i = 0; i < formula.getChildCount(); i++) {
			ParseTree childNode = formula.getChild(i);
			String treeName = childNode.getClass().getSimpleName();
			if(treeName.contains("Context")){
				String noContext = treeName.substring(0, treeName.length()-7);
				DeclareNode declaration = declarations.get(noContext);
				if(declaration!=null && declaration instanceof OperatorDeclaration){
					OperatorDeclaration castDec = (OperatorDeclaration) declaration;
					if (castDec.getType().equals(sufix) && (castDec.getPriority() == priority || priority == -1))
						return castDec.getOperator();
				}
			}
			s += getLongerInfix(childNode, declarations, priority, sufix);
		}
		return s;
	}	
}