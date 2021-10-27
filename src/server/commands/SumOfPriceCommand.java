package server.commands;

import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class SumOfPriceCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public SumOfPriceCommand(CollectionManager collectionManager) {
        super("sum_of_price", "", "вывести сумму значений поля price для всех элементов коллекции");
        this.collectionManager = collectionManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            double sum_of_health = collectionManager.getSumOfHealth();
            if (sum_of_health == 0) throw new CollectionIsEmptyException();
            ResponseOutputer.appendln("Сумма здоровья всех price: " + sum_of_health);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return false;
    }
}