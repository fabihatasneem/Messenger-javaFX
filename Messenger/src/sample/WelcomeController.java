package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WelcomeController{
    Client client;
    NetworkUtil networkUtil;
    Thread thr;

    @FXML
    private Label welcome;

    @FXML
    private Label name;

    @FXML
    private TextField nameField;

    @FXML
    private Label password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createButton;

    @FXML
    private Button loginButton;

    @FXML
    void createAction(){
        try{
            client.showCreateAccountPage();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loginAction() {
        try {
            String s = "L#"+nameField.getText()+"#"+passwordField.getText();
            networkUtil.write(s);
            Object o = networkUtil.read();
            if(o!=null) {
                String received = (String) o;
                if (received.equals("SUCCESS")) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("SUCCESSFUL");
                    alert.setHeaderText("Log In Successful!");
                    alert.showAndWait();

                    client.showUserList();

                } else if (received.equals("FAILURE")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid Credentials!");
                    alert.setContentText("Email or Password Invalid. Please check again.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("User Already Logged In!");
                    alert.setContentText("You are logged in from another window already.");
                    alert.showAndWait();
                }
            }else
                System.out.println("Object is null");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient(Client client){ this.client = client; }

    public void setNetworkUtil(NetworkUtil networkUtil){ this.networkUtil = networkUtil; }
}
