//locate file in the package
package Menu;

//import local class

import Database.Account;

//import java classes
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//implements landing part of "Login" flowchart
public class Landing
{
    //because scanner has same input source we pass it as an argument to avoid NoSuchElementException

    /**
     * Registers new user
     *
     * @param user instance of the Account class
     * @param sc   is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     * @return success of the registration
     */
    public static boolean register(Account user, Scanner sc) throws IOException
    {
        user.setUserId();

        System.out.print("[+] Enter email: ");
        String email = sc.nextLine();
        if (!user.setEmail(email))
        {
            return false;
        }

        System.out.print("[+] Enter username: ");
        String username = sc.nextLine();
        if (!user.setUsername(username))
        {
            return false;
        }

        System.out.print("[+] Enter password: ");
        String password = sc.nextLine();
        if (!user.setPassword(password))
        {
            return false;
        }

        System.out.print("[+] Verify password: ");
        String newPassword = sc.nextLine();
        if (!user.verifyPassword(newPassword))
        {
            return false;
        }

        System.out.print("[+] Choose sex (m,f): ");
        String sex = sc.nextLine();
        if (!(sex.equals("m") || sex.equals("f")))
        {
            System.out.println("[-] Incorrect input");
            return false;
        }
        user.setSex(sex);

        user.saveAccountToFile();
        return true;
    }

    /**
     * Login to existing account
     *
     * @param user instance of the Account class
     * @param sc   is an instance of Scanner class. We pass it as argument to avoid NoSuchElementException.
     * @return success of the login
     */
    public static boolean login(Account user, Scanner sc) throws FileNotFoundException
    {
        System.out.print("[+] Enter email: ");
        String email = sc.nextLine();
        System.out.print("[+] Enter password: ");
        String password = sc.nextLine();
        //return result of the log in
        return user.loadAccountFromFile(email, password);
    }

}
