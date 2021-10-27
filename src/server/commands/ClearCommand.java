package server.commands;

import common.data.Product;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.DatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class ClearCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            for (Product marine : collectionManager.getCollection()) {
                if (!marine.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkMarineUserId(marine.getId(), user)) throw new DatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Коллекция очищена!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (DatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }
}