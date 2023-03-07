package Menu;

import Database.Account;

import java.io.IOException;
import java.util.Scanner;

public class Settings
{
    public static void help(Scanner sc)
    {

        System.out.print("1. Technical support\n2. FAQ\n[+] Choose one: ");
        String userChoice = sc.nextLine();
        switch (userChoice)
        {
            case "1" -> faq(sc);
            case "2" -> technicalSupport(sc);
            default -> System.out.println("[-] No such option");
        }
    }
    public static void changePassword(Scanner sc, Account user) throws IOException
    {
        System.out.print("[+] Enter current password: ");
        String currentPassword = sc.nextLine();
        if (user.verifyPassword(currentPassword))
        {
            System.out.print("[+] Enter new password: ");
            String newPassword = sc.nextLine();
            if (user.setPassword(newPassword)){
                user.updateCredentials();
                System.out.println("[+] Successfully changed");
            }
        }
    }
    public static void logout()
    {
        System.out.println("Goodbye♥");
        System.exit(0);
    }

    public static void faq(Scanner sc)
    {
        System.out.println("FAQ");
    }

    public static void technicalSupport(Scanner sc)
    {
        System.out.println("Technical");
    }
}
