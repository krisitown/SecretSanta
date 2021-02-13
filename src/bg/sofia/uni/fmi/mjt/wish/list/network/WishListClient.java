package bg.sofia.uni.fmi.mjt.wish.list.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WishListClient {
    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if ("disconnect".equals(message)) {
                    break;
                }

                writer.println(message);

                String reply = reader.readLine();
                System.out.println("[ " + reply + " ]");
            }

            System.out.println("Disconnected from server");

        } catch (IOException e) {
            System.out.println("There is a problem with the network communication");
            e.printStackTrace();
        }
    }
}
