import messages.Message;
import users.Account;
import users.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;

public class Main {
    private static Path usersFile = Paths.get("C:\\Git\\Messenger\\src\\users\\users.txt");


    public static void main(String[] args) throws IOException {
        Account account = new Account();
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
                    if (!account.isTaken(userName)) {
                        user.setName(userName);
                        break;
                    }
                }
                System.out.println("Please enter your password:");
                user.setPassword(scanner.nextLine());
                Files.write(usersFile, (user.getName() + "," + user.getPassword() + "\n").getBytes(), APPEND);
                System.out.println("users.Account created.");
                user.setLoggedIn(true);

                // 2 - LogIn
            } else if (userDecision == 2) {
                while (!user.isLoggedIn()) {
                    System.out.println("Please enter your name");
                    userName = scanner.nextLine();
                    if (account.accountListReader().containsKey(userName)) {
                        while (!user.isLoggedIn()) {
                            System.out.println("Please enter your password");
                            userPassword = scanner.nextLine();
                            if (account.isCorrect(userName, userPassword)) {
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
        // Read / Write message / LogOut part
        while (user.isLoggedIn()) {
            System.out.println("What do you want to do: 1-Write a message, 2-Read incoming messages, 3-LogOut");
            userDecision = scanner.nextInt();
            scanner.nextLine();
            // 1 - Write a message
            if (userDecision == 1) {
                while (true) {
                    System.out.println("Please enter Recipient name: ");
                    String recipientName = scanner.nextLine();
                    if (account.accountListReader().containsKey(recipientName)) {
                        System.out.println("Please enter your message:");
                        message.setContent(scanner.nextLine());
                        message.setRecipient(recipientName);
                        if (Files.exists(Paths.get(message.getRecipient() + ".txt"))) {
                            Files.write(Paths.get(message.getRecipient() + ".txt"), (message.getContent() + "\n").getBytes(), APPEND);
                            System.out.println("Message sent.");
                        } else {
                            Files.createFile(Paths.get(message.getRecipient() + ".txt"));
                            Files.write(Paths.get(message.getRecipient() + ".txt"), ("1" + "\n").getBytes());
                            Files.write(Paths.get(message.getRecipient() + ".txt"), (message.getContent() + "\n").getBytes(), APPEND);
                            System.out.println("Message sent.");
                        }
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
                    if (userDecision == 1 && Files.exists(Paths.get(user.getName() + ".txt"))) {
                        List<String> messageLines = Files.readAllLines(Paths.get(user.getName() + ".txt"));
                        int readMessages = Integer.parseInt(messageLines.get(0));
                        messageLines.set(0, String.valueOf(messageLines.size()));
                        System.out.println("--------------------------------------");
                            for (int i = readMessages; i<messageLines.size(); i++ ){
                                System.out.println(messageLines.get(i));
                            }
                        System.out.println("--------------------------------------");
                            Files.write(Paths.get(user.getName() + ".txt"), messageLines);

                    } else if (userDecision == 2 && Files.exists(Paths.get(user.getName() + ".txt"))) {
                        List<String> messageLines = Files.readAllLines(Paths.get(user.getName() + ".txt"));
                        System.out.println("--------------------------------------");
                        for (int i = 1; i< messageLines.size(); i++) {
                            System.out.println(messageLines.get(i));
                        }
                        System.out.println("--------------------------------------");
                    } else if (userDecision == 3){
                        break;
                    }else {
                        System.out.println("There is no messages for you.");
                    }
                }
            }
            // 3 - LogOut
            else {
                System.out.println("See you next time!");
                user.setLoggedIn(false);
            }
        }


    }
}
