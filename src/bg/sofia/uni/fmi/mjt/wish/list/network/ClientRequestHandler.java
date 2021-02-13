package bg.sofia.uni.fmi.mjt.wish.list.network;

import bg.sofia.uni.fmi.mjt.wish.list.models.User;
import bg.sofia.uni.fmi.mjt.wish.list.models.WishList;
import bg.sofia.uni.fmi.mjt.wish.list.services.UserService;
import bg.sofia.uni.fmi.mjt.wish.list.services.WishListService;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientRequestHandler implements Runnable {
    private Socket socket;
    private WishListService wishListService;
    private UserService userService;

    public ClientRequestHandler(Socket socket, Map<String, Set<String>> presents, Map<String, User> users) {
        this.socket = socket;
        this.wishListService = new WishListService(presents);
        this.userService = new UserService(users);
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Message received from client: " + inputLine);
                String[] tokens = inputLine.split("\\s+");
                if(tokens[0].equalsIgnoreCase("get-wish")){
                    if(requireAuthentication(out)) continue;
                    WishList result = this.wishListService.getWishList(this.userService.getPrincipal().getUsername());
                    if(result.getStudent() != null){
                        out.println(String.format("%s: [%s]", result.getStudent(), String.join(", ", result.getWishList())));
                    } else {
                        out.println("");
                    }
                } else if(tokens[0].equalsIgnoreCase("post-wish")){
                    if(requireAuthentication(out)) continue;
                    String studentName = tokens[1];
                    if(!this.userService.getRegisteredUsernames().contains(studentName)){
                        out.println(String.format("Student with username %s is not registered", studentName));
                    }
                    String result = this.wishListService.postWish(tokens[1], tokens[2]);
                    out.println(result);
                } else if(tokens[0].equalsIgnoreCase("register")) {
                    out.println(this.userService.register(tokens[1], tokens[2]));
                } else if(tokens[0].equalsIgnoreCase("login")) {
                    out.println(this.userService.login(tokens[1], tokens[2]));
                } else if(tokens[0].equalsIgnoreCase("logout")) {
                    out.println(this.userService.logout());
                } else {
                    out.println("Invalid command sent!");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean requireAuthentication(PrintWriter out){
        if(this.userService.getPrincipal() == null) {
            out.println("You are not logged in");
            return true;
        }
        return false;
    }
}
