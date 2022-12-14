/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 * Used to create WeaponObjects 
 * 
 * 
 */

package finalProject.Items;

import java.util.Random;

public class Weapon extends Item {
	private int damageMin;
	private int damageMax;
	private int hitChance;
	private boolean isRanged = false;
	private Ammunition ammo;
	
	@Override public String toString() {
		String summary = "";
		
		summary += ":\n";
		summary += "     Damage: " + damageMin + " - " + damageMax + "\n";
		summary += "     Hit Chance: " + hitChance + "%\n";
		
		
		if (isRanged == true) {
			summary += "     Ammo: " + ammo.getName() + "\n";
		}
		
		summary += "     Price: " + getPrice() + " coins";
		
		return (super.toString() + summary);
	}
		//	creates an attack using random values between the min and max
	public int attack() {
		Random rand = new Random();
		int hitPerc = rand.nextInt(99);
		
		if(hitPerc < hitChance - 1){
			if (isRanged == false) {
				return rand.nextInt(damageMax - damageMin) + damageMin;
			}
			else {
//				if (ammo == true) {
//					return rand.nextInt(damageMax - damageMin) + damageMin;
//				}
//				else {
//					System.out.println("You do not have any ammo for that weapon.");
					return 0;
//				}
			}
		}
		else {
			return 0;
		}
	}
	
											//	---Constructors---  \\
	public Weapon() {
		this.name = "Fist";
		this.damageMax = 2;
		this.damageMin = 1;
		this.hitChance = 10;
		this.stackable = false;
	}
		
	public Weapon(String name, int damageMin, int damageMax, int hitChance, int price) {
		this.name = name;
		this.price = price;
		this.damageMin = damageMin;
		this.damageMax = damageMax;
		this.hitChance = hitChance;
		this.stackable = false;
	}

	public Weapon(String name, int damageMin, int damageMax, int hitChance, Ammunition ammo, int price) {
		this.name = name;
		this.price = price;		
		this.damageMin = damageMin;
		this.damageMax = damageMax;
		this.hitChance = hitChance;
		this.isRanged = true;
		this.ammo = ammo;
		this.stackable = false;
	}
	
	
}

