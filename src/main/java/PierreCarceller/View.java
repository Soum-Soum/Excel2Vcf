package PierreCarceller;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observer;


public class View extends Application implements Observer {
    private Label errorMessage =  new Label();

    public static void main(String[] args) {
        Application.launch(View.class, args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Excel2Vcf");
        Group root = new Group();
        Scene scene = new Scene(root, 800, 400, Color.web("daebf3"));
        final FileChooser fileChooser = new FileChooser();
        //<Label>
            Label title = new Label("Excel2Vcf");
            title.setStyle("-fx-font-size: 2.5em; -fx-font-weight: bold; -fx-padding: 50 20 50 20;");
            title.setLayoutX(60);
            Label markerFile = new Label("Marker File:");
            Label matrixFile = new Label("Matrix File:");
            Label finalPath = new Label("Final Path");
            errorMessage.setLayoutX(600);
            errorMessage.setLayoutY(25);
            errorMessage.setVisible(false);
        //</Label>

        //<TestField>
            final TextField path1 = new TextField ();
            final TextField path2 = new TextField ();
            final TextField path3 = new TextField ();
            path1.setPrefWidth(scene.getWidth()/1.5);
            path2.setPrefWidth(scene.getWidth()/1.5);
            path3.setPrefWidth(scene.getWidth()/1.5);
        //</TestField>

        //<Button>
        Button convert = new Button("Convert");
        convert.setLayoutX(85);
        convert.setLayoutY(250);
        convert.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                launchConvertion(path1.getText(), path2.getText(),path3.getText());
                errorMessage.setVisible(false);
            }
        });
        Button help = new Button("Help");
        help.setLayoutX(700);
        help.setLayoutY(350);
        Button parcourir1 = new Button("Parcourir");
        parcourir1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String string = fileChooser.showOpenDialog(primaryStage).getPath();
                path1.appendText(string);
            }
        });
        Button parcourir2 = new Button("Parcourir");
        parcourir2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String string = fileChooser.showOpenDialog(primaryStage).getPath();
                path2.appendText(string);
            }
        });
        Button parcourir3 = new Button("Parcourir");
        parcourir3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String string = fileChooser.showSaveDialog(primaryStage).getPath();
                path3.appendText(string);
            }
        });
        //</Button>

        //<Gride>
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10,10,10,10));
            grid.setVgap(5);
            grid.setHgap(5);
            GridPane.setConstraints(markerFile, 0, 0);
            GridPane.setConstraints(matrixFile, 0, 1);
            GridPane.setConstraints(finalPath, 0, 2);
            GridPane.setConstraints(path1, 1, 0);
            GridPane.setConstraints(path2, 1, 1);
            GridPane.setConstraints(path3, 1, 2);
            GridPane.setConstraints(parcourir1, 2, 0);
            GridPane.setConstraints(parcourir2, 2, 1);
            GridPane.setConstraints(parcourir3, 2, 2);
            grid.getChildren().addAll(path1,path2,path3,markerFile,matrixFile,finalPath,parcourir1,parcourir2,parcourir3);
            grid.setLayoutX(75);
            grid.setLayoutY(125);
        //</Gride>

        root.getChildren().addAll(title,convert,grid,help,errorMessage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void launchConvertion(String path1,String path2,String path3){
        Main main = new Main();
        try {
            main.launchConvertion(path1,path2,path3,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(java.util.Observable o, Object arg) {
        errorMessage.setVisible(true);
        errorMessage.setText((String) arg);
    }
}
