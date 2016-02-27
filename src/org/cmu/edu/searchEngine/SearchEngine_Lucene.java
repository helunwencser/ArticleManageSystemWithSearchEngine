package org.cmu.edu.searchEngine;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.cmu.edu.article.ArticleTitleAndAuthors;
import org.cmu.edu.mysql.ReaderForBasicSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* 
 * This is the class which defines the functionality of search
 * engine based on lucene, the version of lucene is 5.5.0.
 * 
 * */

public class SearchEngine_Lucene{

	private StandardAnalyzer analyzer;
	
	private Directory index;
	
	public SearchEngine_Lucene(){
		
		this.analyzer = new StandardAnalyzer();
		
		/* Create the index */
		this.index = new RAMDirectory();
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		IndexWriter writer;
		try {
			writer = new IndexWriter(index, config);
			addDoc(writer, getContent());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Index created");
	}
	
	public List<String> basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
		
		List<String> res = new ArrayList<String>();
		/*
		 * the "title" arg specifies the default field to use when no field
		 * is explicitly in the query
		 * */
		Query q;
		try {
			q = new QueryParser("title", analyzer).parse(query);
			/* search */
			int hitsPerPage=numResultsToSkip + numResultsToReturn;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(q, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			for(int i = numResultsToSkip; i < hits.length; i++){
				int docId = hits[i].doc;
				Document document = searcher.doc(docId);
				res.add(document.get("title"));
			}
			reader.close();
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private void addDoc(IndexWriter writer, List<ArticleTitleAndAuthors> articles){
		for(ArticleTitleAndAuthors article : articles){
			addDoc(writer, article.getTitle(), article.getAuthors());
		}
	}
	
	private List<ArticleTitleAndAuthors> getContent(){
		ReaderForBasicSearch reader = new ReaderForBasicSearch();
		List<ArticleTitleAndAuthors> res = reader.getContent();
		reader.closeResources();
		return res;
	}
	
	private void addDoc(IndexWriter writer, String title, List<String> authors){
		
		Document doc = new Document();
		
		doc.add(new TextField("title", title, Field.Store.YES));
		
		for(String author : authors){
			doc.add(new TextField("author", author, Field.Store.YES));
		}
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
 