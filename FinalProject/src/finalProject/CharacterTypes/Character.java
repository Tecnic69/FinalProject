/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 * Stores Player Data --> Name; Health; Inventory; StatManger
 *
 * 
 */

package finalProject.CharacterTypes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.*;


public class Character {
	Scanner input = new Scanner(System.in);
	Random rand = new Random();
	
	protected String name;
	protected int maxHealth = 0;
	protected int tempHealth;
		//	Stat Manager
	protected StatManager stats = new StatManager();
		//	inventory variables
	protected int lastEmptyCell = 0;
	protected Item[] inventory = new Item[8];
	protected ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	protected Weapon equippedWeapon = null;	
	protected int money = 0;
		//	death events
	protected Event death;
		
										//	---MISC---  \\
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public StatManager getStats() {
		return stats;
	}	
	
	
								//	---Health Methods---  \\
	
	public String healthBar() {
		return tempHealth + " / " + maxHealth;
	}
	
	public void adjustMaxHealth(int adjustment) {
		maxHealth += adjustment;
		tempHealth = maxHealth;
	}
	public void setMaxHealth(int health) {
		this.maxHealth = health;
		this.tempHealth = maxHealth;
	}
	
	public void setHealth(int health) {
		this.tempHealth = health;
	}
	
	public void adjustHealth(int userIn) {
		this.tempHealth += userIn;
	}
	
	public int getHealth() {
		return this.tempHealth;
	}
	
	public void attack(Character enemy) {
		int damage = equippedWeapon.attack();
		if(damage > 0) {
			int extra = rand.nextInt(stats.getStrength().getStat());
			
			enemy.adjustHealth(-(damage + extra));
			
			System.out.println("You do " + damage + " + " + extra +  " damage to the " + enemy + ".");
			
		}
		else {
			System.out.println("You miss!");
		}	
	}
	
	public void setDeathEvent(Event event) {
		this.death = event;
	}
	public Event getDeathEvent() {
		return this.death;
	}
	
	public void displayDeathEvent() {
		death.displayEvent();
	}
	
								//	---Weapon Methods---  \\
	
	public Item getItem(int index) {
		return inventory[index];
	}
	
	public Weapon getEquippedWeapon() {
		return this.equippedWeapon;
	}
	
	public ArrayList<Weapon> getWeapons() {
		return this.weapons;
	}
	
	public void setEquippedWeapon(Weapon weapon) {
		equippedWeapon = weapon;
	}
	
	public void EquipWeapon() {

		if(weapons.size() == 0) {
			equippedWeapon = new Weapon();
		}
		else {
			for(int i = 0; i < weapons.size(); i++) {
				System.out.println((i + 1) + ": " + weapons.get(i));
			}
			System.out.println("Choose the weapon you would like to equip:");
			equippedWeapon = weapons.get(input.nextInt() - 1);
		}
	}
	
								//	---Inventory Methods---  \\
	
		
	
	
	public void displayInventory() {
		for (int i = 0; i < lastEmptyCell; i++) {
			System.out.println(1 + i + ": " + this.inventory[i]);
		}
		
			//	---print something fancy in empty inventory slots---
	}
	
	public void discardItem() {
		displayInventory();
		System.out.println("What Item would you like to discard?\n9: Exit");
		int discardIndex = input.nextInt();
		
		if(discardIndex < 9) {
			Event confirmDiscard = new Event("Are you sure you want to discard:\n" + inventory[discardIndex - 1], false);
			confirmDiscard.addChoice(new Choice("Yes", () -> {removeItem(discardIndex);}));
			confirmDiscard.addChoice(new Choice("No", () -> {}));
			confirmDiscard.displayEvent();
			
		}
		else if(discardIndex == 9) {
			
		}
		else {
			System.out.println("Invalid Input");
			discardItem();
		}
	}
	
	public void giveItem(NPC recipiant) {
		displayInventory();
		System.out.println("What Item would you like to give to " + recipiant + "?\n9: Exit");
		int discardIndex = input.nextInt();
		
		if(discardIndex < 9){
			Item tempItem = inventory[discardIndex - 1];
			
			Event confirmGive = new Event("Are you sure you want to give:\n" + tempItem, false);
			confirmGive.addChoice(new Choice("Yes", () -> {recipiant.recieveItem(stats.getFriendStat(recipiant), tempItem);removeItem(discardIndex);}));
			confirmGive.addChoice(new Choice("No", () -> {}));
			
			confirmGive.displayEvent();
		}
		else if(discardIndex == 9) {
			
		}
		else {
			System.out.println("Invalid Input: Give");
			giveItem(recipiant);
		}
	}
	
	public void pickPocket(Character recipiant) {
			//	finalize stat system idea
			//	create an if statement that allows for a chance of failure
		if(recipiant.getStats().rollDexterity(this.stats.getDexterity()) == true) {
			displayInventory();
			System.out.println("What would you like to take?\n9: Exit");
			int stealIndex = input.nextInt();
			
			if(stealIndex < 9 && stealIndex > 0) {
				recipiant.addItem(inventory[stealIndex - 1]);
				removeItem(stealIndex);
				
			}
			else if (stealIndex == 9) {
				
			}
			else {
				System.out.println("Invalid Input: Steal");
				pickPocket(recipiant);
			}
		}
		else {
			System.out.println("Failed");
		}
	}
	
	//	Adds a general item to the inventory
	public void addItem(Item newItem) {
		
		if(!newItem.isStackable()) {
			if(lastEmptyCell < 8) {
				this.inventory[this.lastEmptyCell] = newItem;
				this.lastEmptyCell += 1;
		
				
				if(newItem instanceof Weapon) {
					weapons.add((Weapon) newItem);
					if(weapons.size() == 1) {
						equippedWeapon = (Weapon) newItem;
					}
				}
				
			}
			else {
				System.out.println("You have no space in your inventory!");
			}
		}
		else {
			for(int i = 0; i < lastEmptyCell; i++) {
				if(newItem.getName().equals(inventory[i].getName())) {
					inventory[i].adjustAmount(newItem.getAmount());;
					break;
				}
			}
		}
	}
	
	public void removeItem(int index) {
		if(inventory[index - 1] instanceof Weapon) {
			if(this.equippedWeapon == inventory[index - 1]) {
				weapons.remove(inventory[index - 1]);
				inventory[index - 1] = null;
				EquipWeapon();
			}
			else {
				weapons.remove(inventory[index - 1]);
				inventory[index - 1] = null;
			}
		}
		else {
			inventory[index - 1] = null;
		}
		
		for(int i = 0; i < lastEmptyCell; i++) {
			if (inventory[i] == null) {
				inventory[i] = inventory[i + 1];
				inventory[i + 1] = null;
			}
		}
		lastEmptyCell--;
	}
	
								//	---Money Methods---	\\
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public void adjustMoney(int money) {
			this.money += money;
	}
	
	public void purchaseItem(Item item) {
		if (item.getPrice() > this.money) {
			System.out.println("You do not have enough money for this");
		}
		else {
			adjustMoney(-(item.getPrice()));
			addItem(item);
		}
	}
	
								//	---Constructors---  \\
	
	public Character() {
		this.stats.getXp().setStat(0);
	}
	
}

