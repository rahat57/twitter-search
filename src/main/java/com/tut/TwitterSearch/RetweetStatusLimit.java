package com.tut.TwitterSearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;

import twitter4j.FilterQuery;
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

/**
 * Hello world!
 *
 */
public class RetweetStatusLimit 
{
	static int count=1;
    public static void main( String[] args ) throws TwitterException, IOException
    {
    
    	ArrayList<Long> tweetList = new ArrayList<Long>();
            Twitter twitter=null;
            twitter = authorizeUser(twitter);
            
            try {
 
                Query query = new Query("raheel");
                query.setCount(100);
              
               
                QueryResult result;
                Map<String, RateLimitStatus> rateLimitStatus = twitter	.getRateLimitStatus("search");
    			RateLimitStatus searchTweetsRateLimit = rateLimitStatus	.get("/search/tweets");
                do {
                	
                    result = twitter.search(query);
                    
                    List<Status> tweets = result.getTweets();
                    for (Status tweet : tweets) {

                    	System.out.println("line "+count+" Added:  " +tweet.getUser().getScreenName()+" --"+ tweet.getText());
                    	tweetList.add(tweet.getId());          	
        				count++;
        				
                    }
                    if (count > 2000) {
                    	
                    	count=1;
                    	System.out.println("now checking function !!");
                    	
//    					String resp = new ObjectMapper().writeValueAsString(tweetList);
    					long being = System.currentTimeMillis();
    					for(Long tweetid : tweetList){
    						Status statuses = twitter.showStatus(tweetid);
        				
    						if (statuses.isRetweetedByMe()) {
        						
        						System.out.println("line "+count+" Already Exist:  " + tweetid);
        						// tweetList.add(tweet.getId());

        					} else {
        						
        						System.out.println("line "+count+" retweeting...! " + tweetid);

        						// twitter.retweetStatus(status.getId());

        					}
    						 
    						count++;
    					}
    					
    					long end = System.currentTimeMillis();
    				System.out.println(count+" times ShowStatus Function called..!!, Time taken" + (end-being));
    					
    					
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
