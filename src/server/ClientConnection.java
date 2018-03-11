package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends Thread{
    private final ChatServer chatServer;
    private final PrintWriter writer;
    private final Scanner reader;
    private Socket socket;


    ClientConnection(Socket socket, ChatServer chatServer) throws IOException{
        this.socket = socket;
        this.chatServer = chatServer;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new Scanner(socket.getInputStream());
    }

    Socket getSocket() {
        return socket;
    }

    @Override
    public void run(){
        String nickname = reader.nextLine();
        chatServer.addUser(nickname, this);
        while(reader.hasNextLine()){
            broadcastMessage(nickname, reader.nextLine());
        }
    }

    private void broadcastMessage(String sender, String message){
        System.out.println("Received message from " + sender + " : " + message);
        chatServer.getConnectedClients().entrySet()
                .stream()
                .filter(connection -> !connection.getKey().equals(sender))
                .forEach(connection -> connection.getValue().send(sender + ": " + message));
    }

    private void send(String message){
        writer.write(message + "\n");
        writer.flush(); //required to send immediately, no buffer
    }
}
