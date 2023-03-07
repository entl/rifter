package Menu;

import Database.AccountColumns;
import Database.Friends;
import Database.FriendshipStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Profile
{
    public static void displayFriends(Friends friends)
    {
        System.out.printf("%-20s%-20s\n", "Username", "Score");
        for (HashMap<String, Object> friend:friends.getFriends())
        {
            System.out.printf("%-20s%-20s\n", friend.get(AccountColumns.USERNAME.name()), friend.get(AccountColumns.SCORE.name()));
        }
    }

    public static void displayRequests(Friends friends)
    {
        System.out.printf("%-20s%-20s\n", "Username", "Status");
        for (HashMap<String, String> friend:friends.getRequests())
        {
            System.out.printf("%-20s%-20s\n", friend.get(AccountColumns.USERNAME.name()), FriendshipStatus.PENDING.value);
        }
    }

    public static void addFriend(Friends friends, Scanner scannerInput) throws IOException
    {
         while (true)
         {
             System.out.println("[+} Enter , to leave");
             System.out.print("[+] Enter username: ");
             String username = scannerInput.nextLine();
             if (username.equals(","))
             {
                 break;
             }
             if (friends.sendRequest(username)) break;
         }
    }

    public static void deleteFriend(Friends friends)
    {

    }

    public static void displayLeaderboard(Friends friends)
    {

    }
}
