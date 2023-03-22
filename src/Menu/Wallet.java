package Menu;

import Database.ItemsColumns;
import Database.Transactions;
import Database.TransactionsColumns;

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
        System.out.printf("\n%-8s %-16s %-8s %-10s %-14s %-8s\n", "ID", "Item Name", "Price", "Quantity", "Total Price", "Date");
        for (HashMap<String, Object> purchase : purchases)
        {
            System.out.printf("%-8s %-16s %-8s %-10s %-14s %-8s\n", purchase.get(TransactionsColumns.TRANSACTION_ID.name()), purchase.get(ItemsColumns.NAME.name()), purchase.get(ItemsColumns.PRICE.name()), purchase.get(TransactionsColumns.QUANTITY.name()), purchase.get(TransactionsColumns.AMOUNT.name()), purchase.get(TransactionsColumns.TRANSACTION_DATE.name()));
        }
    }

    public static void topUpHistory(Transactions transactions) throws FileNotFoundException
    {
        ArrayList<HashMap<String, Object>> topUps = transactions.getTopUps();
        System.out.printf("\n%-8s %-8s %-8s\n", "ID", "Amount", "Date");
        for (HashMap<String, Object> topUp : topUps)
        {
            System.out.printf("%-8s %-8s %-8s\n", topUp.get(TransactionsColumns.TRANSACTION_ID.name()), topUp.get(TransactionsColumns.AMOUNT.name()), topUp.get(TransactionsColumns.TRANSACTION_DATE.name()));
        }
    }

    public static void topUp(Transactions transactions, Scanner scannerInput) throws IOException
    {
        System.out.print("\n1. Card\n2. Apple Pay\n3. Back\n\n[+] Choose payment method ");
        String userChoice = scannerInput.nextLine();

        switch (userChoice)
        {
            case "1" -> transactions.topUpCard();
            case "2" -> transactions.topUpApplePay();
            case "3" ->
            {
            return;
            }
            default -> System.out.println("[-] No such option");
        }
    }

    public static void buyItem(Transactions transactions, Scanner scannerInput) throws IOException
    {
        System.out.printf("\n%-8s %-20s %-30s %-30s %-8s %-8s\n", "ID", "Company", "Name", "Description", "Price", "Stock");
        for (HashMap<String, Object> item:transactions.getItems())
        {
            System.out.printf("%-8s %-20s %-30s %-30s %-8s %-8s\n", item.get(ItemsColumns.ITEM_ID.name()), item.get(ItemsColumns.COMPANY_NAME.name()), item.get(ItemsColumns.NAME.name()), item.get(ItemsColumns.DESCRIPTION.name()), item.get(ItemsColumns.PRICE.name()), item.get(ItemsColumns.STOCK.name()));
        }
        System.out.println("\n[+] Enter 'q' to leave");
        System.out.print("[+] Enter item id: ");
        String itemId = scannerInput.nextLine();
        if (itemId.equals("q"))
            return;
        System.out.print("[+] Enter quantity: ");
        int quantity = scannerInput.nextInt();
        scannerInput.nextLine();
        transactions.purchaseItem(itemId, quantity);
    }
}
