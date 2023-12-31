import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Calculator Application that makes simple math using JavaFx to display the calculator.
 * It does addition, subtraction, multiplication, division, power, square root and works 
 * with memory buttons too.
 * @author Nicolas Tagliafichi
 *
 */
public class Calculator extends Application {

	/**
	 * Display the text field.
	 */
	private TextField tfDisplay;

	/**
	 * The 24 buttons that the calculator uses.
	 */
	private Button[] btns;        

	/**
	 * The result of calculations.
	 */
	private double result = 0;

	/**
	 * Input number as a string.
	 */
	private String inStr = "0";

	/**
	 * Previous operator: ' '(nothing), '+', '-', '*', '/', '=' or '^'
	 */
	private char lastOperator = ' ';

	/**
	 * The numerical value that will be displayed in the JavaFx text control for memory
	 */
	private double memory = 0;

	/**
	 * Displays the memory number.
	 */
	private Text memoryText;

	// Event handler for all the 24 Buttons 
	private EventHandler<ActionEvent> handler = evt -> {
		String currentBtnLabel = ((Button)evt.getSource()).getText();

		switch (currentBtnLabel) {

		// Number buttons and '.' for decimal places
		case "0": case "1": case "2": case "3": case "4":
		case "5": case "6": case "7": case "8": case "9":
		case ".":

			if (inStr.equals("0")) {
				this.inStr = currentBtnLabel;  // no leading zero
			} else {
				this.inStr += currentBtnLabel; // append input digit
			}

			this.tfDisplay.setText(this.inStr);

			// Clear buffer if last operator is '='
			if (this.lastOperator == '=') {
				this.result = 0; //reset the result
				this.lastOperator = ' '; //reset the last operator
			}
			break;


			// Operator buttons: '+', '-', 'x', '/', '=', '^' 
		case "+":
			compute();
			this.lastOperator = '+';
			break;
		case "-":
			compute();
			this.lastOperator = '-';
			break;
		case "x":
			compute();
			this.lastOperator = '*';
			break;
		case "\u00F7":
			compute();
			this.lastOperator = '/';
			break;
		case "=":
			compute();
			this.lastOperator = '=';
			break;
		case "^":
			compute();
			this.lastOperator = '^';
			break;

			//Square root
		case "\u221A":
			if (this.lastOperator != '=') {
				this.result = Double.parseDouble(this.inStr);
			}
			this.result = Math.sqrt(this.result);
			this.inStr = this.result + "";
			this.tfDisplay.setText(this.inStr);
			this.lastOperator = '=';
			break;

			//Erase last number
		case "\u2190": 
			if (this.inStr.length() == 1) {
				this.inStr = "0";
			}else {
				this.inStr = this.inStr.substring(0, this.inStr.length() - 1);
			}
			this.tfDisplay.setText(this.inStr);

			break;

			//Memory buttons
			//Memory recall
		case "MR":
			this.inStr = String.valueOf(this.memory);
			this.tfDisplay.setText(inStr);
			break;

			//Add to memory
		case "M+":
			if (this.lastOperator != '=') {
				this.memory += Double.parseDouble(this.inStr);
			} else { 
				this.memory += this.result;
			}
			this.memoryText.setText("Memory = " + this.memory);
			break;

			//Subtract from memory
		case "M-":
			if (this.lastOperator != '=') {
				this.memory -= Double.parseDouble(this.inStr);
			} else { 
				this.memory -= this.result;
			}
			this.memoryText.setText("Memory = " + this.memory);
			break;

			//Memory clear
		case "MC":
			this.memory = 0; 
			this.memoryText.setText("Memory = " + this.memory);
			break;

			// Clear button
		case "C":
			this.result = 0;
			this.inStr = "0";
			this.lastOperator = ' ';
			this.tfDisplay.setText("0");
			break;
		}
	};

	/**
	 * User pushes '+', '-', '*', '/' or '=' button. <br>
	 * Perform computation on the previous result and the current input number  based on the previous operator.
	 */
	private void compute() {

		Double inNum = Double.parseDouble(this.inStr);

		this.inStr = "0";
		if (this.lastOperator == ' ') {
			this.result = inNum;
		} else if (this.lastOperator == '+') {
			this.result += inNum;
		} else if (this.lastOperator == '-') {
			this.result -= inNum;
		} else if (this.lastOperator == '*') {
			this.result *= inNum;
		} else if (this.lastOperator == '/') {
			this.result /= inNum;
		} else if (this.lastOperator == '^') {
			this.result = (int) Math.pow(this.result, inNum);	
		} else if (this.lastOperator == '=') {
			//Keeps the result
		}

		this.tfDisplay.setText(this.result + "");
	}

	/**
	 * Setup the UI
	 */
	@Override
	public void start(Stage primaryStage) {

		String[] btnLabels = {  // Labels of 24 buttons.
				"7", "8", "9", "+",
				"4", "5", "6", "-",
				"1", "2", "3", "x",
				".", "0", "=", "\u00F7",//\u00F7 for division
				"C", "\u2190", "^", "\u221A", //\u221A for square root, and \u2190 for backspace
				"M+", "M-", "MR", "MC"
		};

		// Setup the Display TextField
		this.tfDisplay = new TextField("0");
		this.tfDisplay.setEditable(false);
		this.tfDisplay.setAlignment(Pos.CENTER_RIGHT);

		//Instantiate the memory text with an initial value
		this.memoryText = new Text("Memory = 0.0");

		// Setup a GridPane for 4 column Buttons
		int numCols = 4;

		GridPane paneButton = new GridPane();

		paneButton.setPadding(new Insets(15, 0, 15, 0));  // top, right, bottom, left
		paneButton.setVgap(5);  // Vertical gap between nodes
		paneButton.setHgap(5);  // Horizontal gap between nodes

		// Setup 4 columns of equal width, fill parent
		ColumnConstraints[] columns = new ColumnConstraints[numCols];
		for (int i = 0; i < numCols; ++i) {
			columns[i] = new ColumnConstraints();
			columns[i].setHgrow(Priority.ALWAYS) ;  // Allow column to grow
			columns[i].setFillWidth(true);  // Ask nodes to fill space for column
			paneButton.getColumnConstraints().add(columns[i]);
		}

		// Setup 24 Buttons and add to GridPane; and event handler
		btns = new Button[24];

		for (int i = 0; i < btns.length; ++i) {
			btns[i] = new Button(btnLabels[i]);
			btns[i].setOnAction(handler);  // Register event handler
			btns[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // full-width
			paneButton.add(btns[i], i % numCols, i / numCols);  // control, col, row

			//Add color to the buttons depending on their label
			switch(btnLabels[i]) {
			case "M+": case "M-": case "MC": case "MR":
				btns[i].setStyle("-fx-color: #989896");
				break;
			case "+": case "-": case "x": case "\u00F7": case "\u221A": case "^":
			case ".": case "=": case"C": case "\u2190":
				btns[i].setStyle("-fx-color: orange");
				break;
			}
		}

		// Setup up the scene graph rooted at a BorderPane (of 5 zones)
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
		root.setTop(this.tfDisplay);     // Top zone contains the TextField
		root.setCenter(paneButton); // Center zone contains the GridPane of Buttons
		root.setBottom(this.memoryText); // Bottom zone contains the Text that shows the memory
		root.setStyle("-fx-background-color: #B6B6B6"); //Change the background color

		// Set up scene and stage
		primaryStage.setScene(new Scene(root, 300, 300));
		primaryStage.setTitle("JavaFX Calculator");
		primaryStage.show();

	}

	/**
	 * Launch and run the App calculator
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
