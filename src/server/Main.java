package server;

import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import common.utility.Outputer;
import server.commands.*;
import server.utility.*;

public class Main {
    private static final int MAX_CLIENTS = 1000;
    private static String databaseUsername = "s311743";
    private static int port;
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    public static void main(String[] args) {
        if (!initialize(args)) return;
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveByIdCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new AddIfMinCommand(collectionManager, databaseCollectionManager),
                new RemoveGreaterCommand(collectionManager, databaseCollectionManager),
                new HistoryCommand(),
                new SumOfPriceCommand(collectionManager),
                new MaxByMeleeLocationCommand(collectionManager),
                new FilterByOrgTypeCommand(collectionManager),
                new ServerExitCommand(),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();
        databaseHandler.closeConnection();
    }

    private static boolean initialize(String[] args) {
        try {
            if (args.length != 3) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            databaseHost = args[1];
            databasePassword = args[2];
            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }
}