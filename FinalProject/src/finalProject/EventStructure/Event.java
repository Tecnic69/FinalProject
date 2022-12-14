/*
 * Author: Neumann Davila
 * Date:   Oct 7, 2022
 * Description:
 * Events Store multiple things necissary for the program
 * 		- Event description
 * 		- Choices custom to the event
 * 		- Default Events that can run in every event conditionaly
 * 		- NPC's within the event and the interactions that are possible with that npc
 * 			- This is build in so it is easier to return back to this event (may change)
 * 
 * 
 */

package finalProject.EventStructure;

import java.util.ArrayList;
import java.util.Scanner;

import finalProject.Items.Item;
import finalProject.TextGame;
import finalProject.CharacterTypes.*;
import finalProject.CharacterTypes.Character;

public class Event {
	private Scanner input = new Scanner(System.in);
	
	private String description;
	private ArrayList<Choice> eventChoices = new ArrayList<Choice>();
	private ArrayList<Character> eventNPC = new ArrayList<Character>();
	private boolean isDefault = true;
	
									//	---Display Methods---	\\
	
	public void displayEvent() {
			//	automatically runs if there is only one choice in the Event
		if(eventChoices.size() == 1) {
			System.out.println(description);
			eventChoices.get(0).choiceRun();
		}
		else {
				//	If the event has default choices then it will run starting at 0
			if(isDefault == true) {
				System.out.printf(description);
				System.out.println("");
				
				for (int i = 0; i < eventChoices.size();i++ ) {
					System.out.println(i + ": " + eventChoices.get(i));
				}
				try {
					int tempInt = Integer.parseInt(input.nextLine().strip());
					eventChoices.get(tempInt).choiceRun();
					}
					catch(Exception e){
						System.out.println("Invalid Input: Event");
						displayEvent();
					}
			}
				//	Choices will be run stating at 1 for ease of use
				//	only if default choices are not being used
			else {
				System.out.println(description);
				
				for(int i = 1; i < eventChoices.size() + 1; i++) {
					System.out.println(i + ": " + eventChoices.get(i - 1));
				}
				
				getDecision();
			}
		}
	}
	
		//	Inventory event built into every event object if the method is called
	public void inventoryEvent() {
		ArrayList<Choice> inventoryChoices = new ArrayList<Choice>();
		inventoryChoices.add(new Choice("Discard Item", () -> {TextGame.player.discardItem();displayEvent();}));
		inventoryChoices.add(new Choice("Change Equipped Weapon", () -> {TextGame.player.EquipWeapon();}));
		inventoryChoices.add(new Choice("Exit Inventory", () -> {displayEvent();}));
		
		TextGame.player.displayInventory();
		
		System.out.println("What would you like to do in your Inventory?");
		
		for(int i = 1; i < inventoryChoices.size() + 1;i++ ) {
			System.out.println(i + ": " + inventoryChoices.get(i - 1));
		}
		try {
			int tempInt = Integer.parseInt(input.nextLine().strip());
			inventoryChoices.get(tempInt - 1).choiceRun();
			}
			catch(Exception e){
				System.out.println("Invalid input: Inventory");
				inventoryEvent();
			}
		
		displayEvent();
	}
	
								//	---NPC Methods---	\\
	
	public void addNPC(NPC npc) {
		addChoice(new Choice("Interact with " + npc, () -> {NPCEvent(npc);displayEvent();}));
		eventNPC.add(npc);
	}
	
	public void removeNPC(NPC npc) {
		eventNPC.remove(npc);

		for(int i = 0; i < eventChoices.size(); i++) {
			if(("Interact with " + npc).equals("" + eventChoices.get(i))) {
				eventChoices.remove(i);
				break;
			}
		}
	}
	
	/*
	 * 							---Sub Events---
	 * 
	 * These are sub events built into every Event so that I can call and return to my old event
	 * 
	 * If I created a seperate event then I would need to keep track of the current event so that I can return to the event
	 * 
	 */
	
