package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExists;
import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutputer;


public class RegisterCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(user)) ResponseOutputer.appendln("Пользователь " +
                    user.getUsername() + " зарегистрирован.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: это внутренняя команда...");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.appenderror("Пользователь " + user.getUsername() + " уже существует!");
        }
        return false;
    }
}