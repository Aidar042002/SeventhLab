package client.utility;

import client.Main;
import common.data.*;
import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.util.ProductGenerate;
import common.util.Request;
import common.util.ResponseCode;
import common.util.User;
import common.utility.Outputer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(Main.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        Outputer.print(Main.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Превышено количество попыток ввода!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        ProductGenerate marineAddRaw = generateAdd();
                        return new Request(userCommand[0], userCommand[1], marineAddRaw, user);
                    case UPDATE_OBJECT:
                        ProductGenerate marineUpdateRaw = generateMarineUpdate();
                        return new Request(userCommand[0], userCommand[1], marineUpdateRaw, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printerror("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add_if_min":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_price":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "max_by_location":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_by_org_type":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<org_type>");
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    private ProductGenerate generateAdd() throws IncorrectInputInScriptException {
        ProductAsker productAsker = new ProductAsker(userScanner);
        if (fileMode()) productAsker.setFileMode();
        return new ProductGenerate(
                productAsker.askName(),
                productAsker.askCoordinates(),
                productAsker.askPrice(),
                productAsker.askUnitOfMeasure(),
                productAsker.askOrgType(),
                productAsker.askLOcation(),
                productAsker.askOrg()
        );
    }

    private ProductGenerate generateMarineUpdate() throws IncorrectInputInScriptException {
        ProductAsker productAsker = new ProductAsker(userScanner);
        if (fileMode()) productAsker.setFileMode();
        String name = productAsker.askQuestion("Хотите изменить имя?") ?
                productAsker.askName() : null;
        Coordinates coordinates = productAsker.askQuestion("Хотите изменить координаты ?") ?
                productAsker.askCoordinates() : null;
        double health = productAsker.askQuestion("Хотите изменить цену?") ?
                productAsker.askPrice() : -1;
        UnitOfMeasure category = productAsker.askQuestion("Хотите изменить единицу измерения?") ?
                productAsker.askUnitOfMeasure() : null;
        OrganizationType organizationTypeType = productAsker.askQuestion("Хотите изменить тип организации?") ?
                productAsker.askOrgType() : null;
        Location location = productAsker.askQuestion("Хотите изменить локацию?") ?
                productAsker.askLOcation() : null;
        Organization organization = productAsker.askQuestion("Хотите изменить организацию?") ?
                productAsker.askOrg() : null;
        return new ProductGenerate(
                name,
                coordinates,
                health,
                category,
                organizationTypeType,
                location,
                organization
        );
    }
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}