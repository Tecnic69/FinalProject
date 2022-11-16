/*
 * Author: Neumann Davila
 * Date:   Nov 4, 2022
 * Description:
 * An extension of the Character class
 * 		- Overrides certain functions to prevent user prompt
 * 		- Implements FriendStats for custom interactions and outcomes
 *
 * 
 */

package finalProject.CharacterTypes;

import java.util.ArrayList;

import finalProject.TextGame;
import finalProject.EventStructure.Event;
import finalProject.Items.Item;
import finalProject.Items.Weapon;

public class NPC extends Character {

	private ArrayList<String> positiveReacts = new ArrayList<String>();
	private ArrayList<String> neutralReacts = new ArrayList<String>();
	private ArrayList<String> negativeReacts = new ArrayList<String>();
	private ArrayList<Event> sideQuests = new ArrayList<Event>();
	private int interactions = 0;
	private int giftsRecieved = 0;
	
								//	----Interaction---	\\
	
	public void addDialogue(String newDialogue, int type) {
		if(type == 1) {
			positiveReacts.add(newDialogue);
		}
		else if(type == 0) {
			neutralReacts.add(newDialogue);
		}
		else {
			negativeReacts.add(newDialogue);
		}
	}
	
	public String getDialogue(Stat friendStat) {
		
		if(interactions < 3) {
			TextGame.player.getStats().adjustFriendStat(friendStat, 5);
			interactions++;
			System.out.println("" + friendStat);
		}
		
		int friendValue = friendStat.getStat();
		
		if(friendValue > 75) {
			return positiveReacts.get(rand.nextInt(positiveReacts.size()));
		}
		else if(friendValue > 25) {
			return neutralReacts.get(rand.nextInt(neutralReacts.size()));
		}
		else {
			return negativeReacts.get(rand.nextInt(negativeReacts.size()));
		}
	}
	
	public void addSideQuest(Event newQuest) {
		sideQuests.add(newQuest);
	}
	
								//	---Misc---	\\
	
	public void recieveItem(Stat friendStat, Item item) {
		addItem(item);
		
		if (giftsRecieved < 2) {
			TextGame.player.getStats().adjustFriendStat(friendStat, 15);
			giftsRecieved++;
		}
	}
	
	@Override public void attack(Character enemy) {
		int damage = getEquippedWeapon().attack();
		if(damage > 0) {
			int extra = rand.nextInt(getStats().getStrength().getStat());
			
			enemy.adjustHealth(-(damage + extra));
			
			System.out.println("The " + getName() + " did " + damage + " damage to you.");
		}
		else {
				System.out.println(getName() + " missed!");
		}
	}
		
	@Override public void EquipWeapon() {
		if(getWeapons().size() == 0) {
			setEquippedWeapon(new Weapon());
		}
		else if (getWeapons().size() == 1){
			setEquippedWeapon(getWeapons().get(0));
		}
		else {
			//	cycling through weapons or attack type
		}
	}
	
	@Override public void displayDeathEvent() {
		TextGame.player.getStats().addXp(getStats().getXp().getStat());
		System.out.println("test: Death");
		getDeathEvent().displayEvent();
	}
	
	
	public NPC() {
		super();
	}
	
	public NPC(String name, int health, int xp) {
		this.name = name;
		adjustMaxHealth(health);
		this.stats.getXp().setStat(xp);
	}
}

