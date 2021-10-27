package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

public class InfoCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "", "вывести информацию о коллекции");
        this.collectionManager = collectionManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            LocalDateTime lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                    lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

            ResponseOutputer.appendln("Сведения о коллекции:");
            ResponseOutputer.appendln(" Тип: " + collectionManager.collectionType());
            ResponseOutputer.appendln(" Количество элементов: " + collectionManager.collectionSize());
            ResponseOutputer.appendln(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}