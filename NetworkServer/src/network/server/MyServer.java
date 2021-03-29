package network.server;

import network.server.auth.AuthServise;
import network.server.auth.BaseAuthService;
import network.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AuthServise authServise;
    ExecutorService executorService;

    public MyServer(int port) throws IOException {
        executorService = Executors.newFixedThreadPool(2);
        this.serverSocket = new ServerSocket(port);
        this.authServise = new BaseAuthService();
    }

    public void start(int port) throws IOException {
        System.out.println("Server started!");
        authServise.start();
        try {
            while (true) {
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            System.err.println("Failed to accept new connection");
            e.printStackTrace();
        } finally {
            authServise.stop();
            serverSocket.close();
            executorService.shutdown();
        }
    }

    private void waitAndProcessNewClientConnection() throws IOException {
        System.out.println("Waiting  . . .");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connected!");
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        //clients.add(clientHandler);
        clientHandler.handle(executorService);
    }

    public AuthServise getAuthServise() {
        return authServise;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender, boolean isServerInfoMsg) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(isServerInfoMsg ? null : sender.getUsername(), message);
        }
    }

    public synchronized void subscribe(ClientHandler handler) {
        clients.add(handler);
    }

    public synchronized void unsubscribe(ClientHandler handler) {
        clients.remove(handler);
    }

    public synchronized boolean isNicknameAlreadyBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void sendPrivateMessage(ClientHandler sender, String recipient, String privateMessage) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(recipient)) {
                client.sendMessage(sender.getUsername(), privateMessage);
            }
        }
    }
}
