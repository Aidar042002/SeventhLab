package server.commands;

import common.data.Product;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.ProductGenerate;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class AddIfMinCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add_if_min", "{element}", "добавить новый элемент, если его значение меньше, чем у наименьшего");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            ProductGenerate productGenerate = (ProductGenerate) objectArgument;
            Product marineToAdd = databaseCollectionManager.insertMarine(productGenerate, user);
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getFirst()) < 0) {
                collectionManager.addToCollection(marineToAdd);
                ResponseOutputer.appendln("Product успешно добавлен!");
                return true;
            } else ResponseOutputer.appenderror("Значение элемента больше, чем значение наименьшего!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}