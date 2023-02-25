package Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;


public class Friends
{
    private final File friendsFile;
    private final File accountsFile;
    private final String userId;

    //https://www.geeksforgeeks.org/creating-an-arraylist-with-multiple-object-types-in-java/
    private ArrayList<HashMap<String, Object>> friends = new ArrayList<>();

    private ArrayList<HashMap<String, String>> requests = new ArrayList<>();


    public Friends(String userId, String friendsFilename, String accountsFilename)
    {
        this.userId = userId;
        this.friendsFile = new File(friendsFilename);
        this.accountsFile = new File(accountsFilename);
        try
        {
            loadFriends();
            loadRequests();
        } catch (FileNotFoundException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }


    public ArrayList<HashMap<String, Object>> getFriends()
    {
        return friends;
    }

    public ArrayList<HashMap<String, String>> getRequests()
    {
        return requests;
    }

    private void loadFriends() throws FileNotFoundException
    {
        //create scanner to read from file
        Scanner scannerFriends = new Scanner(this.friendsFile);

        //skip first line
        scannerFriends.next();
        while (scannerFriends.hasNext())
        {
            //split string into array
            String[] friendship = scannerFriends.next().split(",");

            //check whether this record has appropriate user_id and status of record is accepted
            //in order to make program more flexible we do not hard code array position
            //instead enum was implemented, so if we have a change in the order of CSV columns, we will need to change only enum
            if (friendship[FriendsColumns.USER_ID.value].equals(this.userId) && friendship[FriendsColumns.STATUS.value].equals(FriendshipStatus.ACCEPTED.value))
            {
                //we open a file every iteration to start from the beginning
                //because friends.csv contains information only about friend relationship
                //we need to iterate over accounts.csv to get friend username and score
                //to answer a question why I do not add username and score to friends.csv
                //from my perspective in make lead to data inconsistency, for instance, if user would like to change username or when scored is changed
                Scanner scannerAccounts = new Scanner(this.accountsFile);

                //explicitly set delimiter to '\n' to make possible to store cardholder name with space
                scannerAccounts.useDelimiter("\n");
                while (scannerAccounts.hasNext())
                {
                    //split record to get values separately
                    String[] account = scannerAccounts.next().split(",");
                    //check if the record has friend id
                    if (account[AccountColumns.USER_ID.value].equals(friendship[FriendsColumns.FRIEND_ID.value]))
                    {
                        //declare temp hashmap to fill it with necessary values such as user_id, username and score
                        HashMap<String, Object> tempHashmap = new HashMap<>();
                        tempHashmap.put("userId", account[AccountColumns.USER_ID.value]);
                        tempHashmap.put("username", account[AccountColumns.USERNAME.value]);
                        tempHashmap.put("score", Float.parseFloat(account[AccountColumns.SCORE.value]));

                        friends.add(tempHashmap);
                        break;
                    }
                }
                scannerAccounts.close();
            }
        }
        scannerFriends.close();
    }

    public void loadRequests() throws FileNotFoundException
    {
        //create scanner to read from file
        Scanner scannerFriends = new Scanner(this.friendsFile);

        //skip first line
        scannerFriends.next();
        while (scannerFriends.hasNext())
        {
            //split string into array
            String[] friendship = scannerFriends.next().split(",");

            //from perspective of another user we are their friend, so we get records only where current user id is on place of friend id
            //in order to make program more flexible we do not hard code array position
            //instead enum was implemented, so if we change CSV column, we will need to change single enum
            if (friendship[FriendsColumns.FRIEND_ID.value].equals(this.userId) && friendship[FriendsColumns.STATUS.value].equals(FriendshipStatus.PENDING.value))
            {
                //we open a file every iteration to start from the beginning
                //we need to open accounts.csv to get username
                Scanner scannerAccounts = new Scanner(this.accountsFile);

                //explicitly set delimiter to '\n' to make possible to store cardholder name with space
                scannerAccounts.useDelimiter("\n");
                while (scannerAccounts.hasNext())
                {
                    //split record to get values separately
                    String[] account = scannerAccounts.next().split(",");

                    //check if the record has user id of sender
                    //as I mentioned before from their perspective they have user_id not friend id
                    if (account[AccountColumns.USER_ID.value].equals(friendship[FriendsColumns.USER_ID.value]))
                    {
                        //declare temp arraylist to fill it with necessary values such as user_id, username and score
                        HashMap<String, String> tempHashmap = new HashMap<>();
                        tempHashmap.put("userId", account[AccountColumns.USER_ID.value]);
                        tempHashmap.put("username", account[AccountColumns.USERNAME.value]);

                        requests.add(tempHashmap);
                        break;
                    }
                }
                scannerAccounts.close();
            }
        }
        scannerFriends.close();
    }

    public void sendRequest(String username) throws IOException
    {
        //check whether user already in friends
        for (HashMap<String, Object> friend : friends)
        {
            if (username.equals(friend.get("username")))
            {
                System.out.println("[-] This user is already your friend");
                return;
            }
        }

        Scanner scannerAccounts = new Scanner(this.accountsFile);
        FileWriter friendWriter = new FileWriter(this.friendsFile, true);

        //create variable which checks whether user exists in database
        boolean exists = false;
        while (scannerAccounts.hasNext())
        {
            //get separate values
            String[] account = scannerAccounts.next().split(",");

            if (account[AccountColumns.USER_ID.value].equals(this.userId))
            {
                System.out.println("[-] You cannot add yourself to friends");
                return;
            }

            //check if record has friend username. We are using enum to indicate index in array
            if (account[AccountColumns.USERNAME.value].equals(username))
            {
                Scanner scannerFriends = new Scanner(this.friendsFile);
                //skip first line
                scannerFriends.next();

                //after we have got right record in account.csv
                //we get their user_id and iterate over friends.csv
                //to check whether we have sent request already
                while (scannerFriends.hasNext())
                {
                    String[] friendship = scannerFriends.next().split(",");
                    //check if line contains this.user_id, friend_id and status is pending
                    if (friendship[FriendsColumns.USER_ID.value].equals(this.userId) && friendship[FriendsColumns.FRIEND_ID.value].equals(account[AccountColumns.USER_ID.value]) && friendship[FriendsColumns.STATUS.value].equals("pending"))
                    {
                        System.out.println("[-] You have already sent request to this user");
                        scannerFriends.close();
                        scannerAccounts.close();
                        friendWriter.close();
                        return;
                    }
                }
                scannerFriends.close();

                //add request to CSV
                friendWriter.write(String.format("%s,%s,%s\n", this.userId, account[AccountColumns.USER_ID.value], "pending"));
                exists = true;
                break;
            }
        }
        scannerAccounts.close();
        friendWriter.close();

        //if no account with such username return message
        if (!exists)
        {
            System.out.println("[-] User does not exist");
            return;
        }
        System.out.println("[+] Request is sent");
    }

    public void acceptRequest(int position) throws IOException
    {
        HashMap<String, String> request = this.requests.get(position);

        //create a temp file in order to rewrite friends.csv
        File tempFile = new File("temp.txt");
        FileWriter tempWriter = new FileWriter(tempFile, true);

        Scanner scannerFriends = new Scanner(this.friendsFile);

        while (scannerFriends.hasNext())
        {
            String currentLine = scannerFriends.next();
            String[] friendship = currentLine.split(",");

            //check whether line has friend id, user id, and status pending
            if (friendship[FriendsColumns.FRIEND_ID.value].equals(this.userId) && friendship[FriendsColumns.USER_ID.value].equals(request.get("userId")) && friendship[FriendsColumns.STATUS.value].equals(FriendshipStatus.PENDING.value))
            {
                //rewrite line where was status pending
                tempWriter.write(String.format("%s,%s,%s\n", this.userId, request.get("userId"), "accepted"));
                //write a new line which symmetric to previous. user id and friend id swapped because friendship is mutual
                tempWriter.write(String.format("%s,%s,%s\n", request.get("userId"), this.userId, "accepted"));
            } else
            {
                tempWriter.write(currentLine + "\n");
            }
        }
        scannerFriends.close();
        tempWriter.close();
        //delete old file
        friendsFile.delete();
        //rename temp file to old name
        tempFile.renameTo(friendsFile);

        //we need to reset array in order to apply changes
        this.friends.clear();
        this.requests.clear();
        loadFriends();
        loadRequests();

    }

    public ArrayList<HashMap<String, Object>> getLeaderBoard(Account user)
    {
        //we need to create temp variable in order to pass by value and do not change initial array
        ArrayList<HashMap<String, Object>> tempFriends = this.friends;

        //create hashmap of current user. This is made to make possible to show user in leaderboard with friends
        HashMap<String, Object> currentUser = new HashMap<>();
        currentUser.put("userId", "You");
        currentUser.put("username", user.getUsername());
        currentUser.put("score", user.getScore());

        tempFriends.add(currentUser);

        int n = tempFriends.size();
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                if ((float) tempFriends.get(j).get("score") < (float) tempFriends.get(j + 1).get("score"))
                {
                    // https://www.geeksforgeeks.org/how-to-swap-two-elements-in-an-arraylist-in-java/
                    Collections.swap(tempFriends, j, j + 1);
                }
            }
        }

        return tempFriends;
    }
}
