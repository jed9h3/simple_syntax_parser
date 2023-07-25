import java.io.File;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

public class Driver extends Application {
    public void start(Stage primaryStage) {
        BorderPane bPane = new BorderPane();
        bPane.setStyle("-fx-background-color: #1A1919");
        Label statusL = new Label();
        statusL.setTextFill(Color.WHITE);
        VBox vbox = new VBox(10);
        statusL.setMinSize(265, 40);
        statusL.setFont(new Font("San Francisco", 19));
        Button compB = new Button("open file");
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(compB);
        Scene scene = new Scene(bPane, 500, 200);
        vbox.getChildren().addAll(hbox, statusL);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Parser");
        bPane.setCenter(vbox);
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        compB.setMinSize(200, 40);
        compB.setFont(new Font("San Francisco", 19));
        // =====================================================================
        compB.setOnAction(ie -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select A Source code text file");
            File inputFile = fileChooser.showOpenDialog(primaryStage);
            LexScanner scan = new LexScanner();
            String[] tokens = scan.scanner(inputFile);
            if (tokens == null) {
                System.exit(0);
            }
            Parser pars = new Parser();
            pars.parse(tokens, scan.linesIndicators);
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}