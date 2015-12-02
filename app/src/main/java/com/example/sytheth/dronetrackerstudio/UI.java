/*

package com.example.sytheth.dronetrackerstudio;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UI extends Application{
	ArrayList<String []> a = new ArrayList<String[]>();;
	int currentItem = 0;
	ImageView img = new ImageView();
	Text t1 = new Text();
	Text t2 = new Text();
	Text t3 = new Text();
	Text t4 = new Text();
	Text item = new Text();
	int totalItems;
	Scene scene;
	Stage primaryStage;
	BorderPane bp;
	File f = new File("");
	String path = f.getAbsolutePath();
	*/
/**
	 * Method run on object instantiation.
	 * @param args Input arguments given to program.
	 *//*

	public static void main(String[] args){
		Application.launch(args);
	}
	
	@Override
	*/
/**
	 * Starts the graphics thread of JavaFX.
	 * @ @param primaryStage The stage on which graphics will be displayed.
	 *//*

	public void start(Stage primaryStage) throws Exception {
		databaseRead();
		load();
		primaryStage.getIcons().add(new Image("file:///" + path + "//Icon.png"));
		scene = new Scene(bp, 875, 590, Color.ORANGE);
		bp.setStyle("-fx-background: #ffcc00;");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	*/
/**
	 * Loads information from the database into an Arraylist.
	 *//*

	public void databaseRead(){
		String initialItem = "";

		try{//Load all rows into an array here
			BufferedReader br = new BufferedReader(new FileReader("Database.txt"));
			while ((initialItem = br.readLine()) != null){
				String[] items = initialItem.split("\\|");
				a.add(items);
				totalItems += 1;
			}
			br.close();
			displayItem(currentItem);
		}catch(Exception e){
		}

	}
	*/
/**
	 * Updates the user interface to display a specific database entry.
	 * @param i Index of the entry to be displayed.
	 *//*

	public void displayItem(int i){
		String[] items = (String[]) a.get(i);
		item.setText(currentItem + 1 + "/" + totalItems);
		try {//For file not found errors
			if(items[items.length-1].contains("jpg")){//For when it tries to load the location as an image
				Image image = new Image("file:///" + items[items.length-1]);
				img.setImage(image);
			}else{
				//Image image = new Image("file:///E:/Documents/Workspace/DroneTrackerStudio/ImageNotAvailable.png");
				Image image = new Image("file:///" + path + "//ImageNotAvailable.png");
				img.setImage(image);
			}
		} catch (Exception e) {
			try {
				//Image image = new Image("file:///E:/Documents/Workspace/DroneTrackerStudio/ImageNotAvailable.png");
				Image image = new Image("file:///" + path + "//ImageNotAvailable.png");
				img.setImage(image);
			} catch (Exception e1) {
			}
		}
		//Edit text 1 to be multiple lines if it is more than 28 characters (length of date string)
		if(items[0].length() > 28){
			String s = "";
			int sum = 0;
			String[] split = items[0].split(" ");

			for(String t : split){
				if(sum + t.length() < 28){
					sum += t.length();
					s = s + t + " ";
				}else{
					sum = t.length();
					s = s + '\n' + t + " ";
				}
			}
			t1.setText(s);
		}else{
			if(items[0].length()==0){
				t1.setText("No Description Provided");
			}else{
				t1.setText(items[0]);
			}
		}
		try{
			String DT = "";
			String[] dt = items[1].split(" ");
			DT = dt[0] + " " + dt[1]  + " " + dt[2] + " " + dt[5] + '\n' + dt[3] + " " + dt[4]; 
			t4.setText(DT);
		}catch (Exception e){
			t4.setText("Date and Time Not Available");
		}
		try{
			t2.setText(items[2].split(":")[1]);
			t3.setText(items[3].split(":")[1]);
		}catch(Exception e){
			t2.setText("Location Unavailable");
			t3.setText("Location Unavailable");
		}
	}

	//-------------------------------------------------------------------------------------------------------------------
	// Loads all items in initally.
	//-------------------------------------------------------------------------------------------------------------------
	*/
