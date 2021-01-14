import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<RoomMate> list = new ArrayList<RoomMate>();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("C:/Users/trist/Documents/GitHub/Groceries/GroceriesApplication/RoomateInformationFile")));
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(new File("C:/Users/trist/Documents/GitHub/Groceries/GroceriesApplication/DebtsInformationFile")));
			oos.writeObject(list);
			oos2.writeObject(new double[1][1]);
		} catch (IOException e) {
			System.out.print("Error");
		}
		
	}
}
