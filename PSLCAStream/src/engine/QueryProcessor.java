/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import exception.PSLCAStreamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import query.Query;
import query.QueryGroupHash;

/**
 * Process the queries for each Document.
 * 
 * @author vinicius
 */
public class QueryProcessor {
    private int nThreads = 1;
    private QueryGroupHash[] queryIndex;
    private List<Thread> threads;
    private String queryFileName;
    private String[] xmlFileList;
    private HashMap<Query, List<Integer>> results;
    /**
     * 
     * @param queriesFileName
     * @param xmlFileList The list of documents 
     * @param xmlFilePath 
     */
    public QueryProcessor(String queryFileName, String[] xmlFileList) {
        threads = new ArrayList<Thread>();
        this.xmlFileList = xmlFileList;
        this.queryFileName = queryFileName;
    }
    
    /**
     * Initializes and separates the queries for parallel process and
     * process multiple queries for each document D and set the results for each it.
     */
    public void multipleQueriesStart(){
      
        
        for(String xmlFileName: xmlFileList){
            try {                
                nThreads = Runtime.getRuntime().availableProcessors();
                //nThreads = 1;
                queryIndex = new QueryGroupHash[nThreads];
                buildQueryIndexGroup(nThreads);
                
                for(int i = 0; i < nThreads ; i++){
                    threads.add(new Thread(new TaskControl(new FileReader
                        (new File("src/xml/"+xmlFileName).getAbsolutePath()), queryIndex[i])));
                }
                for(Thread t: threads){
                    t.start();
                }
                for(Thread t: threads){
                    t.join();
                }
                
            } catch (FileNotFoundException ex) {
                System.out.println("Error loading files");
                Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("Error in Thread");
                Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
            
    }
     
    /**
     * Group all queries with have common terms to accelerate the search engine.
     */
    public void groupQueryWhithCommonTerms(){
        
    }
    
    /**
     * Creates the query_index data.
     */
    public void buildQueryIndexGroup(int nThreads){
        try {
            
            BufferedReader queryFile = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
            int nQueriesPerGroup = this.countQueriesFile()/nThreads;
            List<List<Query>> queryGroup = new ArrayList<>();
            List<List<String>> termGroup = new ArrayList<>();
            queryGroup.add(new ArrayList<>());
            termGroup.add(new ArrayList<>());
            int i = 0, index = 0;
            String line = "";
            
            for(;(line = queryFile.readLine()) != null; i++){
                if(i == nQueriesPerGroup*(index+1) && nQueriesPerGroup > 0 && index+1 < nThreads){
                    queryGroup.add(new ArrayList<>());
                    termGroup.add(new ArrayList<>());
                    index++;
                }
                queryGroup.get(index).add(new Query(i, Arrays.asList(line.split("\\s+"))));
                for(String term: line.split("\\s+")){
                    if(!termGroup.get(index).contains(term))
                        termGroup.get(index).add(term);
                }
            }
            for(index = 0; index < nThreads; index++){
                queryIndex[index] = new QueryGroupHash();
                for(String term: termGroup.get(index)){
                    for(Query q: queryGroup.get(index)){
                        if(q.getQueryTerms().contains(term))
                            queryIndex[index].addQuery(term, q);
                    }
                }
            }
            
        } catch (PSLCAStreamException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    public int countQueriesFile() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(new File("src/query_test/"+queryFileName).getAbsolutePath()));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    public QueryGroupHash[] getQueryIndex() {
        return queryIndex;
    }

    public String[] getxmlFileList() {
        return xmlFileList;
    }

    public void setxmlFileList(String[] xmlFileList) {
        this.xmlFileList = xmlFileList;
    }

    public HashMap<Query, List<Integer>> getResults() {
        return results;
    }

    public void setResults(HashMap<Query, List<Integer>> results) {
        this.results = results;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public String getQueryFileName() {
        return queryFileName;
    }

    public void setQueryFile(String queryFileName) {
        this.queryFileName = queryFileName;
    }

    public String[] getXmlFileList() {
        return xmlFileList;
    }

    public void setXmlFileList(String[] xmlFileList) {
        this.xmlFileList = xmlFileList;
    }

}