/**
	 * Initializes the user interface elements.
	 *//*

	public void load(){
		Button bLeft = new Button("<");
		Button bRight = new Button(">");
		Button query = new Button("Query");

		query.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				currentItem = 0;
				totalItems = 0;
				a.clear();
				EmailSniffer.query();			
				databaseRead();

			}
		});

		bLeft.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(currentItem >=1){
					item.setText(--currentItem + 1 + "/" + totalItems);
					displayItem(currentItem);
				}else{
					currentItem = a.size()-1;
					item.setText(currentItem + 1 + "/" + totalItems);
					displayItem(currentItem);
				}
			}
		});

		bRight.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				if(currentItem <= a.size()-2){
					item.setText(++currentItem + 1 + "/" + totalItems);
					displayItem(currentItem);
				}else{
					currentItem = 0;
					item.setText(currentItem + 1 + "/" + totalItems);
					displayItem(currentItem);
				}
			}
		});

		bp = new BorderPane();

		ComboBox c1 = new ComboBox();//Month
		ComboBox c2 = new ComboBox();//Days
		ComboBox c3 = new ComboBox();//Years
		Button b1 = new Button("Search");

		b1.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				search((String) c1.getValue(), (String) c2.getValue(), (String) c3.getValue());
			}
		});

		String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
		for(int i=1; i<32; i++){
			c2.getItems().add(Integer.toString(i));
		}
		for(int i=1990; i<2030; i++){
			c3.getItems().add(Integer.toString(i));
		}
		c1.getItems().addAll(months);

		c1.setValue("Dec");
		c2.setValue("25");
		c3.setValue("2015");

		GridPane gp = new GridPane();
		Text tD = new Text("Description:");
		tD.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text tLat = new Text("Latitude:");
		tLat.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text tLon = new Text("Longitude:");
		tLon.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text dateTime = new Text("Date and Time:");
		dateTime.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		gp.getColumnConstraints().add(new ColumnConstraints(15));

		gp.add(new Text(""), 3, 1);
		gp.add(new Text(""), 3, 2);
		gp.add(new Text(""), 3, 3);
		gp.add(new Text(""), 3, 4);
		gp.add(new Text(""), 3, 5);
		gp.add(new Text(""), 3, 6);
		gp.add(tD, 3, 6);
		gp.add(t1, 5, 8);
		gp.add(new Text("                                                       "), 5, 7); //To correct for the description filling of 28 characters
		gp.add(new Text(""), 3, 9);
		gp.add(new Text(""), 3, 10);

		gp.add(dateTime, 3, 11);
		gp.add(t4, 5, 13);
		gp.add(new Text(""), 3, 12);
		gp.add(new Text(""), 3, 14);
		gp.add(new Text(""), 3, 15);

		gp.add(tLat, 3, 16);
		gp.add(t2, 5, 18);
		gp.add(new Text(""), 3, 17);
		gp.add(new Text(""), 3, 19);
		gp.add(new Text(""), 3, 20);

		gp.add(tLon, 3, 21);
		gp.add(new Text(""), 3, 22);
		gp.add(t3, 5, 23);

		GridPane gP = new GridPane();
		gP.add(c1, 1, 1);
		gP.add(c2, 2, 1);
		gP.add(c3, 3, 1);
		gP.add(b1, 4, 1);
		gP.add(new Text("\t\t"), 5, 1);
		gP.add(query, 11, 1);

		bp.setLeft(gp);
		bp.setTop(gP);

		BorderPane images = new BorderPane();
		images.setCenter(img);
		VBox V = new VBox();
		HBox H = new HBox(10);
		H.setAlignment(Pos.CENTER);
		H.getChildren().addAll(bLeft,item,bRight);

		images.setTop(H);
		bp.setCenter(images);
	}
	*/
/**
	 * Finds all entries in the database that come after a provided date.
	 * @param m Month in the form of shortened month names.
	 * @param d Date as an integer converted to a string.
	 * @param y Year as an integer converted to a string.
	 *//*

	public void search(String m, String d, String y){
		ArrayList<String[]> temp = new ArrayList<String[]>();
		String input = y + EmailSniffer.m2d(m) + d;
		for(int i = 0; i < a.size(); i++){
			String[] b = a.get(i);
			String[] c = b[1].split(" ");
			String purple = c[5] + EmailSniffer.m2d(c[1]) + c[2];

			if(Integer.parseInt(input) <= Integer.parseInt(purple)){
				for(int j=i; j < a.size(); j++){
					temp.add(a.get(j));
				}
				break;
			}
		}
		if(temp.size() != 0){
			a.clear();
			for(int i = 0; i < temp.size(); i++){
				a.add(temp.get(i));
				//System.out.println(temp.get(i));
			}
			totalItems = temp.size();
			displayItem(0);
		}
	}
}
*/
