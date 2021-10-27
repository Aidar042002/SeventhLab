package server.utility;

import common.utility.Outputer;

import java.sql.*;

public class DatabaseHandler {
    // Table names
    public static final String PRODUCT_TABLE = "product";
    public static final String USER_TABLE = "my_user";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String ORG_TABLE = "organization";
    // PRODUCT_TABLE column names
    public static final String PRODUCT_TABLE_ID_COLUMN = "id";
    public static final String PRODUCT_TABLE_NAME_COLUMN = "name";
    public static final String PRODUCT_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String PRODUCT_TABLE_PRICE_COLUMN = "price";
    public static final String PRODUCT_TABLE_CATEGORY_COLUMN = "unitofmeasure";
    public static final String PRODUCT_TABLE_ORG_TYPE_COLUMN = "org_type";
    public static final String PRODUCT_TABLE_MELEE_WEAPON_COLUMN = "location";
    public static final String PRODUCT_TABLE_CHAPTER_ID_COLUMN = "org_id";
    public static final String PRODUCT_TABLE_USER_ID_COLUMN = "user_id";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN = "product_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    // ORG_TABLE column names
    public static final String ORG_TABLE_ID_COLUMN = "id";
    public static final String ORG_TABLE_NAME_COLUMN = "name";
    public static final String ORG_TABLE_MARINES_COUNT_COLUMN = "zip";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        connectToDataBase();
    }

    private void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            Outputer.println("Соединение с базой данных установлено.");
        } catch (SQLException exception) {
            Outputer.printerror("Произошла ошибка при подключении к базе данных!");
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Драйвер управления базой дынных не найден!");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGeneratedKeys);
            return preparedStatement;
        } catch (SQLException exception) {

            if (connection == null) System.out.println("connection_null");

            throw new SQLException(exception);
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
        } catch (SQLException exception) {
        }
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            Outputer.println("Соединение с базой данных разорвано.");
        } catch (SQLException exception) {
            Outputer.printerror("Произошла ошибка при разрыве соединения с базой данных!");
        }
    }


    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {

        }
    }


    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {

        }
    }


    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {

        }
    }


    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {

        }
    }


    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
        }
    }
}