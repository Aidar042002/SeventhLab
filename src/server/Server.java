package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.utility.Outputer;
import server.utility.CommandManager;
import server.utility.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Runs the server.
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    private CommandManager commandManager;
    private boolean isStopped;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private Semaphore semaphore;

    public Server(int port, int maxClients, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.semaphore = new Semaphore(maxClients);
    }

    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    cachedThreadPool.submit(new ConnectionHandler(this, clientSocket, commandManager));
                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Outputer.printerror("Произошла ошибка при соединении с клиентом!");
                    } else break;
                }
            }
            cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("Работа сервера завершена.");
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
        } catch (InterruptedException e) {
            Outputer.printerror("Произошла ошибка при завершении работы с уже подключенными клиентами!");
        }
    }

    public void acquireConnection() {
        try {
            semaphore.acquire();
        } catch (InterruptedException exception) {
            Outputer.printerror("Произошла ошибка при получении разрешения на новое соединение!");
        }
    }

    public void releaseConnection() {
        semaphore.release();
    }


    public synchronized void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            cachedThreadPool.shutdown();
            serverSocket.close();
            Outputer.println("Завершение работы с уже подключенными клиентами...");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
            Outputer.println("Завершение работы с уже подключенными клиентами...");
        }
    }


    private synchronized boolean isStopped() {
        return isStopped;
    }


    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }


    private Socket connectToClient() throws ConnectionErrorException {
        try {
            Outputer.println("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Соединение с клиентом установлено.");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }
}