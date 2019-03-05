package general.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class User implements Runnable {
    private String name;
    private String password;
    private boolean isLoggedIn;
    private String fileName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            String fileName = name + ".txt";

            while (isLoggedIn) {
                Thread.sleep(200);
                List<String> messageLines = Files.readAllLines(Paths.get(fileName));
                int newMessages = Integer.parseInt(messageLines.get(1));
                if (newMessages>0) {
                    System.out.println("**************************");
                    System.out.println("* You have a new message *");
                    System.out.println("**************************");
                    messageLines.set(1, String.valueOf(0));
                    Files.write(Paths.get(fileName), messageLines);
                    Thread.sleep(1000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

