package com.tut.TwitterSearch;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class directSearch {
	static int count=1;
	public static void main(String[] args) throws TwitterException, IOException {

		ArrayList<Status> tweetList = new ArrayList<Status>();
        Twitter twitter=null;
        twitter = RetweetStatusLimit.authorizeUser(twitter);
        int count = 0;
        for (int i = 0; i < 1000; i++) {
        	 try {
             	
                 Query query = new Query("trump");
                 query.setCount(100);
                 QueryResult result;
                  result = twitter.search(query);
                  List<Status> tweets = result.getTweets();
                  System.out.println("request :"+(++count));
                    
        
             } catch (TwitterException te) {
               //  te.printStackTrace();
             	System.out.println("Limit Exceeded...!!");

             }
		}
       
	}
	
	
	public static boolean Retweeted(List<Status> Rtweets,Status status){
		
		boolean exist = false ;
		for(Status tweet : Rtweets){
			
//			System.out.println("retweetsiD "+tweet.getId()+" toretweetID "+status.getId());
			
			if(status.getId()==tweet.getId()){
				
				exist = true ;
//				System.out.println("Found " + exist);
			}
			
		}
		
		
		return exist ;
	}
	
	// FUNCTION TO WRITE DATA
		public static void writeFile(String fileName, String data)
				throws IOException {
			FileWriter fileWriter = new FileWriter(new File(fileName));
			fileWriter.write(data);
			fileWriter.flush();
			fileWriter.close();
		}
}
