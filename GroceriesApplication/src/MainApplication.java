
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApplication extends Application{

	Stage stage;
	ArrayList<RoomMate> matesList;
	File informationFile = new File("C:/Users/trist/Documents/GitHub/Groceries/GroceriesApplication/RoomateInformationFile"), debtsFile = new File("C:/Users/trist/Documents/GitHub/Groceries/GroceriesApplication/DebtsInformationFile");
	Button menuButton = new Button("Return to menu");
	Text alertText = new Text("");
	NumberFormat usdFormat = NumberFormat.getCurrencyInstance();
	double[][] debtsInformationArray;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	//Starts the program
	public void start(Stage stage) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(informationFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			matesList = (ArrayList<RoomMate>)ois.readObject();
			ois.close();
			fis.close();
			ois = new ObjectInputStream(new FileInputStream(debtsFile)); 
			debtsInformationArray = (double[][])ois.readObject();
			ois.close();
		} catch(FileNotFoundException e) {
			matesList = new ArrayList<RoomMate>();
		}
		catch(ClassNotFoundException e) {
			matesList = new ArrayList<RoomMate>();
		}
		menuButton.setOnAction(e->stage.setScene(new Scene(MainMenu())));
		this.stage = stage;
		stage.setScene(new Scene(MainMenu()));
		stage.setTitle("Groceries Calculator");
		stage.show();
		
	}
	
	//saves the data from the room mates to a file
	private void save() {
		try {
			FileOutputStream fos = new FileOutputStream(informationFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(matesList);
			fos.close();
			oos.close();
			oos = new ObjectOutputStream(new FileOutputStream(debtsFile));
			oos.writeObject(debtsInformationArray);
			oos.close();
		}
		catch(FileNotFoundException e) {} 
		catch (IOException e) {}
	}
	
	//the main menu
	VBox MainMenu(){
		Text mainTitle = new Text("Groceries Calculator");
		mainTitle.setFont(new Font("Helvetica", 50));
		
		alertText.setText("");
		
		VBox result = new VBox();
		Button addRoomateButton = new Button("Add roomate"), addContributionButton = new Button("Calculate totals"), saveButton = new Button("Save");
		Button infoButton = new  Button("Add Contribution"), customButton = new Button("Set custom portions"), paymentButton = new Button("Make Payment");
		
		
		
		addRoomateButton.setOnAction(e-> stage.setScene(new Scene(AddRoomateScreen())));
		saveButton.setOnAction(e->save());
		infoButton.setOnAction(e-> stage.setScene(new Scene( new EditRoomateScreen(-1))));
		customButton.setOnAction(e->stage.setScene(new Scene(setCustomPortions())));
		addContributionButton.setOnAction(e->stage.setScene(new Scene(calculateTotals())));
		paymentButton.setOnAction(e-> stage.setScene(new Scene(makePaymentScreen())));
		result.getChildren().addAll(mainTitle, infoButton ,addRoomateButton, customButton ,addContributionButton, paymentButton, saveButton);
		result.setSpacing(10);
		result.setPadding(new Insets(10));
		return result;
	}
	
	private class EditRoomateScreen extends VBox{
		EditRoomateScreen(int selectedIndex){
			ObservableList<RoomMate> roomMateOL =  FXCollections.observableArrayList(matesList);
			ListView<RoomMate> roomMateLV = new ListView<RoomMate>(roomMateOL);
			roomMateLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			if(selectedIndex ==-1);
			else;
				
			Text averageContributionText = new Text("Average contribution: " + usdFormat.format(averageContribution()));
			TextField newValueField = new TextField();
			Button setContributionButton = new Button("Add contribution");
			setContributionButton.setOnAction(e->{
				if(roomMateLV.getSelectionModel().getSelectedIndex() == -1);
				else {
					try {
					double newContribution = Double.parseDouble(newValueField.getText());
					matesList.get(roomMateLV.getSelectionModel().getSelectedIndex()).addContribution(newContribution);
					for(RoomMate roomMate:matesList)
						roomMate.calculateDifference(totalContribution());
					stage.setScene(new Scene(new EditRoomateScreen(roomMateLV.getSelectionModel().getSelectedIndex())));
					} catch(NumberFormatException ex) {}
				}});
			this.setSpacing(10);
			this.setPadding(new Insets(10));
			this.getChildren().addAll(roomMateLV, averageContributionText, newValueField ,setContributionButton ,menuButton);
		}
	}
		
	VBox AddRoomateScreen(){
		VBox result = new VBox();
		TextField nameField = new TextField();
		Button addThisRoomateButton = new Button("Add roomate");
		Text title = new Text("Enter new roommate");
		
		title.setFont(new Font("Helvetica",50));
		
		addThisRoomateButton.setOnAction(e->{
			if(nameField.getText().equals(""));
			else {
				String name = nameField.getText();
				try {
					if(matesList.size()!=0&&matesList.contains(new RoomMate(name)))
						alertText.setText("Roomate Already in list");
					else {
						matesList.add(new RoomMate(name));
						nameField.setText("");
						alertText.setText("Roommate added");
						expandArray();
						for(RoomMate roomMate: matesList) {
							roomMate.setPortion(100.0/matesList.size());
							roomMate.calculateDifference(totalContribution());
						}
					}
				} catch(NullPointerException exception) {
					matesList.add(new RoomMate(name));
					expandArray();
				}
			}		
		});
		result.getChildren().addAll(title,alertText, nameField,addThisRoomateButton,menuButton);
		result.setSpacing(10);
		result.setPadding(new Insets(10));
		return result;
	}
	
	VBox setCustomPortions() {
		VBox result = new VBox();
		TextField[] portions = new TextField[matesList.size()];
		for(int i = 0; i < matesList.size(); i++) {
			portions[i] = new TextField();
			result.getChildren().addAll(new Text(matesList.get(i).getName() + "'s Portion:"), portions[i]);
		}
		Button setPortionsButton = new Button("Set portions");
		setPortionsButton.setOnAction(e->{
			try {
				double sum = 0;
				for(TextField field:portions)
					sum += Double.parseDouble(field.getText());
				if(sum > 99.0 && sum < 101.0) {
					for(int i = 0; i < matesList.size(); i++)
						matesList.get(i).setPortion(Double.parseDouble(portions[i].getText()));
					setTotal();
				}
				else
					alertText.setText("Non-valid portions set. Please enter portions that sum to 100");
			} catch(NumberFormatException ex) {}
		});
		result.getChildren().addAll(alertText, setPortionsButton, menuButton);
		result.setSpacing(10);
		result.setPadding(new Insets(10));
		return result;
	}
	
	void expandArray() {
		double[][] temp = new double[matesList.size()][matesList.size()];
		for(int i = 0; i < debtsInformationArray.length; i++) {
			for(int j = 0; j < debtsInformationArray[0].length; j++) {
				temp[i][j] = debtsInformationArray[i][j];
			}
		}
		debtsInformationArray = temp;
	}
	
	VBox calculateTotals(){
		VBox result = new VBox();
		TextArea textArea = new TextArea(formatDebts());
		Button addNewDebtsButton = new Button("Add Contributions");
		addNewDebtsButton.setOnAction(e->{
			addNewDebts();
			debtsInformationArray = normalizeArray(debtsInformationArray);
			textArea.setText(formatDebts());
			for(RoomMate mate:matesList) {
				mate.setContribution(0);
				mate.setDifference(0);
			}
		});
		textArea.setEditable(false);
		result.getChildren().addAll(textArea, addNewDebtsButton,menuButton);
		result.setSpacing(10);
		result.setPadding(new Insets(10));
		return result;
	}
	
	VBox makePaymentScreen() {
		
		TextArea debtInformation = new TextArea();
		TextField paymentField = new TextField();
		Button makePaymentButton = new Button("Make Payment");
		
		VBox result = new VBox();
		ChoiceBox<String> payerChoice = new ChoiceBox<String>() , recieverChoice = new ChoiceBox<String>();
		ArrayList<String> namesList = new ArrayList<String>();
		for(RoomMate mate:matesList)
			namesList.add(mate.getName());
		payerChoice.setItems(FXCollections.observableList(namesList));
		recieverChoice.setItems(FXCollections.observableList(namesList));
		payerChoice.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
						debtInformation.setText(formatPersonalDebt(arg2.intValue()));
					}
				});
		
		makePaymentButton.setOnAction(e -> {
			int payerIndex = payerChoice.getSelectionModel().getSelectedIndex(), recieverIndex = recieverChoice.getSelectionModel().getSelectedIndex();
			if (payerIndex == -1 || recieverIndex == -1) 
				alertText.setText("Please fill all fields");
			else if(payerIndex == recieverIndex)
				alertText.setText("You must enter two different people");
			else {
				try {
				debtsInformationArray[recieverIndex][payerIndex] += Double.parseDouble(paymentField.getText());
				alertText.setText("Payment succesfully processed");
				debtsInformationArray = normalizeArray(debtsInformationArray);
				debtInformation.setText(formatPersonalDebt(payerIndex));
				} catch(NumberFormatException ex) {alertText.setText("Entered payment invalid");}
			}
		});
		
		result.getChildren().addAll(debtInformation, alertText , new Text("Who is paying:"),payerChoice,new Text("Who is being payed:")
				,recieverChoice,new Text("How much is being payed:"),paymentField, makePaymentButton ,menuButton);
		result.setSpacing(10);
		result.setPadding(new Insets(10));
		return result;
	}
	
	
	double[][] createPaymentArray(){
		double[][] result = new double[matesList.size()][matesList.size()];
		for(int payerIndex = 0; payerIndex < matesList.size(); payerIndex++) {
			for(int recieverIndex = 0; recieverIndex < matesList.size(); recieverIndex++) {
				if(matesList.get(payerIndex).getDifference() > 0 && matesList.get(recieverIndex).getDifference() < 0) {
					double payment;
					if(Math.abs(matesList.get(payerIndex).getDifference())> Math.abs(matesList.get(recieverIndex).getDifference())) {
						payment = Math.abs(matesList.get(recieverIndex).getDifference()); 
					}
					else {
						payment = Math.abs(matesList.get(payerIndex).getDifference());
					}
					debtsInformationArray[payerIndex][recieverIndex] = payment;
					matesList.get(payerIndex).payDifference(payment);
					matesList.get(recieverIndex).recieveDifference(payment);
				}
			}
		}
		return result;
	}

	double[][] normalizeArray(double[][] input){
		for(int i = 0; i < matesList.size(); i++) {
			for(int j = 0; j<matesList.size(); j++) {
				if(input[i][j]!=0 && input[j][i]!=0) {
					if(input[i][j]>input[j][i]) {
						input[i][j] = input[i][j] - input[j][i];
						input[j][i] = 0;
					}
					else if(input[i][j] < input[j][i]) {
						input[j][i] = input[j][i] - input[i][j];
						input[i][j] = 0;
					}
					else {
						input[i][j] = 0;
						input[j][i] = 0;
					}
				}
			}
		}
		
		return input;
		
	}
	
	void addNewDebts() {
		double[][] newDebts = createPaymentArray();
		for(int payIndex = 0; payIndex < matesList.size(); payIndex++)
			for(int recieveIndex = 0; recieveIndex < matesList.size(); recieveIndex++)
				debtsInformationArray[payIndex][recieveIndex] += newDebts[payIndex][recieveIndex];
	}
	
	String formatPersonalDebt(int payer) {
		String result = matesList.get(payer).getName() + " should pay:\n";
		for(int recieveIndex = 0; recieveIndex <matesList.size(); recieveIndex++) {
			if(debtsInformationArray[payer][recieveIndex]==0);
			else {
				result += "\t" + matesList.get(recieveIndex).getName() + " : " + usdFormat.format(debtsInformationArray[payer][recieveIndex]) + "\n";
			}
		}
		return result;
	}
	
	String formatDebts() {
		String result = "";
		for(int payIndex = 0; payIndex < matesList.size();payIndex++) {
			result += formatPersonalDebt(payIndex);
		}
		return result;
	}
	
	double totalContribution() {
		double sum = 0;
		for(RoomMate roomMate: matesList)
			sum+= roomMate.getContribution();
		return sum;
	}
	
	void setTotal() {
		for(RoomMate mate : matesList)
			mate.calculateDifference(totalContribution());
	}
	
	double averageContribution() {
		double sum = 0;
		for(RoomMate roomMate: matesList)
			sum+= roomMate.getContribution();
		return sum / matesList.size();
	}	

} //end of Main Application
