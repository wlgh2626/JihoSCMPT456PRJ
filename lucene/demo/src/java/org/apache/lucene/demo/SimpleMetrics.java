package org.apache.lucene.demo;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.Term;
public class SimpleMetrics{
    
    public static final String field = "contents";
    
    //IndexSearcher searcher = new IndexSearcher(reader);

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Please Enter word to search");
        String s = scan.nextLine();
        s = s.trim();

        System.out.println("Searching for documents with: " + s);
        System.out.println("Total documents found: " + getDocNum(s) );
        System.out.println("Total frequency: " + getFreq(s) );
    }

    public static long getFreq(String s){
        long c = 0;
        Term t = new Term(field,s);
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            c = reader.totalTermFreq( t );
        } catch (IOException e) {
            c = 0;
        }  
        return c;
    }

    public static long getDocNum(String s){
        long c = 0;
        Term t = new Term(field,s);
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            c = reader.docFreq( t );
        } catch (IOException e) {
            c = 0;
        }  
        return c; 
    }
}