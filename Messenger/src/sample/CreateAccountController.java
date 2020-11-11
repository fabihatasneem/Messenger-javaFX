package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.lang.*;

public class CreateAccountController {
    Client client;
    NetworkUtil networkUtil;

    @FXML
    private Label createAccountHere;

    @FXML
    private Label username;

    @FXML
    private TextField usernameField;

    @FXML
    private Label email;

    @FXML
    private TextField emailField;

    @FXML
    private Label password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;

    @FXML
    void submitAction() {
        String s = "C#" + usernameField.getText() + "#" + emailField.getText() + "#" + passwordField.getText();
        networkUtil.write(s);
        Object o = networkUtil.read();
        if(o!=null) {
            String received = (String) o;
            if (received.equals("SUCCESS")) {
                Alert alert;
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("SUCCESSFUL");
                alert.setHeaderText("SUCCESSFUL!!!");
                alert.setContentText("Account Created. Now you can chat with your friends for free using Messenger.");
                alert.showAndWait();
            } else if (received.equals("Account exists")) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("ACCOUNT ALREADY EXISTS!");
                alert.setContentText("Duplicate account cannot be created. Please log in.");
                alert.showAndWait();
            } else if (received.equals("Username taken")) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("USERNAME UNAVAILABLE!");
                alert.setContentText("This username already exists. Please use another.");
                alert.showAndWait();
            } else if (received.equals("Email taken")) {
                Alert alert;
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("EMAIL UNAVAILABLE!");
                alert.setContentText("This email is already taken. Please use another.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void backAction(){
        try {
            client.showWelcomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setNetworkUtil(NetworkUtil networkUtil){ this.networkUtil = networkUtil; }
}