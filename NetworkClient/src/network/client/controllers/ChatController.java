package network.client.controllers;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import network.client.NetworkChatClient;
import network.client.models.Network;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.io.IOException;


public class ChatController {

    @FXML
    public ListView<String> usersList;

    @FXML
    public Button sendButton;

    @FXML
    public TextArea chatHistory;

    @FXML
    public TextField textField;
    private Network network;
    //    private String message;
//    private String error_message;
    private String selectedRecipient;

    @FXML
    public void initialize() {
        usersList.setItems(FXCollections.observableArrayList(NetworkChatClient.USERS_DATA));
        sendButton.setOnAction(event -> sendMessage());
        textField.setOnAction(event -> sendMessage());

        //usersList.setCellFactory(new ListView<String>());
        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (! cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell ;
        });
    }

    private void sendMessage() {
        String message = textField.getText();
        appendMessage("Я: " + message);
        textField.clear();

        try {
            if (selectedRecipient != null) {
                network.sendPrivateMessage(message, selectedRecipient);
            } else {
                network.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error_message = "Failed to send message";
            NetworkChatClient.showNetworkErrorAlert(e.getMessage(), error_message);
        }
    }


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) {
        String timestamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timestamp);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(message);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());
        toHistoryLog(message);

    }

    public static void showError(String errorDetails, String errorTitle) {
        NetworkChatClient.showNetworkErrorAlert(errorDetails,errorTitle);
    }

    public void toHistoryLog (String message){
        byte[] outData = message.getBytes();
        try (FileOutputStream out = new FileOutputStream("chatHistoryLog.txt",true)) {
            out.write(outData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void fromHistoryLog(){

        byte[] buf = new byte[20];

        try (FileInputStream in = new FileInputStream("chatHistoryLog.txt")) {
            while ((in.read(buf)) > 0) {
                chatHistory.appendText(new String(buf));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
