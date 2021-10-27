package server.commands;

import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;


public class MaxByMeleeLocationCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public MaxByMeleeLocationCommand(CollectionManager collectionManager) {
        super("max_by_location", "", "вывести элемент, значение поля location которого максимально");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.maxByMeleeWeapon());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return true;
    }
}