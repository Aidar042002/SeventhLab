package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class ShowCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.showCollection());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}