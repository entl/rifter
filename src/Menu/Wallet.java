package Menu;

import Database.Account;
import Database.Transactions;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Wallet
{
    public static void purchaseHistory(Transactions transactions) throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> purchases = transactions.getPurchases();
        System.out.printf("%-8s %-8s %-8s\n", "ID", "Item ID", "Amount");
        for (HashMap<String, Object> purchase : purchases)
        {
            System.out.printf("%-8s %-8s %-8s\n", purchase.get("transactionId"), purchase.get("itemId"), purchase.get("amount"));
        }
    }

    public static void buyVRC()
    {
        
    }
}
