package users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {

     public Map<String, String> accountListReader() throws IOException {
        Path usersFile = Paths.get("C:\\Git\\Messenger\\src\\users\\users.txt");
        List<String> fileLines = Files.readAllLines(usersFile);
        Map<String, String> userMap = new HashMap<>();

        for (String string : fileLines) {
            String[] strings = string.split(",");
            userMap.put(strings[0], strings[1]);
        }
        return userMap;
    }

    public boolean isCorrect(String name, String password) throws IOException {
        if (!accountListReader().containsKey(name)) {
            System.out.println("name does not exist in the list");
            return false;
        } else if (!accountListReader().get(name).equals(password)){
            System.out.println("Password is wrong.");
            return false;
        }return true ;
    }

    public boolean isLoggedIn(String name, String password) throws IOException {
        return isCorrect(name, password);
    }

    public boolean isTaken(String name) throws IOException {
        if (accountListReader().containsKey(name)){
            System.out.println("This name is already taken");
            return true;
        }return false;
    }

}
