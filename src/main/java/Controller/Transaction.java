package src.main.java.Controller;

import src.main.java.Entities.Item;
import src.main.java.Entities.Order;
import src.main.java.Entities.OrderStorage;
import src.main.java.Entities.User;
import src.main.java.Use_cases.ItemManager;
import src.main.java.Use_cases.CartManager;
import src.main.java.Use_cases.OrderManager;
import src.main.java.Use_cases.UserManager;

import java.io.IOException;
import java.util.*;

/**
 * remember to generate a successful order
 */
public class Transaction {

    /**
     * Return whether a User is able to make a purchase. Make changes to involving Users' money if purchase made
     * successfully.
     * @param items - a list of Item(s) that the buyer wants to buy.
     * @param buyer -  a User who wish to make the purchase.
     * @return the True iff the buyer has enough money to purchase the Item(s); Otherwise return False.
     */
    /*public static boolean buy_item(ArrayList<Item> items, User buyer, User sellman){
        if (ItemManager.get_all_price(items) <= UserManager.getMoney(buyer)) {
            Order dingdan = OrderManager.create_order(items, buyer, sellman);
            OrderManager.addElement(dingdan);         //create order
            UserManager.subtractMoney(buyer, ItemManager.get_all_price(items));   //subtract buyer money
            UserManager.loadMoney(sellman, ItemManager.get_all_price(items));     //seller get money
            CartManager.remove_items(buyer, items);                   //remove the items in buyer's cart
            return true;
        }
        return false;
    }*/
    public static boolean buy_item(ArrayList<Item> items, User buyer){

        if (ItemManager.get_all_price(items) <= UserManager.getMoney(buyer)) {
            ArrayList<Item> copy_list = new ArrayList<>(items);
            Map<User, ArrayList<Item>> category= new HashMap<>();

            for (Item i: copy_list){
                ArrayList<Item> item = new ArrayList<>();
                item.add(i);
                User seller = ItemManager.getSeller(i);
                UserManager.loadMoney(seller, ItemManager.get_price(i));   //seller get money
                if(category.containsKey(seller)){category.get(seller).add(i);}
                else {category.put(seller, item);}
            }

            for(User u: category.keySet()){
                OrderManager.create_order(category.get(u), buyer, u); // create order with the same seller
            }

            UserManager.subtractMoney(buyer, ItemManager.get_all_price(items));   //subtract buyer money
            CartManager.remove_items(buyer, items);                   //remove the items in buyer's cart

            return true;
        }
        return false;
    }



    /**
     * Get the amount of money that a User has right now.
     * @param username - the User we want to know about.
     * Return a double representing the amount of money.
     */
    public static double getMoney(String username){
        return UserManager.getMoney(Objects.requireNonNull(UserManager.search(username)));
    }

    /**
     * Get all the Items stored.
     * Return a hash map containing information of all Items stored.
     */
    public static Map<String, ArrayList<Item>> getItems(){
        return ItemManager.getItems();
    }

    /**
     * Sell an Item.
     * @param item - the Item being sold.
     * Return nothing
     */
    public static void sell(Item item){
        ItemManager.addElement(item);
    }

    /**
     * Sell multiple Items.
     * @param items - the list of Items being sold.
     * Return nothing
     */
    public void sell(ArrayList<Item> items){
        ItemManager.addElement(items);
    }

    /**
     * Add money to a User.
     * @param u - the User we want to add money to.
     * @param money - the amount of money we wish to add.
     * Return nothing
     */
    public static void addMoney(User u, double money){
        UserManager.loadMoney(u, money);
    }
}


