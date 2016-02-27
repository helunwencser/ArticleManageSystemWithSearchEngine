package org.cmu.edu.driver;

import java.util.Scanner;
import org.cmu.edu.searchEngine.SearchEngine_Lucene;

public class DriverForBasicSearch extends Driver{
	
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
