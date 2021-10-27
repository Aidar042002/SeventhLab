package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import common.util.User;
import server.utility.ResponseOutputer;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "", "завершить работу клиента");
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}