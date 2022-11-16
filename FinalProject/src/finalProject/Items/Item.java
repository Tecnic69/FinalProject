/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 *
 *
 * 
 */

package finalProject.Items;

public class Item {
	protected String name;
	protected int amount;
	protected int price;
	protected boolean stackable;
	
	public String toString() {
		if(amount > 1) {
			return this.name + ": " + amount;
		}
		else {
			return this.name;
		}
	}
	
	public String getName() {
		return this.name;
	}
	public int getPrice() {
		return this.price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isStackable() {
		return stackable;
	}
	public int getAmount() {
		return this.amount;
	}
	
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void addAmount(int amount) {
		this.amount = amount;
	}
	
	public void adjustAmount(int amount) {
		this.amount += amount;
	}
	
	public Item() {
		this.name = "none";
		this.amount = 1;
		this.stackable = true;
	}
	
	public Item(String name, int price) {
		this.name = name;
		this.amount = 1;
		this.stackable = true;
	}
	
	public Item(String name, int amount, int price) {
		this.name = name;
		this.amount = amount;
		this.stackable = true;
	}
}

