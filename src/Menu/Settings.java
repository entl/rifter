//locate file in package
package Menu;

//import locale class

import Database.Account;

//import java classes
import java.io.IOException;
import java.util.Scanner;

public class Settings
{
    /**
     * Display help page. Based on user choice display FAQ page or Technical support
     *
     * @param scannerInput is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     */
    public static void help(Scanner scannerInput)
    {
        System.out.print("\n1. Technical support\n2. FAQ\n3. Back\n[+] Choose one: ");
        String userChoice = scannerInput.nextLine();
        switch (userChoice)
        {
            case "1" -> technicalSupport();
            case "2" -> faq();
            case "3" ->
            {
                return;
            }
            default -> System.out.println("[-] No such option");
        }
    }

    /**
     * Allow user to change current password to another one.
     *
     * @param scannerInput is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     */
    public static void changePassword(Scanner scannerInput, Account user) throws IOException
    {
        System.out.print("\n[+] Enter current password: ");
        String currentPassword = scannerInput.nextLine();
        if (user.verifyPassword(currentPassword))
        {
            System.out.print("[+] Enter new password: ");
            String newPassword = scannerInput.nextLine();
            if (user.setPassword(newPassword))
            {
                user.updateCredentials();
                System.out.println("[+] Successfully changed");
            }
        }
    }

    /**
     * Exit from account
     */
    public static void logout()
    {
        System.out.println("\n[+] Goodbye♥");
        System.exit(0);
    }

    /**
     * Display simple FAQ page
     */
    public static void faq()
    {
        System.out.println("\n1. What is \"Rifter\"?\n\t\"Rifter\" application, which is a kind of symbiosis between customers and establishments. With the help of this application, various companies will be able to publish their products, thereby enabling consumers to buy them for the new VRC currency. ");
        System.out.println("2. How to play?\n\tTo play \"Rifter\" you should go back to the home page and choose map option. After that you will be able to explore city center of Cambridge and complete quests related to local history.");
    }

    /**
     * Display simple Technical Support page
     */
    public static void technicalSupport()
    {
        System.out.println("\n[+] If you have encountered with any problem please email to technical@besteverpossibleemaildomain.com. ");
    }
}
