import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApplication extends Application{

	Stage stage;
	ArrayList<RoomMate> matesList;
	File informationFile = new File("C:/Users/trist/Documents/GitHub/Groceries/GroceriesApplication/RoomateInformationFile");
	Button menuButton = new Button("Return to menu");
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage stage) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(informationFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			matesList = (ArrayList<RoomMate>)ois.readObject();
			ois.close();
			fis.close();
		} catch(FileNotFoundException e) {
			matesList = new ArrayList<RoomMate>();
		}
		catch(ClassNotFoundException e) {
			matesList = new ArrayList<RoomMate>();
		}
		menuButton.setOnAction(e->stage.setScene(new Scene(new MainMenu())));
		this.stage = stage;
		stage.setScene(new Scene(new MainMenu()));
		stage.show();
		
	}
	
	private void save() {
		try {
			FileOutputStream fos = new FileOutputStream(informationFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(matesList);
			fos.close();
			oos.close();
		}
		catch(FileNotFoundException e) {} 
		catch (IOException e) {}
	}
	
	//the main menu
	private class MainMenu extends VBox{
		MainMenu(){
			Button addRoomateButton = new Button("Add roomate"), addContributionButton = new Button("Add Contribution"), saveButton = new Button("Save");
			addRoomateButton.setOnAction(e-> stage.setScene(new Scene(new AddRoomateScreen())));
			saveButton.setOnAction(e->save());
			this.getChildren().addAll(addRoomateButton, addContributionButton, saveButton);
			this.setSpacing(5);
		}
	}
	
	private class AddRoomateScreen extends VBox{
		AddRoomateScreen(){
			TextField nameField = new TextField();
			Button addThisRoomateButton = new Button("Add roomate");
			addThisRoomateButton.setOnAction(e->{
				if(nameField.getText().equals(""));
				else {
					String name = nameField.getText();
					try {
						if(matesList.size()!=0&&matesList.contains(new RoomMate(name)));
						else
							matesList.add(new RoomMate(name));
					} catch(NullPointerException exception) {
						matesList.add(new RoomMate(name));
					}
				}		
			});
			this.getChildren().addAll(new Text("Enter new roomate"),nameField,addThisRoomateButton,menuButton);
			this.setSpacing(5);	
		}
	}

} //emd of Main Application