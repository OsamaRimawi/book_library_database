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
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllBooks implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	// ObservableList date
	ObservableList<book> booksData;
	ObservableList<User> usersData;
	ArrayList<String> authorData;
	ObservableList<Borrow> borrowData;

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
	protected Label userID, authors, publishers ,reserveError;
	@FXML
	protected Button reserve;
    @FXML
    private TextField searchText;
    @FXML
	private TableView<book> bookTable;
	@FXML
	private TableColumn<book, String> book_name;
	@FXML
	private TableColumn<book, Integer> number_of_copy;
	@FXML
	private TableColumn<book, Double> price;
	@FXML
	private TableColumn<book, Double> borrowing_pricePerDay;
	@FXML
	private TableColumn<book, String> book_description;
	@FXML
	private TableColumn<book, String> date_of_publish;
	@FXML
	private TableColumn<book, Integer> publisher_id;

	// constructor
	public AllBooks() {
		this.initial(stage);
	}

	// initial for this scene
	private void initial(Stage stage) {
		this.stage = stage;
		this.borrowData = FXCollections.observableArrayList();
		this.booksData = FXCollections.observableArrayList();
		this.insertOnBookTable();
		this.usersData = FXCollections.observableArrayList();
		this.insertOnTableFromDatabase();
	}

	public void initData(String UserID) {
		userID.setText(UserID);
		this.insertOnBorrowTable();

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

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		book_name.setCellValueFactory(new PropertyValueFactory<book, String>("book_name"));
		number_of_copy.setCellValueFactory(new PropertyValueFactory<book, Integer>("number_of_copy"));
		price.setCellValueFactory(new PropertyValueFactory<book, Double>("price"));
		borrowing_pricePerDay.setCellValueFactory(new PropertyValueFactory<book, Double>("borrowing_pricePerDay"));
		book_description.setCellValueFactory(new PropertyValueFactory<book, String>("book_description"));
		date_of_publish.setCellValueFactory(new PropertyValueFactory<book, String>("date_of_publish"));
		publisher_id.setCellValueFactory(new PropertyValueFactory<book, Integer>("publisher_id"));
		bookTable.setItems(booksData);
		bookTable.setEditable(true);

	}

	@FXML
	public void loginScene(ActionEvent event) {
		try {

			root = FXMLLoader.load(getClass().getResource("LoginLayout.fxml"));
			stage = (Stage) reserve.getScene().getWindow();
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
			Scene scene = new Scene(root);
			PersonalControl controller = loader.getController();
			controller.initData(userID.getText());
			Stage window = (Stage) reserve.getScene().getWindow();
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
			Stage window = (Stage) reserve.getScene().getWindow();
			window.setScene(scene);
			window.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void searchBook(ActionEvent event) {
		this.clearError();
		ObservableList<book> newBookData;
		newBookData = FXCollections.observableArrayList();

		String Name = searchText.getText();
		if(Name.equals("")) {
			bookTable.setItems(booksData);
			bookTable.refresh();
		}
		else {
			for(int i=0; i<booksData.size();i++) {
				if(booksData.get(i).getBook_name().contains(Name))
					newBookData.add(booksData.get(i));
			}
			bookTable.setItems(newBookData);
			bookTable.refresh();
		}

	}
	@FXML
	void displyDetail(MouseEvent event) throws ClassNotFoundException, SQLException {
		this.clearError();
		book Book = bookTable.getSelectionModel().getSelectedItem();
		if (Book != null) {
			this.getAuthorData(Book.getBook_name(), Book.getPublisher_id());
			this.getPublisherData(Book.getPublisher_id());
			String allAuthors = "";
			for (int i = 0; i < authorData.size(); i++) {
				allAuthors = allAuthors + authorData.get(i) + "\n";
			}
			authors.setText(allAuthors);
			publishers.setText(publisherData);
		}
	}

	private void getAuthorData(String bookname, int PID) throws ClassNotFoundException, SQLException {
		authorData = new ArrayList<String>();
		// create a connection with the database
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "SELECT  A.author_name From book_to_auther BTA,author A\r\n"
				+ "WHERE BTA.author_id = A.author_id AND\r\n" + "      BTA.book_name = \"" + bookname + "\" AND\r\n"
				+ "	  BTA.publisher_id = \"" + PID + "\" ;";
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
		String query1 = "SELECT  P.publisher_name From publisher P\r\n" + "WHERE P.publisher_id = \"" + PID + "\";";
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
		String id = userID.getText();
		String query1 = "SELECT  B.book_name, B.publisher_id, B.date_of_borrow, B.end_date_of_borrow, B.return_date, L.lating_days\r\n"
				+ "From borrows B ,lating_Day L \r\n" + "WHERE B.user_id =\"" + id + "\" AND\r\n"
				+ "B.end_date_of_borrow = L.end_date_of_borrow AND\r\n" + "B.return_date = L.return_date ;\r\n";

		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.resultSet = statement.executeQuery(query1);

		while (resultSet.next()) {

			Borrow borrow = new Borrow(resultSet.getString(1), Integer.parseInt(resultSet.getString(2)),
					resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
					Integer.parseInt(resultSet.getString(6)));
			borrowData.add(borrow);
		}

		statement.close();
		resultSet.close();
		connection.close();

	}

	@FXML
	void reserveBook(ActionEvent event) throws ClassNotFoundException, SQLException {
		this.clearError();
		boolean found = false;
		book Book = bookTable.getSelectionModel().getSelectedItem();
		LocalDate currentDate = LocalDate.now();
		LocalDate DatePlusMonth = currentDate.plusMonths(1);
		String date = (currentDate.getDayOfMonth() + "-" + currentDate.getMonthValue() + "-" + currentDate.getYear());
		String enddate = (DatePlusMonth.getDayOfMonth() + "-" + DatePlusMonth.getMonthValue() + "-" + DatePlusMonth.getYear());

		if (Book != null) {
			if (Book.getNumber_of_copy() > 0) {
				for (int i = 0; i < borrowData.size(); i++) {
					if (Book.getBook_name().equals(borrowData.get(i).getBook_name())
							&& Book.getPublisher_id() == borrowData.get(i).getPublisher_id()) { 
						found = true;
								break;
					}
				}
				if(!found) {
					for (int i = 0; i < booksData.size(); i++) {
						if (Book.getBook_name().equals(booksData.get(i).getBook_name())
								&& Book.getPublisher_id() == booksData.get(i).getPublisher_id()) { 
							booksData.get(i).setNumber_of_copy((booksData.get(i).getNumber_of_copy() - 1));
									break;
						}
					}
					borrowData.add(new Borrow(Book.getBook_name(),Book.getPublisher_id(),date,enddate,"",0));
					this.addBorrow(Book.getBook_name(), Book.getPublisher_id(), date, enddate);
				
					bookTable.refresh();

					}
				else
					reserveError.setText("The book is already reserved");

			} else
				reserveError.setText("No Copies left");
		}
	}
	private void addBorrow(String bookname ,int PID , String borrowDate , String endDate) throws ClassNotFoundException, SQLException {
		String id = userID.getText();
		ConnectToDatabase connectToDatabase = new ConnectToDatabase(URL, port, dbName, dbUsername, dbPassword);
		String query1 = "INSERT IGNORE INTO lating_day VALUES(\""+endDate+"\", \"\",0);\r\n" ;
		
		String query2 ="INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) \r\n" + 
				"VALUES (\""+id+"\",\""+bookname+"\","+PID+",\"01000000\", \""+borrowDate+"\", \""+endDate+"\", \"\");";
				
		String query3 =	 "UPDATE book SET number_of_copy = (number_of_copy - 1) \r\n" + 
				"WHERE book_name = \""+bookname+"\" AND publisher_id = "+PID+";";
		
		this.connection = connectToDatabase.connect();
		this.statement = connection.createStatement();
		this.statement.executeUpdate(query1);
		this.statement.executeUpdate(query2);
		this.statement.executeUpdate(query3);

		this.connection.close();
		this.statement.close();
	}
	private void clearError() {
		reserveError.setText("");
	}
}
