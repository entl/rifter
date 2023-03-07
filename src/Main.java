import Database.*;
import Game.GameWindow;
import Menu.Landing;
import Menu.Profile;
import Menu.Settings;
import Menu.Wallet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static Scanner scannerInput = new Scanner(System.in);
    //create class object here to make them global
    public static GameWindow gameWindow = new GameWindow();
    public static Account user = new Account(Constants.ACCOUNTS_CSV);
    public static Transactions transactions;
    public static Friends friends;
    public static Quests quests;

    public static void main(String[] args)
    {
//        gameWindow.start();
        System.out.println("-".repeat(30));
        System.out.printf("%17s%n", "RIFTER");
        System.out.println("-".repeat(30));

        System.out.print("1. Register\n2. Login\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();
        try
        {
            switch (userChoice)
            {
                case "1" ->
                {
                    if (Landing.register(user, scannerInput))
                        if (Landing.login(user, scannerInput))
                            home();
                }
                case "2" ->
                {
                    if (Landing.login(user, scannerInput))
                        home();
                }
                default -> System.out.println("[-] Wrong choice");
            }
        }
        catch(IOException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }

    public static void home()
    {
        transactions = new Transactions(Constants.TRANSACTIONS_CSV, Constants.ITEMS_FILE, user);
        friends = new Friends(Constants.FRIENDS_CSV, Constants.ACCOUNTS_CSV, user);
        quests = new Quests(Constants.QUESTS_CSV, Constants.USES_QUESTS_CSV, user);
        while (true)
        {
            clearScreen();

            System.out.print("1. Map\n2. Settings\n3. Wallet\n4. Profile\n\n[+] Choose one: ");
            String userChoice = scannerInput.nextLine();
            switch (userChoice)
            {
                case "1" ->
                {
                    if (gameWindow.getIsOpen())
                    {
                        System.out.println("[!] It seems game already opened");
                        pressToContinue();
                    }
                    else
                    {
                        gameWindow.start();
                    }
                }
                case "2" -> menuSettings();
                case "3" -> menuWallet();
                case "4" -> menuProfile();
                default -> System.out.println("[-] No such option");
            }
            pressToContinue();
        }
    }

    public static void menuSettings()
    {
        //!!!!!!!!!!!!!!!!!!!!!!!!TO DO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        clearScreen();
        System.out.print("1. Help\n2. Change password\n3. Logout\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();

        try
        {
            switch (userChoice)
            {
                case "1" -> Settings.help(scannerInput);
                case "2" -> Settings.changePassword(scannerInput, user);
                case "3" -> Settings.logout();
                default -> System.out.println("[-] No such option");
            }
        }
        catch (IOException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }

    public static void menuWallet()
    {
        //!!!!!!!!!!!!!!!!!!!!!!!!TO DO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        System.out.println("1. Purchase history\n2. Top up wallet");
    }

    public static void menuProfile()
    {
        clearScreen();
        System.out.print("1. Display friends\n2. Display requests\n3. Add friend\n4. Delete Friend\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();
        switch (userChoice)
        {
            case "1" -> Profile.displayFriends(friends);
            case "2" -> Profile.displayRequests(friends);
            case "3" ->
            {
                try
                {
                    Profile.addFriend(friends, scannerInput);
                }
                catch (IOException e)
                {
                    System.out.println("[!] Error: " + e);;
                }
            }
            case "4" -> Profile.deleteFriend(friends);
            default -> System.out.println("[-] No such option");
        }
    }

    public static void clearScreen()
    {
        //the only option to clear screen in IDE
        //https://stackoverflow.com/questions/25209808/clear-the-console-in-java
        for(int i = 0; i < 1000; i++)
        {
            System.out.println("\b");
        }
    }

    public static void pressToContinue()
    {
        System.out.println("\nPress any key to continue...");
        scannerInput.nextLine();
    }
}