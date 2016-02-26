package org.cmu.edu.driver;

import java.util.Scanner;
import java.util.List;

import org.cmu.edu.lucene.SearchEngine_Lucene;

public class Driver {
	public static String getQuery(Scanner scanner){
		System.out.println("Please input key words, input \"exit\" to exit: ");
		String query = scanner.nextLine();
		while(query == null || query.trim().length() == 0){
			System.out.println("Key words cannot be empty, please input again: ");
			query = scanner.nextLine();
		}
		return query;
	}
	
	public static int getNumber(Scanner scanner, String type){
		System.out.println("Please input the number of " + type);
		String number = scanner.nextLine();
		while(number == null || number.trim().length() == 0 || !number.matches("[0-9]+")){
			System.out.println("Invalid input, please input again:");
			number = scanner.nextLine();
		}
		return Integer.parseInt(number);
	}
	
	public static void printResult(List<String> articles){
		System.out.println("Articles:");
		for(String article : articles){
			System.out.println("\t" + article);
		}
	}
	
	public static void main(String[] args){
		SearchEngine_Lucene searchEngine = new SearchEngine_Lucene();
		Scanner scanner = new Scanner(System.in);
		while(true){
			String query = getQuery(scanner);
			if(query.equals("exit")){
				break;
			}
			int numResultsToSkip = getNumber(scanner, "numResultsToSkip");
			int numResultsToReturn = getNumber(scanner, "numResultsToReturn");
			printResult(searchEngine.basicSearch(query, numResultsToSkip, numResultsToReturn));
		}
		scanner.close();
	}
}
