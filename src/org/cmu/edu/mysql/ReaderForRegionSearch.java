package org.cmu.edu.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.cmu.edu.article.ArticleTitleAndAuthors;
import org.cmu.edu.config.Config;

public class ReaderForRegionSearch {
	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private String DB_URL = "jdbc:mysql://localhost/article";
	
	private String USER = Config.USER;
	private String PASS = Config.PASS;
	
	private Connection conn = null;
	private Statement stmt = null;
	
	public ReaderForRegionSearch(){
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeResources(){
		try {
			if(stmt != null){
				stmt.close();
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

	public List<ArticleTitleAndAuthors> getContent(String startYear, String endYear){
		List<ArticleTitleAndAuthors> res = new ArrayList<ArticleTitleAndAuthors>();
		try {
			String query ="SET @poly="
					+ "'Polygon((" + startYear + " -1," + 
								endYear + " -1," + 
					            endYear + " 1," +
					            startYear + " 1," +
					            startYear + " -1))';";
			stmt.execute(query);
			String query1 = "select title, authors from article_table where " + 
					            "MBRContains(GeomFromText(@poly), year);";
			ResultSet result = stmt.executeQuery(query1);
			while(result.next()){
				res.add(new ArticleTitleAndAuthors(result.getString("title"), result.getString("authors")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return res;
	}
}

