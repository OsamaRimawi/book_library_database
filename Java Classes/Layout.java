package login;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Layout extends Pane {

	private final int WIDTH = 1300;
	private final int HEIGHT = 400;

	public Layout() {

		// set layouts
		this.verticalPaneLayout();

		this.setPrefSize(WIDTH, HEIGHT);
	}


	private VBox verticalPane;

	private void verticalPaneLayout() {

		verticalPane = new VBox();

		verticalPane.prefHeightProperty().bind(this.heightProperty());
		verticalPane.prefWidthProperty().bind(this.widthProperty());
		verticalPane.setSpacing(50);

		this.getChildren().add(verticalPane);

		this.textLayout();
		this.IDPaneLayout();
		this.PasswordPaneLayout();
		this.buttonPaneLayout();
	}
	
	private Label text;
	private void textLayout() {

		text = new Label("Login");

		text.prefHeightProperty().bind(verticalPane.heightProperty().divide(10));
		text.prefWidthProperty().bind(verticalPane.widthProperty());

		text.setAlignment(Pos.CENTER);

		this.verticalPane.getChildren().add(text);
	}

	private HBox IdPane;
	private void IDPaneLayout() {

		IdPane = new HBox(2);

		IdPane.prefHeightProperty().bind(this.verticalPane.heightProperty().divide(10));
		IdPane.prefWidthProperty().bind(this.verticalPane.widthProperty());
		IdPane.setAlignment(Pos.CENTER);

		this.verticalPane.getChildren().add(this.IdPane);
		
		this.IDLayout();
	}

	private HBox PasswordPane;
	private void PasswordPaneLayout() {

		PasswordPane = new HBox(2);

		PasswordPane.prefHeightProperty().bind(this.verticalPane.heightProperty().divide(10));
		PasswordPane.prefWidthProperty().bind(this.verticalPane.widthProperty());
		PasswordPane.setAlignment(Pos.CENTER);

		this.verticalPane.getChildren().add(this.PasswordPane);
		
		this.PasswordLayout();
	}
	
	private HBox buttonPane;

	private void buttonPaneLayout() {

		buttonPane = new HBox(5);

		buttonPane.prefHeightProperty().bind(this.verticalPane.heightProperty().divide(10));
		buttonPane.prefWidthProperty().bind(this.verticalPane.widthProperty());
		buttonPane.setAlignment(Pos.CENTER);

		this.verticalPane.getChildren().add(this.buttonPane);

		// set layout
		this.buttonLayout();
	}
	
	protected TextField UserIDText;
	protected Label IDLabel,errorIDText;
	
	private void IDLayout() {
		
		IDLabel = new Label("User ID");
		IDLabel.prefHeightProperty().bind(this.IdPane.heightProperty());
		IDLabel.prefWidthProperty().bind(this.IdPane.widthProperty().divide(7));
		
		UserIDText = new TextField();
		UserIDText.setPromptText("00000000");
		UserIDText.prefHeightProperty().bind(this.IdPane.heightProperty());
		UserIDText.prefWidthProperty().bind(this.IdPane.widthProperty().divide(7));
		
		errorIDText = new Label();
		errorIDText.prefHeightProperty().bind(this.IdPane.heightProperty());
		errorIDText.prefWidthProperty().bind(this.IdPane.widthProperty().divide(7));
		
		this.IdPane.getChildren().addAll(IDLabel, UserIDText, errorIDText);
	}
	
	protected TextField UserPasswordText;
	protected Label PasswordLabel ,errorPasswordText;
	
	private void PasswordLayout() {
		
		PasswordLabel = new Label("Password");
		PasswordLabel.prefHeightProperty().bind(this.PasswordPane.heightProperty());
		PasswordLabel.prefWidthProperty().bind(this.PasswordPane.widthProperty().divide(7));
		
		UserPasswordText = new TextField();
		UserPasswordText.setPromptText("123");
		UserPasswordText.prefHeightProperty().bind(this.PasswordPane.heightProperty());
		UserPasswordText.prefWidthProperty().bind(this.PasswordPane.widthProperty().divide(7));
		
		errorPasswordText = new Label();
		errorPasswordText.prefHeightProperty().bind(this.PasswordPane.heightProperty());
		errorPasswordText.prefWidthProperty().bind(this.PasswordPane.widthProperty().divide(7));
		
		this.PasswordPane.getChildren().addAll(PasswordLabel, UserPasswordText, errorPasswordText);
		
	}

	protected Button enter;

	private void buttonLayout() {

		enter = new Button("Log In");

		enter.prefHeightProperty().bind(this.buttonPane.heightProperty());
		enter.prefWidthProperty().bind(this.buttonPane.widthProperty().divide(4));

		this.buttonPane.getChildren().addAll(enter);

	}

}
