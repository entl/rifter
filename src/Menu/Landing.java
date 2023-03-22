package Menu;

import Database.Account;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Landing
{
    //because scanner has same input source we pass it as an argument to avoid NoSuchElementException
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
