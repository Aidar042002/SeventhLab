package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.ResponseOutputer;

public class ServerExitCommand extends AbstractCommand {

    public ServerExitCommand() {
        super("server_exit", "", "завершить работу сервера");
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln("Работа сервера успешно завершена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}