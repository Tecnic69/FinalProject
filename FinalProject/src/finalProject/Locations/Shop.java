/*
 * Author: Neumann Davila
 * Date:   Nov 14, 2022
 * Description:
 *
 *
 * 
 */

package finalProject.Locations;

import java.util.ArrayList;
import java.util.Scanner;

import finalProject.TextGame;
import finalProject.EventStructure.*;
import finalProject.Items.Item;

public class Shop extends Location{
	private ArrayList<Item> forSale = new ArrayList<Item>();
	private Event shop;
	
	public void addItems(Item item) {
		forSale.add(item);
	}
	
	public void displayShop() {
		Event displayShop = new Event("What would you like to buy?");
		for(int i = 0; i < forSale.size(); i++) {
			Item tempItem = forSale.get(i);
			displayShop.addChoice(new Choice("" + forSale.get(i), () -> {TextGame.player.purchaseItem(tempItem);}));
		}
		displayShop.addChoice(new Choice("Back", () -> {}));
		displayShop.displayEvent();
		
		shop.displayEvent();
	}
	
	public void enterShop() {
		shop.displayEvent();
	}
	
	
	public Shop(){
		super();
	}
	
	public Shop(String shopName) {
		super(shopName);
		this.shop = new Event("You enter " + shopName);
		shop.addChoice(new Choice("Buy", () -> {displayShop();}));
		shop.addChoice(new Choice("Sell", () -> {}));
	}
}

