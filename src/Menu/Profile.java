package Menu;

import Database.AccountColumns;
import Database.Friends;
import Database.FriendshipStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Profile
{
    public static void displayFriends(Friends friends)
    {
        ArrayList<HashMap<String, Object>> friendsList = friends.getFriends();
        System.out.printf("%-4s.%-20s%-20s\n", "No.", "Username", "Score");
        for (int i = 0; i < friendsList.size(); i++)
        {
            System.out.printf("%-4s%-20s%-20s\n", i, friendsList.get(i).get(AccountColumns.USERNAME.name()), friendsList.get(i).get(AccountColumns.SCORE.name()));
        }
    }

    public static void displayRequests(Friends friends)
    {
        ArrayList<HashMap<String, String>> requests = friends.getRequests();
        System.out.printf("%-4s.%-20s%-20s\n", "No.", "Username", "Status");
        for (int i = 0; i < requests.size(); i++)
        {
            System.out.printf("%-4s%-20s%-20s\n", i, requests.get(i).get(AccountColumns.USERNAME.name()), FriendshipStatus.PENDING.value);
        }
    }

    public static void addFriend(Friends friends, Scanner scannerInput) throws IOException
    {
         while (true)
         {
             System.out.println("[+] Enter , to leave");
             System.out.print("[+] Enter username: ");
             String username = scannerInput.nextLine();
             if (username.equals(","))
             {
                 break;
             }
             if (friends.sendRequest(username)) break;
         }
    }

    public static void deleteFriend(Friends friends, Scanner scannerInput) throws IOException
    {
        displayFriends(friends);
        System.out.print("\n[+] Enter ID of friend record to delete: ");
        int recordId = scannerInput.nextInt();
        friends.deleteFriend(recordId);
    }

    public static void displayLeaderboard(Friends friends)
    {

    }
}
