/*
 * Author: Neumann Davila
 * Date:   Oct 4, 2022
 * TO DO:
 * 1.) Money System
 * 2.) Usable Items
 * 3.) Quests
 */

package finalProject;

import java.util.Scanner;

import finalProject.CharacterTypes.*;
import finalProject.CharacterTypes.Character;
import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.Ammunition;
import finalProject.Items.Weapon;
import finalProject.Locations.*;

public class TextGame {
	static Scanner input = new Scanner(System.in);
		//	Declared Ammunition - Ammunition("Name" , amount)
	static Ammunition arrows = new Ammunition("Arrows", 10);
	
		//	Declared Items - Weapons("Name" , minDamage, maxDamage, hit%)
	static Weapon cane = new Weapon("Cane", 1, 3, 35, 1);
	static Weapon oldSword = new Weapon("Old Sword", 5, 7 , 63, 1);
	static Weapon oldAxe = new Weapon("Old Axe", 8, 10, 45, 1);
	static Weapon oldBow = new Weapon("Old Bow", 6, 9, 55, arrows, 1);
	
		//	Main Player declaration
	public static Character player =  new Character();
		
								//	---Character Creation Methods---\\
	
		/* 
		 * NPC = Character("Name", health, XP gained)
		 * 		- Dialogue must be added within functions
		 * 				- AddDialogue("STRING", int)
		 * 					- int = 1: positiveReaction; 0: neutral; -1: negative
		 * 		- Stats must be added within function
		 * 		- Potential side quest must be added within the function
		 * 		- All Items must be added within the function
		 * 		
		 */
		
	public static NPC createOldMan() {
		NPC oldMan = new NPC("Old man", 6, 20);
		
		oldMan.addItem(cane);
		
		player.getStats().getFriendStat(oldMan).setStat(80);
		
		oldMan.addDialogue("I love you", 1);
		
		oldMan.addDialogue("Hello yourng wipper snapper", 0);
		oldMan.addDialogue("...Oh did you say something", 0);
		
		oldMan.addDialogue("I hate you", -1);	
		
		Event death = new Event("You see the corpse of the man you jsut killed\nWhat wold you like to do", false);
		death.addChoice(new Choice("Loot Body", () -> {}));
		death.addChoice(new Choice("Leave", () -> {}));
		oldMan.setDeathEvent(death);
				
		return oldMan;
	}
	
	
								//	---Location Creation Methods---	\\
	
		/* 
		 * Location name = new Location();
		 * 		- Must add Events & choices in method
		 * 		- location.nextEvent(eventIndex);
		 * 		- location.addEvent(Event);
		 * 
		 */
	static int forestIndex;
	
	
	
	public static Location createPrisonWall() {
		Location prisonWall = new Location();
		
		Event escape = new Event("You finally got over the wall unnoticed... for now.", false);
		escape.addChoice(new Choice("Search", () -> {System.out.println("You find nothing.");player.displayDeathEvent();}));
		escape.addChoice(new Choice("Wait", () -> {System.out.println("You wait and get captured by the men that kept you captive");player.displayDeathEvent();}));
		escape.addChoice(new Choice("Run", () -> {System.out.printf("You run and run for miles, until you finally see a forest in the distance.\nHopefully you will be able to hide in there.");forestIndex = 0;createForest();}));
		
		prisonWall.addEvent(escape);
		
		return prisonWall;
	}
	
	public static Location createForest() {
		Location forest =  new Location();
		Location[] nearbyLocations = {createPrisonWall()};
		
			//	Event Index 0
		Event enterCampsite = new Event("As you get deeper into the forest you find a campsite that was abandoned long ago.\nThere are an asortment of items left behind... hopefully one wants them back.\nYou find a journal and decide to write your name", false);
		enterCampsite.addChoice(new Choice("Write your name in your journal",() -> {System.out.println("Please type your name.");player.setName(input.nextLine());forest.nextEvent(1);}));
		
		forest.addEvent(enterCampsite);
			//	Event Index 1
		Event getWeapon = new Event("You also find an old Backpack with a...", false);
		getWeapon.addChoice(new Choice("Axe", () -> {player.addItem(oldAxe);forest.nextEvent(2);}));
		getWeapon.addChoice(new Choice("Sword", () -> {player.addItem(oldSword);forest.nextEvent(2);}));
		getWeapon.addChoice(new Choice("A bow with 10 arrows", () -> {player.addItem(oldBow);player.addItem(arrows);forest.nextEvent(2);}));
		
		forest.addEvent(getWeapon);
			//	Event Index 2
		Event test = new Event("test");
		test.addNPC(createOldMan());
		
		forest.addEvent(test);
		
		forest.setNearbyLocations(nearbyLocations);
		return forest;
	}
	
	
	public static void run() {
		player.setMaxHealth(20);
		player.getStats().resetGame();
		
		Event gameOver = new Event("You Died", false);
		gameOver.addChoice(new Choice("Restart Game", () -> {TextGame.run();}));
		gameOver.addChoice(new Choice("Quit", () -> {}));
		player.setDeathEvent(gameOver);
		
		
		createPrisonWall().nextEvent(0);
	}
	
	public static void main (String[] args) {
		run();
	}
	
}

