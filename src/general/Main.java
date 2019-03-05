package general;

import general.messages.Message;
import general.users.User;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static general.users.Account.*;
import static java.nio.file.StandardOpenOption.APPEND;

public class Main {

    public static final Path usersFile = Paths.get("src/general/users/users.txt");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        Message message = new Message();
        int userDecision;

        // SignUp / LogIn part
        // 1 - SignUp
        while (!user.isLoggedIn()) {
            String userName;
            String userPassword;

            System.out.println("What do you want to do: 1-SignUp, 2-LogIn.");
            userDecision = scanner.nextInt();
            scanner.nextLine();
            if (userDecision == 1) {
                System.out.println("Please enter your name:");
                while (true) {
                    userName = scanner.nextLine();
                    if (!isTaken(userName)) {
                        user.setName(userName);
                        break;
                    }
                }
                System.out.println("Please enter your password:");
                user.setPassword(scanner.nextLine());
                Files.write(usersFile, (user.getName() + "," + user.getPassword() + "\n").getBytes(), APPEND);
                System.out.println("Account created.");
                user.setLoggedIn(true);

                // 2 - LogIn
            } else if (userDecision == 2) {
                while (!user.isLoggedIn()) {
                    System.out.println("Please enter your name");
                    userName = scanner.nextLine();
                    if (allUsers().containsKey(userName)) {
                        while (!user.isLoggedIn()) {
                            System.out.println("Please enter your password");
                            userPassword = scanner.nextLine();
                            if (isCorrect(userName, userPassword)) {
                                user.setName(userName);
                                user.setLoggedIn(true);
                                System.out.println("Welcome " + user.getName() + "!");
                            }
                        }
                    } else {
                        System.out.println("There is no account with such name");
                    }
                }
            } else {
                System.out.println("Only commands: 1 and 2 are allowed.");
            }
        }
        // Write message / Read message / LogOut part
        while (user.isLoggedIn()) {
            new Thread(user).start();

            System.out.println("What do you want to do: 1-Write a message, 2-Read incoming messages, 3-LogOut");
            userDecision = scanner.nextInt();
            scanner.nextLine();

            // 1 - Write a message
            if (userDecision == 1) {
                while (true) {
                    System.out.println("Please enter Recipient name: ");
                    String recipientName = scanner.nextLine();
                    if (allUsers().containsKey(recipientName)) {
                        System.out.println("Please enter your message:");
                        message.setContent(scanner.nextLine());
                        message.setRecipient(recipientName);
                        String fileName = message.getRecipient() + ".txt";

                        if (!Files.exists(Paths.get(fileName))) {
                            Files.createFile(Paths.get(fileName));
                            Files.write(Paths.get(fileName), ("2" + "\n" +"0" + "\n").getBytes());
                        }

                        Files.write(Paths.get(fileName), (user.getName() + " wrote: " + "\n" + message.getContent() +
                                "\n").getBytes(), APPEND);
                        System.out.println("Message sent.");

                        //part where new message mark is added for new message thread.
                        List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                        messageLines.set(1, String.valueOf(1));
                        Files.write(Paths.get(fileName), messageLines);
                        break;
                    } else {
                        System.out.println("There is no such recipient in the UserMap");
                    }
                }
            }
            // 2 - Read a messages
            else if (userDecision == 2) {
                while (true) {
                    System.out.println("What would you like to read: 1 - New messages, 2 - All messages, 3 - Back");
                    userDecision = scanner.nextInt();
                    scanner.nextLine();
                    String fileName = user.getName() + ".txt";
                    boolean exists = Files.exists(Paths.get(fileName));

                    if (!exists) {
                        System.out.println("There is no messages for you.");
                        break;
                    }
                    // New messages
                    if (userDecision == 1) {
                        List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                        int readMessages = Integer.parseInt(messageLines.get(0));
                        messageLines.set(0, String.valueOf(messageLines.size()));

                        System.out.println("--------------------------------------");
                        for (int i = readMessages; i < messageLines.size(); i++) {
                            System.out.println(messageLines.get(i));
                        }

                        System.out.println("--------------------------------------");
                        Files.write(Paths.get(fileName), messageLines);
                        break;

                        // All messages
                    } else if (userDecision == 2) {
                        List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                        System.out.println("--------------------------------------");
                        for (int i = 2; i < messageLines.size(); i++) {
                            System.out.println(messageLines.get(i));
                        }
                        System.out.println("--------------------------------------");
                        break;
                    } else if (userDecision == 3) {
                        break;
                    } else {
                        System.out.println("Only Commands 1-3 are allowed");
                    }
                }
            }
            // 3 - LogOut
            else if (userDecision == 3) {
                System.out.println("See you next time!");
                user.setLoggedIn(false);
            }
            // none above
            else {
                System.out.println("Only Commands 1-3 are allowed");
            }

        }

    }
}
