/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;

import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.tartarus.snowball.ext.PorterStemmer;
 //Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 //LowerCaseFilter} and {@link StopFilter}, using a list of
 //English stop words.
 
public final class CMPT456Analyzer extends StopwordAnalyzerBase{

  public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;
  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;
    
  static List<String> stopWords = new ArrayList<String>();
  static CharArraySet stopSet;   
  static PorterStemmer stem = new PorterStemmer();

  public CMPT456Analyzer() {
    super();
    setStopWords();
  }
  
  public void setStopWords(){
    try {
        String s = "";
        File f = new File("/lucene-solr/lucene/demo/src/java/org/apache/lucene/demo/stopwords.txt");
        FileReader testw = new FileReader(f);
        BufferedReader br = new BufferedReader(testw);
        
        while( ( s = br.readLine() ) != null ){
            stopWords.add(s);
        }
        stopSet = new CharArraySet(stopWords, false);
        br.close();
    } catch (IOException e) {
        System.out.println("error! Text not found!");
    }
    
  }

  
  public void setMaxTokenLength(int length) {
    maxTokenLength = length;
  }
    

  @Override
  protected TokenStreamComponents createComponents(final String fieldName) {

    final StandardTokenizer src = new StandardTokenizer();
   
    src.setMaxTokenLength(maxTokenLength);
    TokenStream tok = new StandardFilter(src);
    
    tok = new LowerCaseFilter(tok);
    tok = new StopFilter(tok, stopSet);
    tok = new PorterStemFilter(tok);

    return new TokenStreamComponents(src, tok) {
      @Override
      protected void setReader(final Reader reader) {
        // So that if maxTokenLength was changed, the change takes
        // effect next time tokenStream is called:
        src.setMaxTokenLength(CMPT456Analyzer.this.maxTokenLength);
        super.setReader(reader);
      }
    };
  }

  @Override
  protected TokenStream normalize(String fieldName, TokenStream in) {
    TokenStream result = new StandardFilter(in);
    result = new LowerCaseFilter(result);
    return result;
  }

  
}
