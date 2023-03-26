/* Written by Maksym Vorobyov
*  SID: 2217939
*  Team: Verity Rift
*  Live Brief 1
* */
//necessary imports from packages
import Database.*;
import Game.GameWindow;
import Menu.Landing;
import Menu.Profile;
import Menu.Settings;
import Menu.Wallet;

//imports of java packages
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    //create class instances here to make them global
    public static Scanner scannerInput = new Scanner(System.in);
    public static GameWindow gameWindow = new GameWindow();
    public static Account user = new Account(Constants.ACCOUNTS_CSV);
    public static Transactions transactions;
    public static Friends friends;
    public static Quests quests;

    /**
     * Entry point of the program. Used to sign up/sign in or reset password.
     * After authorization user can access program.
     */
    public static void main(String[] args)
    {
        while (true)
        {
            //display logo
            System.out.println("-".repeat(30));
            System.out.printf("%17s%n", "RIFTER");
            System.out.println("-".repeat(30));

            System.out.print("1. Register\n2. Login\n3. Forgot Password\n4. Exit\n\n[+] Choose one: ");
            String userChoice = scannerInput.nextLine();
            try
            {
                switch (userChoice)
                {
                    case "1" -> Landing.register(user, scannerInput);
                    case "2" ->
                    {
                        if (Landing.login(user, scannerInput))
                            home();
                    }
                    case "3" ->
                    {
                        Landing.forgotPassword(user, scannerInput);
                    }
                    case "4" -> System.exit(0);
                    default -> System.out.println("[-] Wrong choice");
                }
            } catch (IOException e)
            {
                System.out.println("[!] Error: " + e);
            }
        }
    }

    /**
     * Home method describes 'Home' part of the flowchart.
     */
    public static void home()
    {
        //after log in assign create instances based on user object
        transactions = new Transactions(Constants.TRANSACTIONS_CSV, Constants.ITEMS_FILE, user);
        friends = new Friends(Constants.FRIENDS_CSV, Constants.ACCOUNTS_CSV, user);
        quests = new Quests(Constants.QUESTS_CSV, Constants.USES_QUESTS_CSV, user);
        while (true)
        {
            clearScreen();

            System.out.print("1. Map\n2. Settings\n3. Wallet\n4. Profile\n5. Exit\n\n[+] Choose one: ");
            String userChoice = scannerInput.nextLine();
            switch (userChoice)
            {
                case "1" ->
                {
                    //check whether game is opened already
                    if (gameWindow.getIsOpen())
                    {
                        System.out.println("[!] It seems game already opened");
                    } else
                    {
                        gameWindow.start(quests, user);
                        //after we open the map start loop, so user cannot use console
                        while (gameWindow.getIsOpen())
                        {
                            System.out.println("[+] Close the game and press any key to continue...");
                            scannerInput.nextLine();
                        }
                    }
                }
                case "2" -> menuSettings();
                case "3" -> menuWallet();
                case "4" -> menuProfile();
                case "5" -> System.exit(0);
                default -> System.out.println("[-] No such option");
            }
            pressToContinue();
        }
    }

    /**
     * Implementation of 'Settings' flowchart
     */
    public static void menuSettings()
    {
        clearScreen();

        System.out.print("1. Help\n2. Change password\n3. Logout\n4. Back\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();

        try
        {
            switch (userChoice)
            {
                case "1" -> Settings.help(scannerInput);
                case "2" -> Settings.changePassword(scannerInput, user);
                case "3" -> Settings.logout();
                case "4" ->
                {
                    return;
                }
                default -> System.out.println("[-] No such option");
            }
        } catch (IOException e)
        {
            System.out.println("[!] Error: " + e);
        }
    }

    /**
     * Implements structure of the 'Wallet' flowchart.
     * Unlike, flowchart buy item functionality implemented in this method.
     * Because, I could not achieve it in Map method.
     */
    public static void menuWallet()
    {
        clearScreen();

        System.out.printf("[+] Your current balance is %f\n", user.getBalance());
        System.out.print("1. Purchase history\n2. Purchase item\n3. Top up wallet\n4. Top up history\n5. Back\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();

        try
        {
            switch (userChoice)
            {
                case "1" -> Wallet.purchaseHistory(transactions);
                case "2" -> Wallet.buyItem(transactions, scannerInput);
                case "3" -> Wallet.topUp(transactions, scannerInput);
                case "4" -> Wallet.topUpHistory(transactions);
                case "5" ->
                {
                    return;
                }
                default -> System.out.println("[-] No such option");
            }
        } catch (IOException e)
        {
            System.out.println("[!] Error: " + e);
            ;
        } catch (InputMismatchException e)
        {
            System.out.println("[!] Incorrect input");
            //clear buffer
            scannerInput.nextLine();
        }
    }

    /**
     * Takes a number and returns its square root.
     * Implementation of 'Profile' flowchart.
     */
    public static void menuProfile()
    {
        clearScreen();

        System.out.print("1. Display friends\n2. Display requests\n3. Add friend\n4. Delete Friend\n5. Display Leaderboard\n6. Back\n\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();

        try
        {
            switch (userChoice)
            {
                case "1" -> Profile.displayFriends(friends);
                case "2" -> Profile.displayRequests(friends, scannerInput);
                case "3" -> Profile.addFriend(friends, scannerInput);
                case "4" -> Profile.deleteFriend(friends, scannerInput);
                case "5" -> Profile.displayLeaderboard(friends);
                case "6" ->
                {
                    return;
                }
                default -> System.out.println("[-] No such option");
            }
        } catch (IOException e)
        {
            System.out.println("[!] Error: " + e);
            ;
        } catch (InputMismatchException e)
        {
            System.out.println("[!] Incorrect input");
            //clear buffer
            scannerInput.nextLine();
        }
    }

    /**
     * Method is used to clear console.
     * I could not find in java function like "clear", so this method deletes 1000 characters.
     * https://stackoverflow.com/questions/25209808/clear-the-console-in-java
     */
    public static void clearScreen()
    {
        for (int i = 0; i < 1000; i++)
        {
            System.out.println("\b");
        }
    }

    /**
     * Method which waits for user input to continue program
     */
    public static void pressToContinue()
    {
        System.out.println("\nPress any key to continue...");
        scannerInput.nextLine();
    }
}