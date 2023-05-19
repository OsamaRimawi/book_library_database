package login;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {

	// ObservableList date
	ObservableList<login> loginData;

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

	protected String UserID = "";
	protected String UserPassword = "";

	// constructor
	public Controller() {
		this.loginData = FXCollections.observableArrayList();
		this.insertOnTableFromDatabase();
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

		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);

		String query1 = "SELECT *\r\n" + "FROM login L;";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			login log = new login(resultSet.getString(1), resultSet.getString(2));

			loginData.add(log);

		}

		statement.close();
		resultSet.close();
		connection.close();

	}

	@FXML
	protected TextField UserIDText, UserPasswordText;

	@FXML
	protected Label errorIDText, errorPasswordText;

	private void clearError() {
		errorIDText.setText("");
		errorPasswordText.setText("");
	}

	@FXML
	void enter(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

		this.clearError();
		String id = UserIDText.getText();

		boolean idfound = false;
		boolean passwordfound = false;

		if (isIDValid(id)) {

			for (int i = 0; i < loginData.size(); i++) {
				if (loginData.get(i).getUser_id().equals(UserIDText.getText())) {
					idfound = true;
					if (loginData.get(i).getUser_password().equals(UserPasswordText.getText())) {
						passwordfound = true;
						break;
					}
				}
			}
			if (idfound == true) {
				if (passwordfound == true) {
					UserID = UserIDText.getText();
					UserPassword = UserPasswordText.getText();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("MyBooksLayout.fxml"));
					Parent root = loader.load();
					Scene scene = new Scene(root);
					MyBooksControl controller = loader.getController();
					controller.initData(UserID);
					Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
					window.setScene(scene);
					window.show();

				} else
					errorPasswordText.setText("Password is Incorrect");

			} else {
				errorIDText.setText("There is no account with this ID");
			}
		} else
			errorIDText.setText("Invalid User ID input");
	}
	
	private boolean isIDValid(String id) {
		boolean IDValid = true;

		if (id.length() != 8)
			return false;
		for (int i = 0; i < id.length() && IDValid; i++) {
			IDValid = (Character.isDigit(id.charAt(i)));
		}
		//Only Members
		if(id.charAt(0) != '0' || id.charAt(1) != '0' || id.charAt(2) != '0')
			return false;

		return IDValid;
	}
	}
