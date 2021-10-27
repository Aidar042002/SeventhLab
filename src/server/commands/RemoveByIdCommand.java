package server.commands;

import common.data.Product;
import common.exceptions.*;
import common.util.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;


public class RemoveByIdCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_by_id", "<ID>", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            long id = Long.parseLong(stringArgument);
            Product marineToRemove = collectionManager.getById(id);
            if (marineToRemove == null) throw new ProductNotFoundException();
            if (!marineToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkMarineUserId(marineToRemove.getId(), user)) throw new DatabaseEditException();
            databaseCollectionManager.deleteMarineById(id);
            collectionManager.removeFromCollection(marineToRemove);
            ResponseOutputer.appendln("Элемент успешно удален!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен числом!");
        } catch (ProductNotFoundException exception) {
            ResponseOutputer.appenderror("Элемента с таким ID в коллекции нет!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appendln("Элемент успешно удален!");
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