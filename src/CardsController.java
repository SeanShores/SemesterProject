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

	@FXML
	void findSolution(ActionEvent event) {
		//Use the card numbers to find a solution that adds to 24
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine js = mgr.getEngineByName("JavaScript");

	    String solution = null;
	    
	    char[] operations = new char[] { '+', '-', '*', '/' };
	    
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            for (int k = 0; k < 4; k++) {
	                try {
	                    String exp = "" + cardNum[0] + operations[i] + cardNum[1] + operations[j]
	                            + cardNum[2] + operations[k] + cardNum[3];
	                    String res = js.eval(exp).toString();
	                    if (Double.valueOf(res).intValue() == 24) {
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
	    if (solution == null) {
	    	txtTop.setText("No solution");
	    }
	}

	@FXML
	void refresh(ActionEvent event) {
		//display 4 random cards
		boolean[] usedCards = new boolean[52];
		int[] validNumbers = new int[4];

		int count = 0;	String suite = "", cardType;
		pnCards.getChildren().clear();
		while (count < 4) {
			//If statement for card suite
			int card = (int) (Math.random() * 13),
					suiteNum = (int) (Math.random() * 4);

			while (suiteNum == 0)
				suiteNum = (int) (Math.random() * 4);

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
			

			//If statement to load correct cards
			while (card == 0)
				card = (int) (Math.random() * 13);

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

			String fileName = "file:Cards/" + cardType + "_of_" + suite + ".png";
			if (!(usedCards[card * suiteNum])) {
				usedCards[card * suiteNum] = true;

				ImageView img = new ImageView(new Image(fileName));
				img.setScaleX(.6);	img.setScaleY(.6);
				img.setTranslateX(count * 121);
				img.setTranslateY(-55);

				int value = card % 13;
				validNumbers[count] = (value == 0) ? 13 : value;
				count++;

				pnCards.getChildren().add(img);
			}
		}
	}

	@FXML
	int verify(ActionEvent event) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine js = mgr.getEngineByName("JavaScript");
        
        //Parse expression to check if numbers are the same as the card values
        String expression = txtBottom.getText();
        String tokens[] = expression.split("[-+*/]");	boolean cardsValid[] = new boolean[4];
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


	@FXML
	public void initialize() {
		txtTop.setEditable(false);
		refresh(null);
	}

}

