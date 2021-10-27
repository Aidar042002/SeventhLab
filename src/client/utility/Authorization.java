package client.utility;

import client.Main;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Authorization {
    private Scanner userScanner;

    public Authorization(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public String askLogin() {
        String login;
        while (true) {
            try {
                Outputer.println("Введите логин:");
                Outputer.print(Main.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Данного логина не существует!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return login;
    }


    public String askPassword() {
        String password;
        while (true) {
            try {
                Outputer.println("Введите пароль:");
                Outputer.print(Main.PS2);
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Неверный логин или пароль!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return password;
    }

    public boolean askQuestion(String question) {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(Main.PS2);
                answer = userScanner.nextLine().trim();
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Ответ не распознан!");
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен '+' или '-'!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}