package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class Client extends Application{
    public static NetworkUtil networkUtil;
    Stage window;
    Thread thr;
    public static TextFlow textFlow = new TextFlow();
    String serverAddress = "127.0.0.1";
    int serverPort = 44444;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        showWelcomePage();
    }

    public void showWelcomePage() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("welcome.fxml"));
        Parent root = loader.load();
        WelcomeController controller = loader.getController();
        controller.setClient(this);
        controller.setNetworkUtil(networkUtil);
        window.setTitle("Messenger");
        window.setScene(new Scene(root, 600, 450));
        window.show();
    }

    public void showCreateAccountPage() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("createaccount.fxml"));
        Parent root = loader.load();
        CreateAccountController controller = loader.getController();
        controller.setClient(this);
        controller.setNetworkUtil(networkUtil);
        window.setTitle("Create Account in Messenger");
        window.setScene(new Scene(root, 600, 450));
        window.show();
    }

    public void showUserList() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("users.fxml"));
        Parent root = loader.load();
        UsersController controller = loader.getController();
        controller.setClient(this);
        controller.setNetworkUtil(networkUtil);
        controller.printList();
        window.setTitle("Users");
        window.setScene(new Scene(root, 600, 450));
        window.show();
    }

    public void Message(ArrayList<String> names){
        this.thr=new Thread(new ReadClient());
        thr.start();

        AnchorPane root = new AnchorPane();
        root.setPrefHeight(400);
        root.setPrefWidth(600);

        textFlow.setLayoutX(0);
        textFlow.setLayoutY(52.0);
        textFlow.setPrefHeight(250.0);
        textFlow.setPrefWidth(600.0);

        Label title = new Label("My Messages");
        title.setLayoutX(270);
        title.setLayoutY(20);

        Label newMessage = new Label("New Message");
        newMessage.setLayoutX(65);
        newMessage.setLayoutY(313);

        TextArea typing = new TextArea();
        typing.setLayoutX(62.0);
        typing.setLayoutY(339.0);
        typing.setPrefHeight(53.0);
        typing.setPrefWidth(400.0);

        Button sendButton = new Button("Send");
        sendButton.setLayoutX(480.0);
        sendButton.setLayoutY(355.0);
        sendButton.setMnemonicParsing(false);
        sendButton.setOnAction(e -> sendAction(names,typing.getText()));

        Button backButton2 = new Button("Back");
        backButton2.setLayoutX(15.0);
        backButton2.setLayoutY(15.0);
        backButton2.setMnemonicParsing(false);
        backButton2.setOnAction(e -> backAction2());

        window.setTitle("Messenger");
        root.getChildren().addAll(title, newMessage, sendButton, backButton2, typing, textFlow);
        window.setScene(new Scene(root, 600, 450));
        window.show();
    }

    void sendAction(ArrayList<String> names, String text){
        for(String each : names){
            String s = "M#"+each+"#"+text;
            networkUtil.write(s);
        }
    }

    void backAction2() {
        try {
            networkUtil.write(false);
            thr.interrupt();
            showUserList();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
class ReadClient implements Runnable{
    public void run(){
        try {
            while (!Thread.interrupted()) {
                Object o = Client.networkUtil.read();
                if(o instanceof Boolean){
                    Boolean b = (Boolean)o;
                    if(b.equals(false)) {
                        break;
                    }
                } else if (o != null) {
                    String s = (String)o;
                    Text t = new Text(s);
                    Platform.runLater(() -> Client.textFlow.getChildren().add(t));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}