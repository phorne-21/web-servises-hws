package ru.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8081;
    private static final int MENU_LENGTH = 6;
    private static Socket socket;
    private static BufferedReader input;
    private static BufferedWriter output;
    private static Scanner scanner;

    public static void showTheAnswer(String message) {
        int number = 0;
        try {
            if (message.equals("sing") || message.equals("draw") ||
                    message.equals("Sing") || message.equals("SING") ||
                    message.equals("Draw") || message.equals("DRAW")) {
                number = 3;
            } else if (message.equals("sleep") ||
                        message.equals("Sleep") ||
                        message.equals("SLEEP")) {
                number = 7;
            } else {
                number = 1;
            }
            for(int i = 0; i < number; i++) {
                System.out.println(input.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);

            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // читаем Hello
                System.out.println(input.readLine());
                System.out.println(input.readLine());
                // считываем с консоли ответ
                scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                // передаём ответ серверу
                output.write(message + "\n");
                output.flush();
                System.out.println(input.readLine());
                // получаем ответ от сервера и выводим его
                do {
                    for (int i = 0; i < MENU_LENGTH; i++) {
                        System.out.println(input.readLine());
                    }
                    scanner = new Scanner(System.in);
                    message = scanner.nextLine();
                    output.write(message + "\n");
                    output.flush();
                    showTheAnswer(message);
                } while (!message.equals("exit"));
            } finally {
                input.close();
                output.close();
            }
        } catch (IOException e) {
            System.out.printf("Cannot connection problem with host %s and  port %s", SERVER_HOST, SERVER_PORT);
        }
    }
}
