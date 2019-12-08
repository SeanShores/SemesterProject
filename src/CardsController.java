import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardsController {

	static String cardNum[] = new String[4];
	
	/*
	 * Buttons and textfield identifiers.
	 * pnCards is used to hold the cards only in order to not remove anything else when the pane is cleared
	 */

	@FXML
	private Pane pnCards;

	@FXML
	private Button btnSolution;

	@FXML
	private TextField txtTop;

	@FXML
	private Button btnRefresh;

	@FXML
	private TextField txtBottom;

	@FXML
	private Button btnVerify;

	//Find all possible solutions that evaluate to 24 using cardNums
	@FXML
	void findSolution(ActionEvent event) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine js = mgr.getEngineByName("JavaScript");

		String solution = null;
		/*Operations is an array of chars, containing the operations available to evaluate the expression
		 *cardNum is an array that contains the int value of each randomly generated card as a string
		 *Using nested loops, go through all possible combinations of cardNums and operations
		 *So long as there are no duplicate cardNums used, (Checking indexes rather than value)
		 *and the value of res is 24 *check with javascript*, print the possible solutions to console and set the top text to
		 *the most recent solution
		 */
		char[] operations = new char[] { '+', '-', '*', '/' };

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					for(int l = 0; l < 4; l++) {
						for (int m = 0; m < 4; m++) {
							for (int n = 0; n < 4; n++) {
								for (int o = 0; o < 4; o++) {
									try {

										String exp = "" + cardNum[i] + operations[m] + cardNum[j] + operations[n]
												+ cardNum[k] + operations[o] + cardNum[l];
										String res = js.eval(exp).toString();
										if (Double.valueOf(res).intValue() == 24 && i!= j && i != k && i != l
												&& j!=k && j != l && k != l) {
											solution = exp;
											txtTop.setText(exp);
											System.out.println("Possible solution: " + exp);
										}
									}
									catch (ScriptException e) {
										System.out.println("Error");
									}
								}
							}
						}
					}
				}
			}
		}
		if (solution == null) {
			txtTop.setText("No solution");
		}
	}
	//Generate 4 random cards and add them to pnCards
	@FXML
	void refresh(ActionEvent event) {
		
		boolean[] usedCards = new boolean[53];
		int count = 0;	String suite = "", cardType;

		pnCards.getChildren().clear();
		while (count < 4) {
		   /*Declaration statements for card and suiteNum.
			*Randomly generate between 0 and 13 and cast to int
			*Will reroll later if 0 is generated
			*/
			int card = (int) (Math.random() * 14);
			int	suiteNum = (int) (Math.random() * 5);

			/*If statement to set suitNum string for the purpose of getting the filename
			while loop to reroll if math.Random gives a 0
			*/
			while (suiteNum == 0)
				suiteNum = (int) (Math.random() * 5);

			if (suiteNum == 1) {
				suite = "clubs";
			}
			else if (suiteNum == 2) {
				suite = "diamonds";
			}
			else if (suiteNum == 3) {
				suite = "hearts";
			}
			else if (suiteNum == 4) {
				suite = "spades";
			}

			/*If statement to set cardType string for the purpose of getting the filename
			while loop to reroll if math.Random gives a 0
			*/
			while (card == 0)
				card = (int) (Math.random() * 14);

			if (card == 1) {
				cardType = "ace";
			}
			else if (card == 11) {
				cardType = "jack";
			}
			else if (card == 12) {
				cardType = "queen";
			}
			else if (card == 13) {
				cardType = "king";
			}
			else {
				cardType = card + "";
			}

			//Used to allow other methods to see the card numbers
			cardNum[count] = card + "";

			/*
			 * Generate the fileName by using cardType and suite
			 * Using boolean usedCards[], ensure no duplicate cards are used
			 * setScale to fit into the pane
			 * Using count, set the card position to 4 different locations
			 */
			String fileName = "file:Cards/" + cardType + "_of_" + suite + ".png";
			if (!(usedCards[card * suiteNum])) {
				usedCards[card * suiteNum] = true;

				ImageView img = new ImageView(new Image(fileName));
				img.setScaleX(.6);	img.setScaleY(.6);
				img.setTranslateX(count * 121);
				img.setTranslateY(-55);
				count++;

				pnCards.getChildren().add(img);
			}
		}
	}
	
	//Verify checks two things: User entered an equation that equals 24, and used all 4 numbers generated
	@FXML
	int verify(ActionEvent event) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine js = mgr.getEngineByName("JavaScript");

		/*Parse expression to check if numbers are the same as the card values
		*split expression to remove operators, and then uses a for loop to compare card with tokens - the split user entry, 
		*setting cardsValid to true if card and tokens are equal
		*Then an if statement ensures that if any of the four cardsValid are false, it returns 1 and sets the top text
		*to "Use card numbers only!"
		*Lastly set text to "Valid!" if the expression evaluates to 24 *checked with javascript*, or "Invalid!" if otherwise
		*
		*/
		String expression = txtBottom.getText();
		String tokens[] = expression.split("[-+*/]");	
		boolean cardsValid[] = new boolean[4];
		System.out.println("\nExpression checked: " + expression);

		for (int i = 0; i < 4; i++) {
			for (String card : tokens) {
				if (card.equals(cardNum[i]))
					cardsValid[i] = true;
			}
			System.out.println("Card " + (i + 1) + " valid? (" + cardNum[i] + "):" + " " + cardsValid[i]);
		}

		if ( !cardsValid[0] || !cardsValid[1] || !cardsValid[2] || !cardsValid[3]) {
			txtTop.setText("Use card numbers only!");
			return 1;
		}

		if (Math.round( Double.parseDouble("" + js.eval(expression)) ) == 24) {
			txtTop.setText("Valid!");
		}
		else {
			txtTop.setText("Invalid!");
		}
		return 0;
	}

	//Initialize statement that ensures topText cannot be edited, as well as calling the refresh method to generate 4 cards
	@FXML
	public void initialize() {
		txtTop.setEditable(false);
		refresh(null);
	}

}

