import java.util.HashSet;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardsController {

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
	void verify(ActionEvent event) {
		char ch = '?';
		if (ch == '(' || ch == ')' || ch == '/' || ch == '+' || ch == '-' || ch == '*') {
			//see if expression adds up to 24?
		}
	}


	@FXML
	public void initialize() {
		refresh(null);
	}

}

