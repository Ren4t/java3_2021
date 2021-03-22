package network.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.client.controllers.AuthDialogController;
import network.client.controllers.ChatController;
import network.client.models.Network;

import java.util.Arrays;
import java.util.List;

public class NetworkChatClient extends Application {

    public static final List<String> USERS_DATA = Arrays.asList("user1","user2","user3");
    private Stage primaryStage;
    private Stage authDialogStage;
    private Network network;
    private ChatController chatController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)throws Exception {

        this.primaryStage = primaryStage;
        FXMLLoader authLoader = new FXMLLoader();

        authLoader.setLocation(NetworkChatClient.class.getResource("view/authDialog.fxml"));
        Parent authDialogPanel = authLoader.load();
        authDialogStage = new Stage();

        authDialogStage.setTitle("Аутентификация чата");
        authDialogStage.initModality(Modality.WINDOW_MODAL);
        authDialogStage.initOwner(primaryStage);
        Scene scene = new Scene(authDialogPanel);
        authDialogStage.setScene(scene);
        authDialogStage.show();


        network = new Network();
        if (!network.connect()) {
            showNetworkErrorAlert("","Failed to connect");
        }

        AuthDialogController authDialogController = authLoader.getController();
        authDialogController.setNetwork(network);
        authDialogController.setClientApp(this);


        FXMLLoader chatLoader = new FXMLLoader();
        chatLoader.setLocation(NetworkChatClient.class.getResource("view/chatBox.fxml"));

        Parent root = chatLoader.load();
        primaryStage.setTitle("ChatBOX");
        primaryStage.setScene(new Scene(root));
       //primaryStage.show();


        chatController = chatLoader.getController();
       chatController.setNetwork(network);

       //network.waitMessage(chatController);

       primaryStage.setOnCloseRequest(event -> {
           network.close();
       });
    }

    public static void showNetworkErrorAlert(String errorDetails, String errorTitle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Network error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();


    }

    public void openChat() {

        authDialogStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        network.waitMessage(chatController);
    }
}
