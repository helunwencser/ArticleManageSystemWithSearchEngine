package org.cmu.edu.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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
import java.io.IOException;

/* 
 * This is the class which defines the functionality of search
 * engine based on lucene, the version of lucene is 5.5.0.
 * 
 * */

public class SearchEngine_Lucene{
	public static void main(String[]args) 
			throws IOException, ParseException{
		/* 
		 * Specify the analyzer for tokenizing text.
		 * The same analyzer should be used for indexing and searching.
		 * */
		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		/* Create the index */
		Directory index = new RAMDirectory();
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		IndexWriter w = new IndexWriter(index, config);
		
		addDoc(w, "Lucene in Action", "193398817");
		addDoc(w, "Lucene for Dummies", "55320055Z");
		addDoc(w, "Managing Gigabytes", "55063554A");
		addDoc(w, "The Art of Computer Science", "9900333X");
		w.close();
		
		/* query */
		String querystr = "lucene";
		
		/*
		 * the "title" arg specifies the default field to use when no field
		 * is explicitly in the query
		 * */
		Query q = new QueryParser("title", analyzer).parse(querystr);
		
		/* search */
		int hitsPerPage=10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		/* display results */
		System.out.println("Found " + hits.length + " hits.");
		for(int i = 0; i < hits.length; ++i){
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + "." + d.get("isbn")+"\t"+ d.get("title"));
		}
		
		reader.close();
	}
	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException{
		
		Document doc = new Document();
		
		doc.add(new TextField("title", title, Field.Store.YES));
		
		/*
		 * use a StringField for isbn because we don't want it to be tokenized
		 * */
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
}
 