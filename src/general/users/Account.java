package general.users;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static general.Main.usersFile;

public class Account {

     public static Map<String, String> allUsers() throws IOException {

        List<String> fileLines = Files.readAllLines(usersFile);
        Map<String, String> userMap = new HashMap<>();

        for (String string : fileLines) {
            String[] strings = string.split(",");
            userMap.put(strings[0], strings[1]);
        }
        return userMap;
    }

    public static boolean isCorrect(String name, String password) throws IOException {
        Map<String, String> users = allUsers();
        if (!users.containsKey(name)) {
            System.out.println("name does not exist in the list");
            return false;
        } else if (!users.get(name).equals(password)){
            System.out.println("Password is wrong.");
            return false;
        }return true ;
    }

    public static boolean isLoggedIn(String name, String password) throws IOException {
        return isCorrect(name, password);
    }

    public static boolean isTaken(String name) throws IOException {
        if (allUsers().containsKey(name)){
            System.out.println("This name is already taken");
            return true;
        }return false;
    }

}
