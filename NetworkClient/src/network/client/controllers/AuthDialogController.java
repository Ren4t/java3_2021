package network.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import network.client.NetworkChatClient;
import network.client.models.Network;

public class AuthDialogController {
    private @FXML TextField loginField;
    private @FXML PasswordField passwordField;
    private @FXML Button authButton;

    private Network network;
    private NetworkChatClient clientApp;

    @FXML
    public void executeAuth(ActionEvent actionEvent) {

        String login = loginField.getText();
        String password = passwordField.getText();
        if(login == null || password == null){
            NetworkChatClient.showNetworkErrorAlert("Username and password should be not empty!","Auth error");
            return;
        }

        String authError = network.sendAuthCommand(login, password);
        if(authError == null ){
            //network.setChatMode();
            clientApp.openChat();
        }else {
            NetworkChatClient.showNetworkErrorAlert(authError, "Auth error");
        }

    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setClientApp(NetworkChatClient clientApp) {
        this.clientApp = clientApp;
    }
}
