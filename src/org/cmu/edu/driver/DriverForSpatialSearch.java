package org.cmu.edu.driver;

import java.util.Scanner;

import org.cmu.edu.searchEngine.SearchEngine_SpatialIndex;

public class DriverForSpatialSearch extends Driver{
	
	public static String getYear(Scanner scanner, String type){
		System.out.println("Please input the " + type + ": ");
		String year = scanner.nextLine();
		while(year == null || year.trim().length() == 0 || !year.matches("[12][01789][0-9][0-9]")){
			System.out.println("Invalid year, please input again: ");
			year = scanner.nextLine();
		}
		return year;
	}
	
	public static void main(String[] args){
		SearchEngine_SpatialIndex searchEngine = new SearchEngine_SpatialIndex();
		Scanner scanner = new Scanner(System.in);
		while(true){
			String query = getQuery(scanner);
			if(query.equals("exit")){
				break;
			}
			String startYear = getYear(scanner, "start year");
			String endYear = getYear(scanner, "end year");
			int numResultsToSkip = getNumber(scanner, "numResultsToSkip");
			int numResultsToReturn = getNumber(scanner, "numResultsToReturn");
			printResult(searchEngine.spatialSearch(query, startYear, endYear, numResultsToSkip, numResultsToReturn));
		}
		scanner.close();
	}
}
