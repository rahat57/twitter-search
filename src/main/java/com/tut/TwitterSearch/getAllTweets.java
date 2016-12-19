package com.tut.TwitterSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class getAllTweets {

	public static void main(String[] args) throws TwitterException, IOException {

		List ListTweetes = new ArrayList();
		Twitter twitter = null;
		twitter = RetweetStatusLimit.authorizeUser(twitter);
		int paheNo =1 ;
		int id = 0;
		try {
			do {
				
				 int size = ListTweetes.size(); 
			Paging paging = new Paging(paheNo++, 200);
			ListTweetes.addAll(twitter.getUserTimeline(paging));
			
		//	List<Status> Rtweets = twitter.getUserTimeline("orbittest2",paging);
					
			List<Status> tweets = twitter.getUserTimeline(paging);
						
				for (Status Toretweet: tweets) {
				

					System.out.println(" get  "+id+" record  "+Toretweet.getId()+" text -"+Toretweet.getText());
					id++;
					//Method for checking whether tweet(given by tweet id) is already retweeted by user
				//	long ids=Toretweet.getId();
				//	Status status = twitter.showStatus(ids);
					
					/*if (Toretweet.isRetweetedByMe()) {
						
						System.out.println(" Already Exist:  " + Toretweet.getId());
					//	tweetList.add(tweet.getId());
						id++;	
					}
					else {
						
						System.out.println("retweeting...! ");
						System.out.println("id: "+Toretweet.getId()+" text: "+Toretweet.getText());
						twitter.retweetStatus(Toretweet.getId()); 
						
					}*/
					
				}
				if (ListTweetes.size() == size)
			          break;
				// String resp = new	 ObjectMapper().writeValueAsString(ListTweetes);
			//	 System.out.println("resp: "+resp);
				// writeFile("tweetIds.txt", resp);
					System.out.println("found "+id);
				
			/*	
				if (id > 100) {
					 String resp = new	 ObjectMapper().writeValueAsString(tweetList);
					 System.out.println("resp: "+resp);
					 writeFile("tweetIds.txt", resp);
				
			}
		*/
			} 
			while(true);
				
				
				

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());

		}
	}
	
	public static boolean Retweeted(List<Status> Rtweets,Status status){
		
		boolean exist = false ;
		for(Status tweet : Rtweets){
			
//			System.out.println("retweetsiD "+tweet.getId()+" toretweetID "+status.getId());
			
			if(status.getId()==tweet.getId()){
				
				exist = true ;
//				System.out.println("Found " + exist);
			//	break;
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
