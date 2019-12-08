import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * The main function, which uses FXMLLoader to display everything, as well as setting the title
 */

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage pStage) throws Exception {
		Parent p = FXMLLoader.load(getClass().getResource("CardsLayout.fxml"));
		Scene cards = new Scene(p);
		
		pStage.setTitle("Playing Cards 24");
		pStage.setScene(cards);
		pStage.show();
	}

}
