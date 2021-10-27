package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.ProductGenerate;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class AddCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            ProductGenerate productGenerate = (ProductGenerate) objectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertMarine(productGenerate, user));
            ResponseOutputer.appendln("Product успешно добавлен!");
            return true;
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