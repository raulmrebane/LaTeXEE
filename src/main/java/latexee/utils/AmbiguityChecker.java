package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.logging.Logger;

public class AmbiguityChecker {
	
	public static void check(ParseTree formula,Map<String,DeclareNode> declarations){
		//Find all used operators in the formula. 
		//This lets us cap the max string length for when we find the combinations
		//Which can greatly improve efficiency (for example if there was one unused operator with an absurd length)
		Set<OperatorDeclaration> usedOperations = getUsedOperations(formula, declarations);
		int maxLength = 0;
		for (OperatorDeclaration operatorDeclaration : usedOperations) {
			int opLength = operatorDeclaration.getOperator().length();
			if(opLength>maxLength){
				maxLength=opLength;
			}
		}
		
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
					if(string.equals(opString)){
						counter++;
					}
					
				}
				break;
			case "postfix":
				//Postfix is completely analogous to prefix
				Integer postfixOpLen = opString.length();
				List<String> postfixSameLen = prefixCombinations.get(postfixOpLen);
				for (String string : postfixSameLen) {
					if(string.equals(opString)){
						counter++;
					}
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
									String complete = pre + inf + post;
									if(complete.equals(opString)){
										counter++;
									}
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
				System.out.println("Operator \""+opString+"\" is ambiguous in formula "+formula.getText());
			}
		}
		
	}
	private static Set<OperatorDeclaration> getUsedOperations(ParseTree formula, Map<String,DeclareNode> declarations){
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
				operations.add((OperatorDeclaration)declaration);
			}
		}
		//Find operators from children
		for(int i=0;i<formula.getChildCount();i++){
			ParseTree childNode = formula.getChild(i);
			operations.addAll(getUsedOperations(childNode,declarations));
		}
		
		return operations;
	}
	
	//Helper function to create a map of prefix/postfix strings.
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
}



