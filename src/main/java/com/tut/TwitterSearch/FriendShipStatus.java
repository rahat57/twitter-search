package com.tut.TwitterSearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import twitter4j.Friendship;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FriendShipStatus {
	static int count=1;
    public static void main( String[] args ) throws TwitterException, IOException
    {
    
    	ArrayList<Long> tweetList = new ArrayList<Long>();
            Twitter twitter=null;
            twitter = authorizeUser(twitter);
            QueryResult result;
            try {
      
                Query query = new Query("raheel");
                query.setCount(100);
                Map<String, RateLimitStatus> rateLimitStatus = twitter	.getRateLimitStatus("search");
    			RateLimitStatus searchTweetsRateLimit = rateLimitStatus	.get("/search/tweets");
                do {
                	
                    result = twitter.search(query);
                    
                    List<Status> tweets = result.getTweets();
                    for (Status tweet : tweets) {
                    
                    	//twitter.showFriendship(count+ " "+" orbittest2", tweet.getUser().getScreenName());
                    	ResponseList<Friendship> FriendshipStatus = twitter.lookupFriendships(tweet.getUser().getScreenName());
                    	System.out.println("relation: "+FriendshipStatus);
                    	for(Friendship obj : FriendshipStatus){
                    		
                    		System.out.println("called "+count+" Followed by "+obj.isFollowedBy()+" Followinf "+obj.isFollowing());
                        	count++;
                    	}

                    	System.out.println("line "+count+" Added:  " +tweet.getUser().getScreenName()+" --"+ tweet.getText());
                    	tweetList.add(tweet.getId());          	
                    }

                    
                    //String resp = new ObjectMapper().writeValueAsString(tweetList);
                   
                } while (searchTweetsRateLimit.getRemaining() != 0);
                
             
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println(count+" Failed to search tweets: " + te.getMessage());
                System.exit(-1);
            }
            
            
        }
    
    public static Twitter authorizeUser(Twitter twitter)
			throws TwitterException, IOException {
		String env = System.getenv("TSAK_CONF");
		// Twitter twitter=getTwitterInstance();
		if (twitter == null) {
			File propConfFile = new File(env + File.separator
					+ "twitter.properties");
			if (!propConfFile.exists()) {
				System.out.println("tsak.properties file does not exist in: "
						+ env);
			}
			Properties prop = new Properties();
			InputStream propInstream = new FileInputStream(propConfFile);
			prop.load(propInstream);
			propInstream.close();
			String consumerKey = prop.getProperty("consumerKey");
			String consumerSecret = prop.getProperty("consumerSecret");
			String accessToken = prop.getProperty("accessToken");
			String accessSecret = prop.getProperty("accessSecret");
			// System.out.println("consumerKey: "+consumerKey+" accessSecret: "+accessSecret+" accessToken: "+accessToken+" consumerSecret: "+consumerSecret);
			if (consumerKey == null || consumerSecret == null
					|| accessToken == null || accessSecret == null) {
				System.out.println("some or all keys are missing!");
			}

			twitter = new TwitterFactory(
					(new ConfigurationBuilder().setDebugEnabled(true)
							.setOAuthConsumerKey(consumerKey.trim())
							.setOAuthConsumerSecret(consumerSecret.trim())
							.setOAuthAccessToken(accessToken.trim())
							.setOAuthAccessTokenSecret(accessSecret.trim()))
							.build()).getInstance();
		}
		twitter.verifyCredentials();
		return twitter;
	}
}
