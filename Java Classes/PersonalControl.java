
package login;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonalControl {

	private Stage stage;
	private Scene scene;
	private Parent root;

	ObservableList<User> usersData;
	ArrayList<String> userPhone;

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

	@FXML
	protected Label userID, firstName, lastName, gender, birthdate, phones;

	@FXML
	private Label birthdateError, firstNameError, lastNameError, phonesError;

	@FXML
	protected Button firstNamebutt;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField firstNameText, lastNameText, phonesText;

	@FXML
	private ToggleGroup radio;

	@FXML
	private RadioButton femaleCombo;

	@FXML
	private RadioButton maleCombo;

	// constructor
	public PersonalControl() {
		this.initial(stage);
	}

	// initial for this scene
	private void initial(Stage stage) {
		this.stage = stage;
		this.usersData = FXCollections.observableArrayList();
		this.insertOnTableFromDatabase();

	}

	public void initData(String UserID) {
		userID.setText(UserID);
		try {
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(UserID)) {
					firstName.setText(usersData.get(i).getFirstName());
					lastName.setText(usersData.get(i).getLastName());
					if (usersData.get(i).getGender().equals("M"))
						gender.setText("Male");
					else if (usersData.get(i).getGender().equals("F"))
						gender.setText("Female");
					birthdate.setText(usersData.get(i).getBirthDate());
					this.addPhones();
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertOnTableFromDatabase() {

		try {
			this.getUsersData();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void getUsersData() throws ClassNotFoundException, SQLException {

		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "SELECT U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, M.fees\r\n"
				+ "FROM users U1, member M\r\n" + "  WHERE U1.user_id = M.user_id ;\r\n";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			User user = new User(Integer.parseInt(resultSet.getString(1)), resultSet.getString(2),
					resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
					Double.parseDouble(resultSet.getString(7)));
			usersData.add(user);

		}
		statement.close();
		resultSet.close();
		connection.close();
	}

	@FXML
	public void loginScene(ActionEvent event) {
		try {

			root = FXMLLoader.load(getClass().getResource("LoginLayout.fxml"));
			stage = (Stage) firstNamebutt.getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void allBooksScene(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("AllBooksLayout.fxml"));
			Parent root;
			root = loader.load();
			Scene scene = new Scene(root);
			AllBooks controller = loader.getController();
			controller.initData(userID.getText());
			Stage window = (Stage) firstNamebutt.getScene().getWindow();
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void myBooksScene(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MyBooksLayout.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			MyBooksControl controller = loader.getController();
			controller.initData(userID.getText());
			Stage window = (Stage) firstNamebutt.getScene().getWindow();
			window.setScene(scene);
			window.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@FXML
	private void addPhones() throws ClassNotFoundException, SQLException {
		userPhone = new ArrayList<String>();
		String id = userID.getText();
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "SELECT  P.phone_number From phone P\r\n" + "WHERE P.user_id = \"" + id + "\" ;\r\n";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			String Phone = resultSet.getString(1);
			userPhone.add(Phone);

		}
		statement.close();
		resultSet.close();
		connection.close();
		String allPhones = "";
		for (int i = 0; i < userPhone.size(); i++) {
			allPhones = allPhones + userPhone.get(i) + "\n";
		}
		phones.setText(allPhones);
	}

	@FXML
	private void updateFirstName() throws ClassNotFoundException, SQLException {
		this.clearError();
		String id = userID.getText();
		String newName = firstNameText.getText();
		if (!newName.equals("")) {
			firstName.setText(newName);
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(id)) {
					usersData.get(i).setFirstName(newName);
					break;
				}
			}
			String query = "UPDATE users\r\n" + "SET user_firstName = \"" + newName + "\"\r\n" + "WHERE user_id = "
					+ id;
			this.executeQuery(query);
		} else {
			firstNameError.setText("Invalid Input");
		}
	}

	@FXML
	private void addNewPhone() throws ClassNotFoundException, SQLException {
		this.clearError();
		String id = userID.getText();
		String newPhone = phonesText.getText();
		if (isPhoneNumberValid(newPhone)) {
			userPhone.add(newPhone);
			String allPhones = "";
			for (int i = 0; i < userPhone.size(); i++) {
				allPhones = allPhones + userPhone.get(i) + "\n";
			}
			phones.setText(allPhones);
			String query = "Insert Into phone VALUES(\""+newPhone+"\", \""+id+"\");";
			this.executeQuery(query);
		} else {
			phonesError.setText("Invalid Input");
		}
	}
	@FXML
	private void deletePhone() throws ClassNotFoundException, SQLException {
		this.clearError();
		boolean found = false ;
		String newPhone = phonesText.getText();
		if (isPhoneNumberValid(newPhone)) {
			for (int i = 0; i < userPhone.size(); i++) {
				if (newPhone.equals(userPhone.get(i))) {
					userPhone.remove(i);
					String allPhones = "";
					for (int j = 0; j < userPhone.size(); j++) {
						allPhones = allPhones + userPhone.get(j) + "\n";
					}
					phones.setText(allPhones);
					String query = "DELETE FROM phone WHERE phone_number =\""+newPhone+"\";" ;
					this.executeQuery(query);
					found = true;
					break;
				}
			}
			if(!found)
				phonesError.setText("Don't own this phone");

		} else {
			phonesError.setText("Invalid Input");
		}
	}
	
	@FXML
	private void updateLasttName() throws ClassNotFoundException, SQLException {
		this.clearError();
		String id = userID.getText();
		String newName = lastNameText.getText();
		if (!newName.equals("")) {

			lastName.setText(newName);
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(id)) {
					usersData.get(i).setLastName(newName);
					break;
				}
			}
			String query = "UPDATE users\r\n" + "SET user_lastName = \"" + newName + "\"\r\n" + "WHERE user_id = " + id;
			this.executeQuery(query);
		} else {
			lastNameError.setText("Invalid Input");
		}
	}

	@FXML
	private void updateGender() throws ClassNotFoundException, SQLException {
		this.clearError();
		String id = userID.getText();
		if (maleCombo.isSelected()) {

			gender.setText("Male");
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(id)) {
					usersData.get(i).setGender("M");
					break;
				}
			}
			String query = "UPDATE users\r\n" + "SET user_gender = \'" + "M" + "\'\r\n" + "WHERE user_id = " + id;
			this.executeQuery(query);
		} else if (femaleCombo.isSelected()) {

			gender.setText("Female");
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(id)) {
					usersData.get(i).setGender("F");
					break;
				}
			}
			String query = "UPDATE users\r\n" + "SET user_gender = \'" + "F" + "\'\r\n" + "WHERE user_id = " + id;
			this.executeQuery(query);
		}
	}

	@FXML
	private void updateBirthday() throws ClassNotFoundException, SQLException {
		this.clearError();
		String id = userID.getText();
		LocalDate localdate = datePicker.getValue();
		LocalDate now = LocalDate.now();
		String newBirthday = "";
		if (localdate != null) {
			if (localdate.isBefore(now)) {
				newBirthday = (localdate.getDayOfMonth() + "-" + localdate.getMonthValue() + "-" + localdate.getYear());

				birthdate.setText(newBirthday);
				for (int i = 0; i < usersData.size(); i++) {
					if (usersData.get(i).getId() == Integer.parseInt(id)) {
						usersData.get(i).setBirthDate(newBirthday);
						break;
					}
				}
				String query = "UPDATE users\r\n" + "SET user_birthDate = \"" + newBirthday + "\"\r\n"
						+ "WHERE user_id = " + id;
				this.executeQuery(query);
			} else {
				birthdateError.setText("Date should be before current time");
			}
		} else {
			birthdateError.setText("Invalid Input");
		}
	}

	private boolean isPhoneNumberValid(String phone) {
		boolean isValid = true;
		// chick if phone number is contain 10 digits
		isValid = (phone.length() == 10);
		// chick if all characters are digits
		for (int i = 0; i < phone.length() && isValid; i++) {
			isValid = (Character.isDigit(phone.charAt(i)));
		}
		return isValid;
	}
	
	private void clearError() {
		firstNameError.setText("");
		lastNameError.setText("");
		birthdateError.setText("");
		phonesError.setText("");

	}

	private void executeQuery(String query) throws ClassNotFoundException, SQLException {
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);

		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.statement.executeUpdate(query);

		this.connection.close();
		this.statement.close();
	}

}
