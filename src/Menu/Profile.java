//locate file in package
package Menu;

//import local classes

import Database.AccountColumns;
import Database.Friends;

//import java classes
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Profile
{
    /**
     * Displays friends list of the current user
     *
     * @param friends instance of the Friends class of the current user
     */
    public static void displayFriends(Friends friends)
    {
        //get friends
        ArrayList<HashMap<String, Object>> friendsList = friends.getFriends();
        System.out.printf("\n%-4s.%-20s%-8s\n", "No.", "Username", "Score");
        for (int i = 0; i < friendsList.size(); i++)
        {
            System.out.printf("%-4s%-20s%-8s\n", i, friendsList.get(i).get(AccountColumns.USERNAME.name()), friendsList.get(i).get(AccountColumns.SCORE.name()));
        }
    }

    /**
     * Displays list of friend requests for the current user
     *
     * @param friends      instance of the Friends class of the current user
     * @param scannerInput is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     */
    public static void displayRequests(Friends friends, Scanner scannerInput) throws IOException, InputMismatchException
    {
        //get requests
        ArrayList<HashMap<String, String>> requests = friends.getRequests();
        System.out.printf("\n%-4s %-20s %-8s\n", "No.", "Username", "Status");
        for (int i = 0; i < requests.size(); i++)
        {
            System.out.printf("%-4s %-20s %-8s\n", i, requests.get(i).get(AccountColumns.USERNAME.name()), "Pending");
        }
        while (true)
        {
            //suggest user to accept request or leave
            System.out.println("\n[+] Enter '-1' to leave");
            System.out.print("[+] Enter No. of the request you would like to accept: ");
            int position = scannerInput.nextInt();
            //clear buffer
            scannerInput.nextLine();
            if (position <= -1)
            {
                break;
            } else if (position > requests.size())
            {
                System.out.println("[-] Wrong position");
                continue;
            }
            friends.acceptRequest(position);
            break;
        }
    }

    /**
     * Adding a friend based on username
     *
     * @param friends      instance of the Friends class of the current user
     * @param scannerInput is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     */
    public static void addFriend(Friends friends, Scanner scannerInput) throws IOException
    {
        while (true)
        {
            System.out.println("\n[+] Enter 'q' to leave");
            System.out.print("[+] Enter username: ");
            String username = scannerInput.nextLine();
            if (username.equals("q"))
            {
                break;
            }
            //if request successful break
            if (friends.sendRequest(username)) break;
        }
    }

    /**
     * Deletes a friend based on selected record id
     *
     * @param friends      instance of the Friends class of the current user
     * @param scannerInput is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     */
    public static void deleteFriend(Friends friends, Scanner scannerInput) throws IOException
    {
        displayFriends(friends);
        while (true)
        {
            //suggest user to accept request or leave
            System.out.println("\n[+] Enter '-1' to leave");
            System.out.print("[+] Enter No. of the request you would like to accept: ");
            int recordId = scannerInput.nextInt();

            //clear buffer
            scannerInput.nextLine();
            if (recordId == -1)
            {
                break;
            } else if (recordId > friends.getFriends().size())
            {
                System.out.println("[-] Wrong position");
                continue;
            }
            friends.deleteFriend(recordId);
            break;
        }
    }

    /**
     * Displays leaderboard
     *
     * @param friends instance of the Friends class of the current user
     */
    public static void displayLeaderboard(Friends friends)
    {
        ArrayList<HashMap<String, Object>> leaders = friends.getLeaderBoard();
        System.out.printf("\n%-4s %-20s %-8s\n", "No.", "Username", "Score");
        for (int i = 0; i < leaders.size(); i++)
        {
            System.out.printf("%-4s %-20s %-8s\n", i, leaders.get(i).get(AccountColumns.USERNAME.name()), leaders.get(i).get(AccountColumns.SCORE.name()));
        }
    }
}
