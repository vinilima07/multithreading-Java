/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.util.List;

/**
 *
 * @author vinicius franca, evandrino barros
 */
public class Query {
    private int queryID;
    private int lastResultId = -1;
    private List<String> queryTerms;
    private List<Integer> results;

    public Query(int queryID, List<String> queryTerms) {
        this.queryID = queryID;
        this.queryTerms = queryTerms;
    }
    
    public void addResult(Integer nodeId){
        this.results.add(nodeId);
    }

    public int getQueryID() {
        return queryID;
    }

    public void setQueryID(int queryID) {
        this.queryID = queryID;
    }

    public int getLastResultId() {
        return lastResultId;
    }

    public void setLastResultId(int lastResultId) {
        this.lastResultId = lastResultId;
    }

    public List<String> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<String> queryTerms) {
        this.queryTerms = queryTerms;
    }

    public List<Integer> getResults() {
        return results;
    }

    public void setResults(List<Integer> results) {
        this.results = results;
    }
    
    public void printQueryTerms(){
        for(String a: this.queryTerms){
            System.out.print(a+" ");
        }
        System.out.println("");
    }
    
}