		//	Events that are created when an NPC is interacted with
	public void NPCEvent(NPC npc) {
		ArrayList<Choice> NPCChoices = new ArrayList<Choice>();
		Stat friendStat = TextGame.player.getStats().getFriendStat(npc);
		
		NPCChoices.add(new Choice("Talk to " + npc, () -> {System.out.println(npc.getDialogue(friendStat));}));
		NPCChoices.add(new Choice("Give something to " + npc, () -> {TextGame.player.giveItem(npc);}));
		NPCChoices.add(new Choice("Attack " + npc, () -> {combatEvent(npc);TextGame.player.getStats().adjustFriendStat(friendStat, -50);}));
		NPCChoices.add(new Choice("Pickpocket " + npc, () -> {npc.pickPocket(TextGame.player);TextGame.player.getStats().adjustFriendStat(friendStat, -20);}));
		NPCChoices.add(new Choice("Back", () -> {}));
		
		for (int i = 1; i < NPCChoices.size() + 1;i++ ) {
			System.out.println(i + ": " + NPCChoices.get(i - 1));
		}
		
		try {
			int tempInt = Integer.parseInt(input.nextLine().strip());
			NPCChoices.get(tempInt - 1).choiceRun();
			
			}
			catch(Exception e){
				System.out.println("Invalid input: NPC");
				NPCEvent(npc);
			}	
		}
	
		//	Method that runs combat Events with NPC's
	
	public void combatEvent(NPC enemy) {
		ArrayList<Choice> combatChoices = new ArrayList<Choice>();
		combatChoices.add(new Choice("Attack: " + TextGame.player.getEquippedWeapon(), () -> {TextGame.player.attack(enemy);enemy.attack(TextGame.player);}));
		combatChoices.add(new Choice("Switch Weapons", () -> {TextGame.player.EquipWeapon();combatEvent(enemy);}));
		combatChoices.add(new Choice("Use your surroundings", () -> {}));
		combatChoices.add(new Choice("Run", () -> {if(TextGame.player.getStats().rollDexterity(enemy.getStats().getDexterity())) {System.out.println("You ran");displayEvent();} else {System.out.println("Failed"); enemy.attack(TextGame.player);}}));

			//while both the enemy and the player have over 0 health
		while(TextGame.player.getHealth() > 0 && enemy.getHealth() > 0) {
			System.out.println("\nHealth: " + TextGame.player.healthBar() + "\n" + enemy + ": " + enemy.getHealth());
					
			for (int i = 1; i < combatChoices.size() + 1;i++ ) {
				System.out.println(i + ": " + combatChoices.get(i - 1));
			}
			
			try {
				int tempInt = Integer.parseInt(input.nextLine().strip());
				combatChoices.get(tempInt - 1).choiceRun();
			}
			catch(Exception e){
					System.out.println("Invalid input: Combat");
					removeNPC(enemy);
			}	
		}
		
		if(TextGame.player.getHealth() <= 0) {
			TextGame.player.displayDeathEvent();
		}
		else {
			System.out.println("\nYou killed the " + enemy);
			enemy.displayDeathEvent();
			removeNPC(enemy);
		}
		
	}
	
	
									//	---Choice Methods---  \\

	public void addChoice(Choice choice) {
		this.eventChoices.add(choice);
	}
		//	Collects and runs the decision for the event 
	public void getDecision() {
		try {
		int tempInt = Integer.parseInt(input.nextLine().strip());
		eventChoices.get(tempInt - 1).choiceRun();
		}
		catch(Exception e){
			System.out.println("Invalid input: Decision");
			displayEvent();
		}	
	}
	
									//	---Constructors---	\\
	
	public Event() {
		this.description = "test";
		eventChoices.add(new Choice("Show Inventory", () -> {inventoryEvent();}));
		addNPC(new NPC());
	}
	
	public Event(String description) {
		this.description = description;
		eventChoices.add(new Choice("Show Inventory", () -> {inventoryEvent();}));
	}
		//	Special Constructor for events without default choice like Display Inventory
	public Event(String description, boolean containsDefaultChoices) {
		this.description = description;
		this.isDefault = containsDefaultChoices;
	}
	
	public Event(String description, Item triggerItem) {
		this.description = description;
	}

}

