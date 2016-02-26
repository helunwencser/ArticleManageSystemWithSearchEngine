package org.cmu.edu.article;

import java.util.ArrayList;
import java.util.List;

public class ArticleTitleAndAuthors {
	private String title;
	private List<String> authors;
	
	public ArticleTitleAndAuthors(String title, String authors){
		this.title = title;
		this.authors = new ArrayList<String>();
		String[] authorArray = authors.split("@");
		for(String author : authorArray){
			this.authors.add(author);
		}
	}

	public String getTitle() {
		return title;
	}

	public List<String> getAuthors() {
		return authors;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Title:\t" + this.title + "\n");
		sb.append("Authors: \n");
		for(String author : this.authors){
			sb.append("\t\t" + author + "\n");
		}
		return sb.toString();
	}
}
