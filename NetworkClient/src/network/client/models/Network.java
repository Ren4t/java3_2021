package network.client.models;

import javafx.application.Platform;
import network.client.controllers.ChatController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 17197;

    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX = "/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";
    private static final String PRIVATE_MSG_CMD_PREFIX = "/w";
    private static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg";
    private static final String SERVER_MSG_CMD_PREFIX = "/serverMsg";
    private static final String END_CMD= "/end";

    private final String host;
    private final int port;
    DataOutputStream outputStream;
    DataInputStream inputStream;
    private Thread thread;
    private Socket clientSocket;
    private String username;

    public Network(){
        this(SERVER_ADDRESS,SERVER_PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {

        try {
            clientSocket = new Socket(host,port);
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting");
            e.printStackTrace();
            return false;
        }
    }

    public String sendAuthCommand(String login, String password){
        try {
            getOutputStream().writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = getInputStream().readUTF();
            if(response.startsWith(AUTHOK_CMD_PREFIX)){
                this.username = response.split("\\s+",2)[1];
                return null;
            }else{
                return response.split("\\s+",2)[1];
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }


    public void waitMessage(ChatController chatController) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        String message = inputStream.readUTF();
                        if (message.startsWith(CLIENT_MSG_CMD_PREFIX)) {
                            String[] parts = message.split("\\s+",3);
                            String sender = parts[1];
                            String msgBody = parts[2];
                            Platform.runLater(() ->
                            {
                                chatController.appendMessage(String.format("%s %s", sender, msgBody));
                            });

                        } else if(message.startsWith(SERVER_MSG_CMD_PREFIX)) {
                            String[] parts = message.split("\\s+",2);
                            Platform.runLater(() ->
                            {
                                chatController.appendMessage(parts[1]);
                            });
                        }else {
                            Platform.runLater(() ->
                            {
                                chatController.showError("Unknown command from server!", message);
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Disconnect");
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChatMode() {
        waitMessage(null);
    }

    public String getUsername() {
        return username;
    }


    public void sendPrivateMessage(String message, String recipient) {
        String command = String.format("%s %s %s",PRIVATE_MSG_CMD_PREFIX, recipient,message);
        sendMessage(command);
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
