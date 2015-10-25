package main.java.latexee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import main.java.latexee.declareast.DeclareNode;
import main.java.latexee.declareast.MacroDeclaration;
import main.java.latexee.declareast.OperatorDeclaration;
import main.java.latexee.docast.DeclareStatement;
import main.java.latexee.docast.ParsedStatement;

public class GrammarGenerator {
	
	//This assumes that DeclareStatements have their appropriate nodes attached. 
	//If you're not sure, run your tree through DeclarationParser.declarationFinder(tree) first.
	//This method serves only as a proof of concept, in later iterations of the project the list would be created during AST traversal.
	public static ArrayList<DeclareNode> getDeclareNodes(ParsedStatement tree, ArrayList<DeclareNode> existingRules){
		
		if(tree instanceof DeclareStatement){
			existingRules.add(((DeclareStatement) tree).getNode());
		}
		
		for (ParsedStatement child : tree.getChildren()) {
			getDeclareNodes(child,existingRules);
		}
		return existingRules;
	}
	
	public static String createGrammar(ArrayList<DeclareNode> nodes){
		StringBuilder sb = new StringBuilder();
		
		HashMap<Integer,ArrayList<OperatorDeclaration>> operatorNodes = new HashMap<Integer,ArrayList<OperatorDeclaration>>();
		ArrayList<DeclareNode> macroNodes = new ArrayList<DeclareNode>();
		
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
		
		sb.append("grammar RuntimeGrammar;\n");
		sb.append("highestLevel : ");

		sb.append("highestNumber ;\n");
		
		ArrayList<Integer> priorities = new ArrayList<Integer>(operatorNodes.keySet());
		
		//lazy
		Collections.sort(priorities);

		if(priorities.size()==0){
			sb.append("highestNumber : lowestlevel;\n");
		}
		else{
			sb.append("highestNumber : level" + Integer.toString(priorities.get(0))+ ";\n");
			
			for(int i=0;i<priorities.size();i++){
				if(i<priorities.size()-1){
					sb.append("level"+Integer.toString(priorities.get(i))+" : ");
					
					for(OperatorDeclaration opNode : operatorNodes.get(priorities.get(i))){
						sb.append(opNode.toGrammarRule(priorities.get(i+1))+" | ");
					}
					
					sb.append("level"+Integer.toString(priorities.get(i+1))+";\n");
				}
				else{
					String lowestPriority = Integer.toString(priorities.get(i));
					sb.append("level"+lowestPriority+" : ");
					for(DeclareNode opNode : operatorNodes.get(priorities.get(i))){
						sb.append(opNode.toGrammarRule()+" | ");
					}
					sb.append("level"+Integer.toString(priorities.get(i)+1)+";\n");
					sb.append("level"+Integer.toString(priorities.get(i)+1)+ " : lowestLevel;\n");
				}
			}
		}
		sb.append("lowestLevel : '{' highestLevel '}' |");
		for (DeclareNode node : macroNodes){
			sb.append(node.toGrammarRule()+" | ");
		}
		sb.append(" LEXERRULE;\n");
		sb.append("LEXERRULE : [0-9]+ | [a-z]+;\n");
		sb.append("OTHER : .->skip;");
		
		return sb.toString();
	}
	public static void exampleCase(){
		ParsedStatement AST = DeclarationParser.giveMeTheTestCase();
		DeclarationParser.declarationFinder(AST);
		ArrayList<DeclareNode> grammarNodes = getDeclareNodes(AST, new ArrayList<DeclareNode>());
		System.out.println(createGrammar(grammarNodes));
	}
	
	
}
