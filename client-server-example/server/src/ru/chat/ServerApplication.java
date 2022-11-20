package ru.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {

    private static final int SERVER_PORT = 8081;

    private static final String LULLABY = "\uD83C\uDF1F Twinkle, twinkle, little star,\uD83C\uDF1F\n ⭐ How I wonder what you are.⭐\nUp above the world so high,\n ✨ Like a diamond in the sky. ✨\n\uD83C\uDF1F Twinkle, twinkle, little star, \uD83C\uDF1F\n\uD83D\uDCAB  How I wonder what you are! \uD83D\uDCAB ";
    private static final String CHRISTMAS_SONG = "❄️We wish you a merry Christmas❄\n❄️We wish you a merry Christmas❄\n❄️We wish you a merry Christmas and a happy new year!☃️\uD83C\uDF84 \uD83C\uDF85\uD83C\uDFFC";
    private static final String BUNNY = "(\\__/)\n(='.'=)\n(\")_(\")";
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static BufferedReader input;
    private static BufferedWriter writer;
    private static String message;

    public static void showStartMenu() {
        try {
            writer.write("What will we do now? Write only one word please:\n");
            writer.flush();
            writer.write("\uD83C\uDFB5 \"sing\" to sing a song with me \uD83C\uDFB5 \n");
            writer.flush();
            writer.write("\uD83C\uDFA8 \"draw\" to draw a little picture \uD83C\uDFA8 \n");
            writer.flush();
            writer.write("\uD83D\uDEC1 \"bath\" if you gonna take a shower with me \uD83D\uDEC1 \n");
            writer.flush();
            writer.write("\uD83C\uDF18 \"sleep\" if you wanna go to bed now \uD83C\uDF12 \n");
            writer.flush();
            writer.write("\u274C \"exit\" to live me alone \u274C \n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeChosen(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.printf("IOException with write a message \"%s\"", message);
        }
    }

    public static void choose(String message) {
            if (message.equals("sing") || message.equals("Sing") || message.equals("SING")) {
                writeChosen(CHRISTMAS_SONG);
            } else if (message.equals("draw") || message.equals("Draw") || message.equals("DRAW")) {
                writeChosen(BUNNY);
            } else if (message.equals("bath") || message.equals("Bath") || message.equals("BATH")) {
                writeChosen("Just kidding! \uD83D\uDE04 I'm not waterproof.");
            } else if (message.equals("sleep") || message.equals("Sleep") || message.equals("SLEEP")) {
                writeChosen("I'll sing you a lullaby:");
                writeChosen(LULLABY);
            } else if (message.equals("exit") || message.equals("Exit") || message.equals("EXIT")) {
                writeChosen("Hope to see you later! Bye! \uD83D\uDC4B");
            } else {
                writeChosen("\uD83D\uDE00I'm sorry, but I don't understand you. Please, try again.\uD83E\uDD17");
            }
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Сервер запущен");
            //для получения сообщений от клиента. accept() - ожидает соединение с клиентом
            clientSocket = serverSocket.accept();
            //поток выполнения тут блокируется до тех пор, пока сервер не полут ответ от клиента
            System.out.println("Client started on port: " + clientSocket.getPort());

            try {
                // получаем потоки с clientSocket
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                writeChosen("Hello! I'm glad to see you here.");
                writeChosen("I'm a little chat-bot Lili. And what is your name?");

                message = input.readLine();
                writer.write(String.format("Nice to meet you, %s!\n", message));
                writer.flush();
                do {
                    showStartMenu();
                    message = input.readLine();
                    choose(message);
                } while (!message.equals("exit"));
            } finally {
                input.close();
                writer.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.printf("IOException with create server application in port %s", SERVER_PORT);
        }
    }
}
