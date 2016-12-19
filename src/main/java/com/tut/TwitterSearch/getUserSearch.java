package com.tut.TwitterSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Relation;

import org.codehaus.jackson.map.ObjectMapper;

import twitter4j.Friendship;
import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class getUserSearch {

	public static void main(String[] args) throws TwitterException, IOException {

		Twitter twitter = null;
		twitter = RetweetStatusLimit.authorizeUser(twitter);
		
		
		try {
			int pageNo = 1;
			int count = 1;
			ResponseList<User> users;
			
			do {
				
				users = twitter.searchUsers("samaatv", pageNo);
				for (User user : users) {
					if (user.getStatus() != null) {

					/*	72 friend RelationshipJSONImpl{targetUserId=52136185, targetUserScreenName='MichaelCohen212',
						sourceBlockingTarget=false, sourceNotificationsEnabled=false, sourceFollowingTarget=false,
								sourceFollowedByTarget=false, sourceCanDm=false, sourceMutingTarget=false,
								sourceUserId=799593858585739264, sourceUserScreenName='orbittest2', wantRetweets=false}*/
						ResponseList<Friendship> friend = twitter.lookupFriendships(user.getScreenName());
						for(Friendship frnd : friend){
							System.out.println(count+" friend  followed by "+frnd.isFollowedBy()+" following "+frnd.isFollowing());
							
						}
						//limit 180
//						System.out.println(count+" friend  followed by "+friend.isSourceFollowedByTarget()+" following "+friend.isSourceFollowingTarget());
					//	System.out.println("index "+count + user.getScreenName() + " - "+ user.getStatus().getText());
						count++;
					} else {
						
						// the user is protected
						System.out.println("@" + user.getScreenName());
					}
				}
				pageNo++;
			} while (users.size() != 0 && pageNo < 50);

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());

		}
	}

	public static boolean Retweeted(List<Status> Rtweets, Status status) {

		boolean exist = false;
		for (Status tweet : Rtweets) {

			// System.out.println("retweetsiD "+tweet.getId()+" toretweetID "+status.getId());

			if (status.getId() == tweet.getId()) {

				exist = true;
				// System.out.println("Found " + exist);
				// break;
			}

		}

		return exist;
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
