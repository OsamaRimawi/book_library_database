package login;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyBooksControl implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	// ObservableList date
	ObservableList<Borrow> borrowData;
	ObservableList<User> usersData;
	ObservableList<book> booksData;
	ArrayList<String> authorData;
	String publisherData;
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
	protected Label userID ,authors ,book_description ,date_of_publish,num_of_copies 
	,price ,price_per_day ,publisher ,balance ,Fees;
	@FXML
	protected Button search;
    @FXML
    private TextField searchText;
	@FXML
	private TableView<Borrow> bookTable;
	@FXML
	private TableColumn<Borrow, String> book_name;
	@FXML
	private TableColumn<Borrow, Integer> publisher_id;
    @FXML
    private TableColumn<Borrow, String> date_of_borrow;
    @FXML
    private TableColumn<Borrow, String> end_date_of_borrow;
    @FXML
    private TableColumn<Borrow, String> return_date;
    @FXML
    private TableColumn<Borrow, Integer> lating_days;

	// constructor
	public MyBooksControl() {
		this.initial(stage);
	}

	// initial for this scene
	private void initial(Stage stage) {
		this.stage = stage;
		this.borrowData = FXCollections.observableArrayList();
		this.usersData = FXCollections.observableArrayList();
		this.insertOnTableFromDatabase();
		this.booksData = FXCollections.observableArrayList();
		this.insertOnBookTable();
	}

	public void initData(String UserID) {
		userID.setText(UserID);
		this.insertOnBorrowTable();
			for (int i = 0; i < usersData.size(); i++) {
				if (usersData.get(i).getId() == Integer.parseInt(UserID)) {
					balance.setText(usersData.get(i).getFinantialPalance());
					Fees.setText(Double.toString(usersData.get(i).getFees()));
					break;
				}
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
	private void insertOnBookTable() {

		try {
			this.getBookData();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			stage = (Stage) search.getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void personalScene(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PersonalLayout.fxml"));
			Parent root;
			root = loader.load();
			Scene pers = new Scene(root);
			PersonalControl controller = loader.getController();
			controller.initData(userID.getText());
			Stage window = (Stage) search.getScene().getWindow();
			window.setScene(pers);
			window.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			Stage window = (Stage) search.getScene().getWindow();
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	public void searchBook(ActionEvent event) {
		ObservableList<Borrow> newBorrowData;
		newBorrowData = FXCollections.observableArrayList();

		String Name = searchText.getText();
		if(Name.equals("")) {
			bookTable.setItems(borrowData);
			bookTable.refresh();
		}
		else {
			for(int i=0; i<borrowData.size();i++) {
				if(borrowData.get(i).getBook_name().contains(Name))
					newBorrowData.add(borrowData.get(i));
			}
			bookTable.setItems(newBorrowData);
			bookTable.refresh();
		}

	}

	private void insertOnBorrowTable() {

		try {
			this.getBorrowData();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getBorrowData() throws ClassNotFoundException, SQLException {
		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String id =	userID.getText();
		String query1 = "SELECT  B.book_name, B.publisher_id, B.date_of_borrow, B.end_date_of_borrow, B.return_date, L.lating_days\r\n"
				+ "From borrows B ,lating_Day L \r\n" 
				+ "WHERE B.user_id =\""+ id +"\" AND\r\n" + 
				"B.end_date_of_borrow = L.end_date_of_borrow AND\r\n" + 
				"B.return_date = L.return_date ;\r\n";
		
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			Borrow borrow = new Borrow(resultSet.getString(1), Integer.parseInt(resultSet.getString(2)),
					resultSet.getString(3),resultSet.getString(4),
					resultSet.getString(5), Integer.parseInt(resultSet.getString(6)));
			borrowData.add(borrow);
		}

		statement.close();
		resultSet.close();
		connection.close();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		book_name.setCellValueFactory(new PropertyValueFactory<Borrow, String>("book_name"));
		publisher_id.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("publisher_id"));
		date_of_borrow.setCellValueFactory(new PropertyValueFactory<Borrow, String>("date_of_borrow"));
		end_date_of_borrow.setCellValueFactory(new PropertyValueFactory<Borrow, String>("end_date_of_borrow"));
		return_date.setCellValueFactory(new PropertyValueFactory<Borrow, String>("return_date"));
		lating_days.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("lating_days"));
		bookTable.setItems(borrowData);

		bookTable.setEditable(true);

	}
	
	private void getBookData() throws ClassNotFoundException, SQLException {
		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);

		String query1 = "SELECT *\r\n" + "FROM book b;";

		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			book Book = new book(resultSet.getString(1), Integer.parseInt(resultSet.getString(2)),
					Double.parseDouble(resultSet.getString(3)), Double.parseDouble(resultSet.getString(4)),
					resultSet.getString(5), resultSet.getString(6), Integer.parseInt(resultSet.getString(7)));
			booksData.add(Book);

		}

		statement.close();
		resultSet.close();
		connection.close();
	}
    @FXML
    void displyDetail(MouseEvent event) throws ClassNotFoundException, SQLException {
    	Borrow borrow = bookTable.getSelectionModel().getSelectedItem();
    	if(borrow != null) {
    		for (int i = 0; i < booksData.size(); i++) {
    			if(borrow.getBook_name().equals(booksData.get(i).getBook_name()) && 
    			   borrow.getPublisher_id() == booksData.get(i).getPublisher_id())
    			{

    	    		this.getAuthorData(borrow.getBook_name(),borrow.getPublisher_id());
    	    		this.getPublisherData(borrow.getPublisher_id());
    	    		String allAuthors = "";
    	    		for (int j = 0; j < authorData.size(); j++) {
    	    			allAuthors = allAuthors + authorData.get(j) + "\n";
    	    		}
    	    		authors.setText(allAuthors);
    	    		publisher.setText(publisherData);
    	    		book_description.setText(booksData.get(i).getBook_description());
    	    		date_of_publish.setText(booksData.get(i).getDate_of_publish());
    	    		price_per_day.setText(Double.toString(booksData.get(i).getBorrowing_pricePerDay()));
    	    		price.setText(Double.toString(booksData.get(i).getPrice()));
    	    		num_of_copies.setText(Integer.toString(booksData.get(i).getNumber_of_copy()));
    			}
    		}
    	}
    }
    
	private void getAuthorData(String bookname ,int PID) throws ClassNotFoundException, SQLException {
		authorData = new ArrayList<String>();
		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "SELECT  A.author_name From book_to_auther BTA,author A\r\n" + 
				"WHERE BTA.author_id = A.author_id AND\r\n" + 
				"      BTA.book_name = \""+bookname+"\" AND\r\n" + 
				"	  BTA.publisher_id = \""+PID+"\" ;";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			authorData.add(resultSet.getString(1));
		}
		statement.close();
		resultSet.close();
		connection.close();
	}
	private void getPublisherData(int PID) throws ClassNotFoundException, SQLException {
		publisherData = "";
		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "SELECT  P.publisher_name From publisher P\r\n" + 
				"WHERE P.publisher_id = \""+PID +"\";";
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			publisherData = resultSet.getString(1);
		}
		statement.close();
		resultSet.close();
		connection.close();
	}
	

}
