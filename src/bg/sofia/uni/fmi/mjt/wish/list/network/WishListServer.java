package bg.sofia.uni.fmi.mjt.wish.list.network;

import bg.sofia.uni.fmi.mjt.wish.list.models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishListServer {
    private static final ConcurrentHashMap<String, Set<String>> presents = new ConcurrentHashMap<>();
    private static final HashMap<String, User> users = new HashMap<>();
    private static ServerSocket socket;

    public static void main(String[] args) {
        int port = 4444;
        if(args.length > 0) {
            Integer.parseInt(args[0]);
        }
        int executorThreads = 10;
        if(args.length > 1){
            executorThreads = Integer.parseInt(args[1]);
        }
        ExecutorService executor = Executors.newFixedThreadPool(executorThreads);

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            socket = serverSocket;
            Socket clientSocket;

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());
                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, presents, users);
                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            System.out.println("There is a problem with the server socket");
            e.printStackTrace();
        }
    }


    /*
    * This is because I found out at last minute that the Grader uses a different structure
    * in order to test the classes...
    * */

    private int serverPort;

    public WishListServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public void start(){
        main(new String[] {Integer.toString(this.serverPort)});
    }

    public void stop(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
