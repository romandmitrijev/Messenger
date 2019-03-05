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
        String userDecision;

        // SignUp / LogIn part
        // 1 - SignUp
        while (!user.isLoggedIn()) {
            String userName;
            String userPassword;

            System.out.println("What do you want to do: 1-SIGNUP, 2-LOGIN.");
            userDecision = scanner.nextLine();
            if (userDecision.equals("1") || userDecision.equalsIgnoreCase("signup")) {
                System.out.println("Please enter your name or type back to go back:");
                while (true) {
                    String nameInput = scanner.nextLine();
                    if (nameInput.equalsIgnoreCase("break")) {
                        break;
                    }
                    userName = nameInput;
                    if (!isTaken(userName)) {
                        user.setName(userName);
                        break;
                    }
                }
                System.out.println("Please enter your password:");
                user.setPassword(scanner.nextLine());
                user.setFileName(user.getName() + ".txt");
                Files.write(usersFile, (user.getName() + "," + user.getPassword() + "\n").getBytes(), APPEND);

                Files.createFile(Paths.get(user.getFileName()));
                Files.write(Paths.get(user.getFileName()), ("2" + "\n" + "0" + "\n").getBytes());
                user.setLoggedIn(true);
                System.out.println("Account created.");

                // 2 - LogIn
            } else if (userDecision.equals("2") || userDecision.equalsIgnoreCase("login")) {

                while (!user.isLoggedIn()) {
                    int passTries = 3;
                    System.out.println("Please enter your name:");
                    userName = scanner.nextLine();
                    if (allUsers().containsKey(userName)) {
                        while (!user.isLoggedIn() && passTries >= 0) {
                            System.out.println("Please enter your password:");
                            userPassword = scanner.nextLine();
                            if (isCorrect(userName, userPassword)) {
                                user.setName(userName);
                                user.setLoggedIn(true);
                                System.out.println("Welcome " + user.getName() + "!");
                            } else {
                                System.out.println(passTries + " tries left.");
                                passTries--;
                            }
                        }
                    } else {
                        System.out.println("There is no account with such name.");
                        break;
                    }
                }
            } else {
                System.out.println("No such command");
            }
        }
        // Write message / Read message / LogOut part
        while (user.isLoggedIn()) {
            new Thread(user).start();

            System.out.println("What do you want to do: 1-WRITE a message, 2-READ incoming messages, 3-LOGOUT");
            userDecision = scanner.nextLine();

            // 1 - Write a message
            if (userDecision.equals("1") || userDecision.equalsIgnoreCase("write")) {
                while (true) {
                    System.out.println("Please enter Recipient name: ");
                    String recipientName = scanner.nextLine();
                    if (allUsers().containsKey(recipientName)) {
                        System.out.println("Please enter your message:");

                        message.setContent(scanner.nextLine());
                        message.setRecipient(recipientName);
                        String recipientFileName = message.getRecipient() + ".txt";
                        Files.write(Paths.get(recipientFileName), (user.getName() + " wrote: " + "\n" + message.getContent() +
                                "\n").getBytes(), APPEND);
                        System.out.println("Message sent.");

                        //part where new message mark is added for new message thread.
                        List<String> messageLines = Files.readAllLines(Paths.get(recipientFileName));
                        messageLines.set(1, String.valueOf(1));
                        Files.write(Paths.get(recipientFileName), messageLines);
                        break;
                    } else {
                        System.out.println("There is no such recipient in the UserMap");
                    }
                }
            }
            // 2 - Read a messages
            else if (userDecision.equals("2") || userDecision.equalsIgnoreCase("read")) {
                while (true) {
                    System.out.println("What would you like to read: 1 - NEW messages, 2 - ALL messages, 3 - BACK");
                    userDecision = scanner.nextLine();
                    String fileName = user.getName() + ".txt";
                    boolean exists = Files.exists(Paths.get(fileName));

                    if (!exists) {
                        System.out.println("There is no messages for you.");
                        break;
                    }
                    // New messages
                    if (userDecision.equals("1") || userDecision.equalsIgnoreCase("new")) {
                        List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                        int readMessages = Integer.parseInt(messageLines.get(0));
                        if (readMessages == messageLines.size()) {
                            System.out.println("--------------------------------------" + "\n"
                                    + " You have no new messages" + "\n"
                                    + "--------------------------------------");
                        } else {
                            messageLines.set(0, String.valueOf(messageLines.size()));

                            System.out.println("--------------------------------------");
                            for (int i = readMessages; i < messageLines.size(); i++) {
                                System.out.println(messageLines.get(i));
                            }

                            System.out.println("--------------------------------------");
                            Files.write(Paths.get(fileName), messageLines);
                            break;
                        }
                        // All messages
                    } else if (userDecision.equals("2") || userDecision.equalsIgnoreCase("all")) {
                        List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                        messageLines.set(0, String.valueOf(messageLines.size()));

                        System.out.println("--------------------------------------");
                        for (int i = 2; i < messageLines.size(); i++) {
                            System.out.println(messageLines.get(i));
                        }
                        System.out.println("--------------------------------------");

                        Files.write(Paths.get(fileName), messageLines);
                        break;
                    } else if (userDecision.equals("3") || userDecision.equalsIgnoreCase("back")) {
                        break;
                    } else {
                        System.out.println("No such command");
                    }
                }
            }
            // 3 - LogOut
            else if (userDecision.equals("3") || userDecision.equalsIgnoreCase("logout")) {
                System.out.println("See you next time!");
                user.setLoggedIn(false);
            }
            // none above
            else {
                System.out.println("No such command");
            }

        }

    }
}
