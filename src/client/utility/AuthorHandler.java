package client.utility;

import common.util.Request;
import common.util.User;

import java.util.Scanner;


public class AuthorHandler {
    private final String loginCommand = "login";
    private final String registerCommand = "register";

    private Scanner userScanner;

    public AuthorHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public Request handle() {
        Authorization authorization = new Authorization(userScanner);
        String command = authorization.askQuestion("У вас уже есть учетная запись?") ? loginCommand : registerCommand;
        User user = new User(authorization.askLogin(), authorization.askPassword());
        return new Request(command, "", user);
    }
}