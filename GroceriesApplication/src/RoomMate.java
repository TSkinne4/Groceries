import java.io.Serializable;
import java.text.NumberFormat;

/* ****************
 *  IMPORTANT
 * ***************
 * Any change to this file should be followed by a running of Test.java as MainApplication.java will be unable to recognize the
 * contents of RoomMateInformationFile.txt as an ArrayList of type RoomMate. If any information is being stored, make sure to write it down.
 */ 

public class RoomMate implements Serializable {
	
	String name;
	double contribution, difference, portion;
	NumberFormat usdFormat = NumberFormat.getCurrencyInstance();
	
	RoomMate(String name){
		this.name = name;
		contribution = 0;
		difference = 0;
		portion = 0;
	}
	
	void addContribution(double newContribution) {
		contribution+=newContribution;
	}
	
	void addContribution(int newContribution) {
		contribution+=(double)newContribution;
	}
	
	void setPortion(double portion) {
		this.portion = portion/100.0;
	}
	
	double getContribution() {
		return contribution;
	}
	
	double getDifference() {
		return difference;
	}
	
	void setContribution(double newContribution) {
		contribution = newContribution;
	}

	void setDifference(double newDifference) {
		difference = newDifference;
	}
	
	String getName() {
		return name;
	}
	
	public String toString() {
		return "Name: " + name + "\nContribution: " + usdFormat.format(contribution) +"\nPortion: "
				  + String.format("%.2f", portion*100) + "%\nDifference: " + usdFormat.format(difference);
	}
	
	void calculateDifference(double total) {
		difference = total * portion - contribution;
	}
	
	void payDifference(double pay) {
		difference-=pay;
	}
	
	void recieveDifference(double pay) {
		difference+=pay;
	}
}
