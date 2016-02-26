package org.cmu.edu.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ETL {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/article";
	
	private static final String USER = Config.USER;
	private static final String PASS = Config.PASS;
	
	private static Connection conn = null;
	private static PreparedStatement stmtArticle = null;
	
	private static void closeResources(){
		try {
			if(stmtArticle != null){
				stmtArticle.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addArticle(Article article){
		try {
			stmtArticle.setString(1, article.getTitle());
			stmtArticle.setString(2, article.getMdate());
			stmtArticle.setString(3, article.getKey());
			stmtArticle.setString(4, article.getAuthorsToString());
			stmtArticle.setString(5, article.getPages());
			stmtArticle.setString(6, article.getYear());
			stmtArticle.setString(7, article.getVolume());
			stmtArticle.setString(8, article.getJournal());
			stmtArticle.setString(9, article.getNumber());
			stmtArticle.setString(10, article.getEe());
			stmtArticle.setString(11, article.getUrl());
			stmtArticle.addBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	   
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException{
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String insertArticle = "insert into article_table (title, mdate, keywords, authors, pages, year, " + 
									"volume, journal, number, ee, url) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmtArticle = conn.prepareStatement(insertArticle);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    
	    //Document document = builder.parse(ClassLoader.getSystemResourceAsStream("article.xml")); 
	    Document document = builder.parse(new FileInputStream(new File("article.xml")));
	    NodeList nodeList = document.getDocumentElement().getChildNodes();

	    int count = 0;
	    for(int i = 0; i < nodeList.getLength(); i++){
	    	Node node = nodeList.item(i);
	    	if(node instanceof Element){
	    		Article article = new Article();
	    		article.setMdate(node.getAttributes().getNamedItem("mdate").getNodeValue());
	    		article.setKey(node.getAttributes().getNamedItem("key").getNodeValue());
	    		NodeList children = node.getChildNodes();
	    		for(int j = 0; j < children.getLength(); j++){
	    			Node child = children.item(j);
	    			if(child instanceof Element){
	    				String content = child.getLastChild().getTextContent().trim();
	    				switch(child.getNodeName()){
	    				case "author" : article.addAuthors(content); break;
	    				case "title" : article.setTitle(content); break;
	    				case "pages" : article.setPages(content); break;
	    				case "year" : article.setYear(content); break;
	    				case "volume" : article.setVolume(content); break;
	    				case "journal" : article.setJournal(content); break;
	    				case "number" : article.setNumber(content); break;
	    				case "url" : article.setUrl(content); break;
	    				case "ee" : article.setEe(content); break;
	    				default: break;
	    				}
	    			}
	    		}
	    		count++;
	    		if(count > 3000)
	    			break;
	    		addArticle(article);
	    		if(count%1000 == 0){
	    			try {
	    				System.out.println("inserting...");
	    				long start = System.currentTimeMillis();
						stmtArticle.executeBatch();
						long end = System.currentTimeMillis();
						System.out.println(end - start);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}
	    }
		try {
			System.out.println("inserting...");
			long start = System.currentTimeMillis();
			stmtArticle.executeBatch();
			long end = System.currentTimeMillis();
			System.out.println(end - start);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(count);
	    closeResources();
	}
}
