// Do not submit with package statements if you are using eclipse.
// Only use what is provided in the standard libraries.

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NaiveBayes {
	
	HashMap<String, int[]> wordMap = null;
	double pr_H = 0.0;
	double pr_S = 0.0;
	int totalSpam = 0;
	int totalHam = 0;

    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Train your Naive Bayes Classifier based on the given training
     * ham and spam emails.
     *
     * Params:
     *      hams - email files labeled as 'ham'
     *      spams - email files labeled as 'spam'
     */
    public void train(File[] hams, File[] spams) throws IOException {
      // map from words to an int arr where arr's 0 index is
      // its ham count and 1 index is it's spam count.
      HashMap<String,int[]> wordMap = new HashMap<>();

      for(int i = 0; i < hams.length; i++){
        HashSet<String> wordSet = tokenSet(hams[i]);
        for(String s:wordSet){
          if(wordMap.get(s) == null) {
        	  wordMap.put(s, new int[2]);
          }
          wordMap.get(s)[0]++;
        }
      }
      
      for(int i = 0; i < spams.length; i++){
          HashSet<String> wordSet = tokenSet(spams[i]);
          for(String s:wordSet){
            if(wordMap.get(s) == null) {
          	  wordMap.put(s, new int[2]);
            }
            wordMap.get(s)[1]++;
          }
        }
      
      this.wordMap = wordMap;
      this.pr_H = 1.0 * hams.length / (hams.length + spams.length);
      this.pr_S = 1.0 * spams.length / (hams.length + spams.length);
      this.totalHam = hams.length;
      this.totalSpam = spams.length;
    }
    
    public static String print(HashMap<String, int[]> map) {
    	
    	String retStr = "";
    	
    	for(String s: map.keySet()) {
    		retStr += (s + " " + Arrays.toString(map.get(s)));
    		retStr += "\n";
    	}
    	
    	return retStr;
    }

    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Classify the given unlabeled set of emails. Follow the format in
     * example_output.txt and output your result to stdout. Note the order
     * of the emails in the output does NOT matter.
     *
     * Do NOT directly process the file paths, to get the names of the
     * email files, check out File's getName() function.
     *
     * Params:
     *      emails - unlabeled email files to be classified
     */
    public void classify(File[] emails) throws IOException {
        for(File f: emails) {
        	HashSet<String> wordSet = new HashSet<>();
        	for(String s: tokenSet(f)) {
        		if(this.wordMap.containsKey(s)) {
        			wordSet.add(s);
        		}
        	}
        	System.out.println(f.getName() + " " + spamResult(wordSet));
        }
        
        
    }
    
    public String spamResult(HashSet<String> wordSet) {
    	double spamTotal = 0.0;
    	double hamTotal = 0.0;
    	for(String s: wordSet) {
    		int[] arr = wordMap.get(s);
    		spamTotal += Math.log((arr[1]+1.0)/(totalSpam+2.0));
    		hamTotal += Math.log((arr[0]+1.0)/(totalHam+2.0));
    	}
    	double computation1 = Math.log(this.pr_S) + spamTotal;
    	double computation2 = Math.log(this.pr_H) + hamTotal;
    	
    	return computation1 > computation2 ? "spam" : "ham";
    }
    


    /*
     *  Helper Function:
     *  This function reads in a file and returns a set of all the tokens.
     *  It ignores "Subject:" in the subject line.
     *
     *  If the email had the following content:
     *
     *  Subject: Get rid of your student loans
     *  Hi there ,
     *  If you work for us , we will give you money
     *  to repay your student loans . You will be
     *  debt free !
     *  FakePerson_22393
     *
     *  This function would return to you
     *  ['be', 'student', 'for', 'your', 'rid', 'we', 'of', 'free', 'you',
     *   'us', 'Hi', 'give', '!', 'repay', 'will', 'loans', 'work',
     *   'FakePerson_22393', ',', '.', 'money', 'Get', 'there', 'to', 'If',
     *   'debt', 'You']
     */
    public static HashSet<String> tokenSet(File filename) throws IOException {
        HashSet<String> tokens = new HashSet<String>();
        Scanner filescan = new Scanner(filename);
        filescan.next(); // Ignoring "Subject"
        while(filescan.hasNextLine() && filescan.hasNext()) {
            tokens.add(filescan.next());
        }
        filescan.close();
        return tokens;
    }
}
