package Menu;

import Database.Transactions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Wallet
{
    public static void purchaseHistory(Transactions transactions) throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> purchases = transactions.getPurchases();
        System.out.printf("\n%-8s %-16s %-8s %-8s %-8s\n", "ID", "Item Name", "Price", "Quantity", "Total Price");
        for (HashMap<String, Object> purchase : purchases)
        {
            System.out.printf("%-8s %-16s %-8s %-8s %-8s\n", purchase.get("transactionId"), purchase.get("itemName"), purchase.get("itemPrice"), purchase.get("quantity"), purchase.get("amount"));
        }
    }

    public static void topUpHistory(Transactions transactions) throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> topUps = transactions.getTopUps();
        System.out.printf("\n%-8s %-8s %-8s\n", "ID", "Amount", "Date");
        for (HashMap<String, Object> topUp : topUps)
        {
            System.out.printf("%-8s %-8s %-8s\n", topUp.get("transactionId"), topUp.get("amount"), topUp.get("transactionDate"));
        }
    }

    public static void topUp(Transactions transactions, Scanner scannerInput) throws IOException
    {
        System.out.print("\n1. Card\n2. Apple Pay\n\n[+] Choose payment method ");
        String userChoice = scannerInput.nextLine();

        switch (userChoice)
        {
            case "1" -> transactions.topUpCard();
            case "2" -> transactions.topUpApplePay();
        }
    }

    public static void buyItem(Transactions transactions, Scanner scannerInput) throws IOException
    {
        System.out.printf("\n%-8s %-20s %-30s %-30s %-8s %-8s\n", "ID", "Company", "Name", "Description", "Price", "Stock");
        for (HashMap<String, Object> item:transactions.getItems())
        {
            System.out.printf("%-8s %-20s %-30s %-30s %-8s %-8s\n", item.get("itemId"), item.get("company"), item.get("name"), item.get("description"), item.get("price"), item.get("stock"));
        }
        System.out.print("\n[+] Enter item id: ");
        String itemId = scannerInput.nextLine();
        System.out.print("[+] Enter quantity: ");
        int quantity = scannerInput.nextInt();
        scannerInput.nextLine();
        transactions.purchaseItem(itemId, quantity);
    }
}
