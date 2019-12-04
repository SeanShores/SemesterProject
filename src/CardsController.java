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
	private ImageView img0;

	@FXML
	void findSolution(ActionEvent event) {
		//Use the card numbers to find a solution that adds to 24
		ScriptEngineManager sem = new ScriptEngineManager();
	    ScriptEngine engine = sem.getEngineByName("javascript");

	    String solution = null;
	    for (int i = 0; i < 4; i++) {
	    	System.out.println("Card " + i + ": " + cardNum[i]);
	    }
	    
	    char[] operations = new char[] { '+', '-', '*', '/' };
	    
	    for (int i = 0; i < 4; i++) {
	        for (int j = 0; j < 4; j++) {
	            for (int k = 0; k < 4; k++) {
	            	
	                try {
	                    String exp = "" + cardNum[0] + operations[i] + cardNum[1] + operations[j]
	                            + cardNum[2] + operations[k] + cardNum[3];
	                    String res = engine.eval(exp).toString();
	                    if (Double.valueOf(res).intValue() == 24) {
	                        solution = exp;
	                        txtTop.setText(exp);
	                        System.out.println("Solution: " + exp);
	                    }
	                } catch (ScriptException e) {
	                    System.out.println("Error");
	                }
	            }
	        }
	    }
	    if (solution == null) {
	    	txtTop.setText("No solution");
	    	System.out.println("No Solution");
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
			

			//If statement for card numbers
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
			
			cardNum[count] = card + "";
			System.out.println("Card number "+ count + " = " + cardNum[count]);

			String fileName = "file:Cards/" + cardType + "_of_" + suite + ".png";
			if (!(usedCards[card * suiteNum])) {
				usedCards[card * suiteNum] = true;

				img0 = new ImageView(new Image(fileName));
				img0.setScaleX(.6);	img0.setScaleY(.6);
				img0.setTranslateX(count * 121);
				img0.setTranslateY(-55);

				int value = card % 13;
				validNumbers[count] = (value == 0) ? 13 : value;
				count++;

				pnCards.getChildren().add(img0);
			}
		}
	}

	@FXML
	int verify(ActionEvent event) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine js = mgr.getEngineByName("JavaScript");
        
        //See if spaces can be added in between the operators to recognize
        //valid numbers in the expression?
	    String expression = "" + js.eval( txtBottom.getText() );
	    boolean validOperator = false;
        
        if (expression.contains("+") || expression.contains("-") ||
        		expression.contains("*") || expression.contains("/")) {
        	validOperator = true;
        }
        
        //For some reason the cards are showing up invalid even
        //though they are valid.  WHY?????
        for (int i = 0; i < 4; i++) {
        	System.out.print("Card " + i + " valid? ");
        	if (expression.contains(cardNum[i] + "")) {
        		System.out.println(cardNum[i] + ": True");
        	}
        	else {
        		System.out.println(cardNum[i] + ": False");
        	}
        } 
        
	    
	    if ( !expression.contains(cardNum[0] + "") || !expression.contains(cardNum[1] + "") ||
	    		!expression.contains(cardNum[2] + "") || !expression.contains(cardNum[3] + "") ||
	    		!expression.contains("+") || !validOperator) {
	    	System.out.println("Not right dude");
	    	txtTop.setText("Use card numbers only!");
	    	return 1;
	    	
	    }
	    
	    int exp = Math.round( Integer.parseInt(expression) );
	    System.out.println(exp);
	    
	    if (exp == 24) {
			txtTop.setText("Valid!");
		}
		else {
			txtTop.setText("Invalid!");
		}
	    return 0;
	}


	@FXML
	public void initialize() {
		refresh(null);
	}

}

