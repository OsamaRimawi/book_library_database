package login;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.DoubleStringConverter;
import login.ConnectToDatabase;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import com.mysql.cj.xdevapi.Result;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;

public class Controller {


 private Stage stage;
 private Scene scene;
 private Parent root;

	// ObservableList date
	ObservableList<login> data;

	// ----------------------------------------------------------------------------//
	// fields for connection to database
	private Connection connection;
	private ResultSet resultSet;
	private Statement statement;

	private static String dbUsername = "root"; // database username
	private static String dbPassword = "osama12345"; // database password
	private static String URL = "127.0.0.1"; // server location
	private static String port = "3306"; // port that mysql uses
	private static String dbName = "Library"; // database on mysql to connect to
	// ----------------------------------------------------------------------------//

	private String UserID ="";
	private String UserPassword ="";

	// constructor
	public Controller() {
		this.initial(stage);
		}

	// initial for this scene
	private void initial(Stage stage) {
		this.stage = stage;
		this.data = FXCollections.observableArrayList();
		this.insertOnTableFromDatabase();
	}

	// set scene in the stage
	public void setScene() {
		this.stage.setScene(this.scene);
	}
	
	public void setScene(Scene s) {
		this.stage.setScene(s);
	}
 
	private void insertOnTableFromDatabase() {

		try {
			this.getData();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	private void getData() throws ClassNotFoundException, SQLException {

		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);

		// select information from users and phone tables who has 2 phone numbers
		String query1 = "SELECT *\r\n"
				+ "FROM login L;";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		// add all users in the data ObservableList
		while (resultSet.next()) { 	

			// create user reference
			login log = new login(resultSet.getString(1), resultSet.getString(2));

			// add user reference to data ObseravleList
			data.add(log);

		} // end while

		statement.close();
		resultSet.close();
		connection.close();

	}
	// clear all errors
	private void clearError() {
		errorIDText.setText("");
		errorPasswordText.setText("");
	}
	
 @FXML
	protected TextField UserPasswordText,UserIDText;
 
 @FXML
	protected Label errorPasswordText,errorIDText; 
 @FXML
 	void enter (ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

			this.clearError();
			boolean isIDValid = true;
			String s = UserIDText.getText();
			if (s.length() < 8)
				isIDValid = false;
			for (int i = 0; i < s.length() && isIDValid; i++) {
				isIDValid = (Character.isDigit(s.charAt(i)));
			}

			boolean found = false;
			if (isIDValid) {

				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).getUser_id().equals(UserIDText.getText()))
						if (data.get(i).getUser_password().equals(UserPasswordText.getText())) {
							found = true;
							break;
						}
				}
				if (found == true) {
					errorIDText.setText("found");
					UserID = UserIDText.getText();
					UserPassword = UserPasswordText.getText();
					  root = FXMLLoader.load(getClass().getResource("UserLayout.fxml"));
					  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					  scene = new Scene(root);
					  stage.setScene(scene);
					  stage.show();
				} else {
					errorIDText.setText("ID is not found");
				}
			} else
					errorIDText.setText("invalid ID input");
		}
		
 public void switchToScene1(ActionEvent event) throws IOException {
  root = FXMLLoader.load(getClass().getResource("LoginLayout.fxml"));
  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
  scene = new Scene(root);
  stage.setScene(scene);
  stage.show();
 }
 
 }