/*
 * Author: Neumann Davila
 * Date:   Nov 1, 2022
 * Description:
 * Controls the managment of every stat for any character
 *
 * 
 */

package finalProject.CharacterTypes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import finalProject.TextGame;
import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;

public class StatManager {
	Scanner input = new Scanner(System.in);
	Random rand = new Random();
	
	private ArrayList<Stat> friendStats = new ArrayList<Stat>();
	
	private Stat strength = new Stat("Strength");
		/*	
		 * Strength controls combat scenarious	
		 * 		- add or subtract Combat Damage	
		 * 		- add health
		 * 		!---More Ideas---!
		 */
	private Stat dexterity = new Stat("Dexterity");
		/*
		 * Dexterity controls any movement outside of combat
		 * 		- Stealth related actions
		 * 		- movement speeds
		 * 		- funky movements
		 */
	private Stat charisma = new Stat("Charisma");
		/*
		 * Charisma controls how a player interacts with NPC's
		 * 		- Persuade NPC's
		 * 		- Controls an NPC's response towards the player
		 */
	private Stat xp = new Stat("Experience Points");
	private int level;
	private int totalStatPoints;
	
	public String toString() {
		return "" + strength + "\n" + dexterity + "\n" + charisma + "\n";
	}
	
	public void clearStats() {
		this.strength.setStat(1);
		this.dexterity.setStat(1);
		this.charisma.setStat(1);
	}
	
	public void setStats(int str, int cha, int dex) {
		this.strength.setStat(str);
		this.charisma.setStat(cha);
		this.dexterity.setStat(dex);
	}
	
	
	public void setStats() {
		
		int tempStr = strength.getStat();
		int tempCha = charisma.getStat();
		int tempDex = dexterity.getStat();
		
		while(totalStatPoints > 0) {
			Event setStats = new Event("You have " + totalStatPoints + " stat points\nPlease chose which stat you would like to increase", false);
			
			setStats.addChoice(new Choice("" + strength, () -> {System.out.println("How many points would you like to add to this stat?");int tempInt = input.nextInt();if(tempInt <= totalStatPoints && tempInt > 0) {strength.adjustStat(tempInt);totalStatPoints -= tempInt;} else{System.out.println("You can not add " + tempInt + " stat points.");}}));
			setStats.addChoice(new Choice("" + dexterity, () -> {System.out.println("How many points would you like to add to this stat?");int tempInt = input.nextInt();if(tempInt <= totalStatPoints && tempInt > 0) {dexterity.adjustStat(tempInt);totalStatPoints -= tempInt;} else{System.out.println("You can not add " + tempInt + " stat points.");}}));
			setStats.addChoice(new Choice("" + charisma, () -> {System.out.println("How many points would you like to add to this stat?");int tempInt = input.nextInt();if(tempInt <= totalStatPoints && tempInt > 0) {charisma.adjustStat(tempInt);totalStatPoints -= tempInt;} else{System.out.println("You can not add " + tempInt + " stat points.");}}));
			setStats.addChoice(new Choice("Reset to previous stats", () -> {totalStatPoints +=(strength.getStat() - tempStr);totalStatPoints +=(dexterity.getStat() - tempDex);totalStatPoints +=(charisma.getStat() - tempCha);strength.setStat(tempStr);dexterity.setStat(tempDex);charisma.setStat(tempCha);}));
			
			setStats.displayEvent();
		}
		Event confirmStats = new Event("Are these the stats you wish to keep?\n" + toString(), false);
		
		confirmStats.addChoice(new Choice("Confirm Stats", () -> {TextGame.player.adjustMaxHealth(strength.getStat() - tempStr);}));
		confirmStats.addChoice(new Choice("Reset Stats", () -> {totalStatPoints +=(strength.getStat() - tempStr);totalStatPoints +=(dexterity.getStat() - tempDex);totalStatPoints +=(charisma.getStat() - tempCha);strength.setStat(tempStr);dexterity.setStat(tempDex);charisma.setStat(tempCha);setStats();}));
		
		confirmStats.displayEvent();
	}
	
		public void resetGame() {
			totalStatPoints = 5;
			clearStats();
			setStats();
		}
		
								//	---Strength Methods---	\\
		
		public Stat getStrength() {
			return this.strength;
		}
		
		public boolean rollStrength(Stat opposingStat) {
			return strength.rolllStat(opposingStat);
		}
		
								//	---Dexterity Methods---\\\
		
		public Stat getDexterity() {
			return this.dexterity;
		}
		
		public boolean rollDexterity(Stat opposingDexterity) {
			return dexterity.rolllStat(opposingDexterity);
		}
		
								//	---Charisma Methods--	\\
		
		public Stat getCharisma() {
			return this.charisma;
		}
		
		public boolean rollCharisma(Stat opposingCharisma) {
			return charisma.rolllStat(opposingCharisma);
		}
		
								//	---Xp Methods---\\
		
		public Stat getXp() {
			return this.xp;
		}
		
		public void addXp(int adjustAmount) {
			System.out.println("You gain " + adjustAmount + " xp\n");
			xp.adjustStat(adjustAmount);
			
			if(xp.getStat() >= (level + 1) * 10) {
				levelUp();
				xp.adjustStat((level + 1) * -10);
			}
		}
		
		public void levelUp() {
			level++;
			System.out.println("You are now level " + level);
			totalStatPoints += 2;
			setStats();
			
		}
		
								//	---Friend Stats---	\\
		
			//	detects if the player has ever interacted with the NPC and creates a stat if there isn't already one 
		public Stat getFriendStat(NPC npc) {
			for(int i = 0; i < friendStats.size(); i++) {
				if(("" + npc).equals("" + friendStats.get(i).getName())) {
					return friendStats.get(i);
				}
			}
		
				friendStats.add(new Stat("" + npc, 26));
				return friendStats.get(friendStats.size() - 1);
		}
		
		public void adjustFriendStat(Stat friendStat, int adjustment) {
			friendStat.adjustStat(adjustment + rand.nextInt(charisma.getStat()));
		}
		
								//	---Constructors---	\\
		
		public StatManager() {
			this.level = 1;
			this.strength.adjustStat(1);
			this.dexterity.adjustStat(1);
			this.charisma.adjustStat(1);
		}
		
}

