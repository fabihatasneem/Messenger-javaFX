package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UsersController implements Initializable {
    Client client;
    NetworkUtil networkUtil;
    private static ArrayList<String> myList;

    @FXML
    private ListView<String> listView = new ListView<>();

    @FXML
    private AnchorPane layout;

    @FXML
    private Label title;

    @FXML
    private Button logoutButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button refreshButton;

    @FXML
    void chatAction() {
        ObservableList<String> selectedUsers = listView.getSelectionModel().getSelectedItems();
        ArrayList<String> names = new ArrayList<>(selectedUsers);
        client.Message(names);
    }

    @FXML
    void logoutAction() {
        try {
            networkUtil.write("Logout");
            client.showWelcomePage();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Successful");
            alert.setHeaderText("Logout Successful!");
            alert.setContentText("You are now logged out. Please log in again to chat.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR!!!");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void refreshAction(){
        printList();
    }

    public void printList() {
        try {
            networkUtil.write("P");
            listView.getItems().clear();
            listView.getItems().add("Group");
            Object o = networkUtil.read();
            if(o!=null) {
                myList = (ArrayList<String>) o;
                for (String name : myList) {
                    listView.getItems().add(name);
                }
            }else{
                System.out.println("O is null");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setClient(Client client) { this.client = client;}

    public void setNetworkUtil(NetworkUtil networkUtil){ this.networkUtil = networkUtil; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}