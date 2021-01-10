import java.io.Serializable;

public class RoomMate implements Serializable {

	
	String name;
	double contribution;
	
	RoomMate(String name){
		this.name = name;
		contribution = 0;
	}
	
	void addContribution(double newContribution) {
		contribution+=newContribution;
	}
	
	String getName() {
		return name;
	}
}