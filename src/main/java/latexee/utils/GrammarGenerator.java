package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.ParsedStatement;

/**
 * GrammarGenerator class contains method to generate grammar based on before-gathered operator and macro declaration rules.
 * Generated grammar is later used to parse the formulas.
 */
public class GrammarGenerator {
	/**
	 * This method is only used for testing purposes. Assumes that DeclareStatements have their appropriate nodes attached.
	 * If you're not sure, run your tree through DeclarationParser.declarationFinder(tree) first.
	 * @param tree statement tree where DeclareStatements have their appropriate nodes attached
	 * @param existingRules existing macro and operator rules
	 * @return returns an ArrayList of declaration nodes
	 */
	public static ArrayList<DeclareNode> getDeclareNodes(ParsedStatement tree, ArrayList<DeclareNode> existingRules){
		
		if(tree instanceof DeclareStatement){
			existingRules.add(((DeclareStatement) tree).getNode());
		}
		
		for (ParsedStatement child : tree.getChildren()) {
			getDeclareNodes(child,existingRules);
		}
		return existingRules;
	}

	/**
	 * This method translates operator and macro declarations to ANTLR grammar, which can and is later used to parse formulas.
	 * Chooses appropriate levels for each rule in the grammar.
	 * StringBuilder object is used to put together grammar rule-by-rule.
	 * @param nodes list of DeclareNode objects
	 * @return string of ANTLR grammar
	 */
	public static String createGrammar(List<DeclareNode> nodes){
		//Resulting grammar is built here.
		StringBuilder sb = new StringBuilder();
		
		//Map for parsing priority -> List of operators with that parsing priority
		HashMap<Integer,ArrayList<OperatorDeclaration>> operatorNodes = new HashMap<Integer,ArrayList<OperatorDeclaration>>();
		//Just all macro nodes
		ArrayList<DeclareNode> macroNodes = new ArrayList<DeclareNode>();
		
		//Populate the operator map and macro list
		for (DeclareNode unknownNode : nodes) {
			if(unknownNode instanceof OperatorDeclaration){
				OperatorDeclaration castNode = (OperatorDeclaration) unknownNode;
				int priority = castNode.getPriority();
				if(operatorNodes.containsKey(priority)){
					operatorNodes.get(priority).add(castNode);
				}else{
					operatorNodes.put(priority, new ArrayList<OperatorDeclaration>(Arrays.asList(castNode)));
				}
			}
			if(unknownNode instanceof MacroDeclaration){
				macroNodes.add(unknownNode);
			}
		}
		
		//Variable for keep DEFAULT contexts unique
		//Default contexts are ones where we go down in priority without performing an operation
		int defaultCounter = 0;
		
		//General header of the grammar
		sb.append("grammar RuntimeGrammar;\n");
		sb.append("highestLevel : ");
		sb.append("highestNumber #DEFAULT"+Integer.toString(defaultCounter)+";\n");
		defaultCounter++;
		
		//Gather all used priority levels up (there may be gaps, i.e. 1,2,7 are the only used priorities)
		ArrayList<Integer> priorities = new ArrayList<Integer>(operatorNodes.keySet());
		
		//Add invisible times priority as it is hard-coded. Adding value to operatorNodes so it would
		//comply with earlier design
		if(!priorities.contains(100)){
			priorities.add(100);
		}
		if(!operatorNodes.containsKey(100)){
			operatorNodes.put(100, new ArrayList<OperatorDeclaration>());
		}
		
		//Sort them so we can always use i+1 for the next priority to be used
		Collections.sort(priorities);
		
		//Edge case where we have no symbol operators defined, we jump to where the macro levels are
		if(priorities.size()==0){
			sb.append("highestNumber : lowestLevel #DEFAULT"+Integer.toString(defaultCounter)+";\n");
			defaultCounter++;
		}
		else{
			//Otherwise link to the start of the chain of operators
			sb.append("highestNumber : level" + Integer.toString(priorities.get(0))+ " #DEFAULT"+Integer.toString(defaultCounter)+"; \n");
			defaultCounter++;
			
			//Using i outside of the loop later as the last iteration is special
			//And this is faster than an if-else clause
			int i = 0;
			for(;i<priorities.size()-1;i++){

				int priority = priorities.get(i);
				String priorityAsString = Integer.toString(priority);
				String nextPriorityAsString = Integer.toString(priorities.get(i+1));
				
				sb.append("level"+priorityAsString+" : ");
				//Hard-coding the invisible times
				if(priority==100){
					sb.append("level"+priorityAsString+" "+"level"+nextPriorityAsString+" #INVISIBLETIMES\n|");
				}
				ArrayList<OperatorDeclaration> opNodes = operatorNodes.get(priorities.get(i));
				Collections.sort(opNodes, new DeclarationComparator());

				for(OperatorDeclaration opNode : opNodes){
					
					//Using the method which specifies which level the rule should point to
					//For example if we have addition on level 5 and the next level is 7
					//Then nextPriority that is handed will be 7 and we will ignore 6
					
					Integer nextPriority = priorities.get(i+1);
					sb.append(opNode.toGrammarRule(nextPriority)+"|");
				}
				
				sb.append("level" + nextPriorityAsString + " #DEFAULT"+Integer.toString(defaultCounter)+"; \n");
				defaultCounter++;
			}
			Integer lastPriority = priorities.get(i);
			String priorityAsString = Integer.toString(lastPriority);
			sb.append("level"+priorityAsString+": ");
			
			//Invisible times check
			if(lastPriority==100){
				sb.append("level"+priorityAsString+" level101"+" #INVISIBLETIMES\n|");
			}
			//Here opNode.toGrammarRule just points to a level that is 1 higher than the current one. 
			ArrayList<OperatorDeclaration> opNodes = operatorNodes.get(lastPriority);
			Collections.sort(opNodes, new DeclarationComparator());
			for(DeclareNode opNode : operatorNodes.get(lastPriority)){
				sb.append(opNode.toGrammarRule()+"|");
			}
			
			//As that level does not exist, we create it with just one rule that points to the next one.
			//Possible refactoring angle is to have OperatorDeclaration.toGrammarRule() take a string as an argument instead of an int
			//So you could just give "lowestlevel" for the last level instead of creating a new dead level.
			
			sb.append("level"+Integer.toString(lastPriority+1)+" #DEFAULT"+Integer.toString(defaultCounter)+"; \n");
			defaultCounter++;
			sb.append("level"+Integer.toString(lastPriority+1)+ " : lowestLevel #DEFAULT"+Integer.toString(defaultCounter)+"; \n");
			defaultCounter++;
			
			
		}
		
		//The last level includes brackets and macro operators
		sb.append("lowestLevel : '{' highestLevel '}' #BRACES\n|");
		sb.append("'(' highestLevel ')' #PARENS\n|");
		
		for (DeclareNode node : macroNodes){
			sb.append(node.toGrammarRule()+"|");
		}
		
		//And finally the lexer rules. 
		//There should be annotations for OMI/OMV to determine the kind of lexer rule used in ParseTree -> OM process.
		//Currently we're determining OMV/OMI based on matching the parse tree text with a regular expression.
		//That is just wasteful implementation and code duplication.
		
		//TODO: Annotations for OMI/OMV
		
		sb.append("LEXERRULE #DEFAULT"+Integer.toString(defaultCounter)+";\n");
		defaultCounter++;
		sb.append("LEXERRULE : [0-9]+ | [a-z];\n");

		return sb.toString();
	}	
}
