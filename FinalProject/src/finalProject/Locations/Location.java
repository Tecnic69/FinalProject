/*
 * Author: Neumann Davila
 * Date:   Oct 4, 2022
 * Description:
 * This program is going to create location objects so that I can access them in my text adventure game
 * 
 * 
 */

package finalProject.Locations;

import java.util.ArrayList;

import finalProject.CharacterTypes.NPC;
import finalProject.EventStructure.*;

public class Location {
	protected String name;
	protected ArrayList<Event> locationEvents =  new ArrayList<Event>();
	protected NPC[] LocationNPCs;
	protected int eventIndex;
	
	public void setNearbyLocations(Location[] nearbyLocations) {
		for(int i = 0; i < locationEvents.size(); i++) {
			for(int j = 0; j < nearbyLocations.length; j++) {
				locationEvents.get(i).addChoice(new Choice("Go to " + nearbyLocations[j], () -> {}));
			}
		}
	}
	
		 // Method displays information when the character arrives at this location
	public void addEvent(Event newEvent) {
		locationEvents.add(newEvent);
	}
		//	create a way to get the next event by yourself loser
	public void nextEvent(int eventNum) {
		locationEvents.get(eventNum).displayEvent();
	}
		
	public Event getEvent(int eventNum) {
		return locationEvents.get(eventNum);
	}
	
		//	Object Constructor
	public Location() {
	}
	
	public Location(String name) {
		this.name = name;
	}

}

