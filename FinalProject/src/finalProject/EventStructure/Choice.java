package finalProject.EventStructure;
/*
 * Author: Neumann Davila
 * Date:   Oct 7, 2022
 * Description:
 *
 *
 * 
 */

public class Choice {
	private String choiceDescription;
	private Consequence outcome;
		//	runs whatever effect is assigned to the react() method
	public void choiceRun() {
		System.out.println("");
		outcome.react();
	}
	
	public String toString() {
		return this.choiceDescription;
	}
		//	Collects info necessary for the Choice
	public Choice(String choiceDescription, Consequence effect) {
		this.choiceDescription = choiceDescription;
		this.outcome = effect;
	}
}

