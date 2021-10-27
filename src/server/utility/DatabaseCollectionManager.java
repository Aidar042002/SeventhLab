package server.utility;

import common.data.*;
import common.exceptions.DatabaseHandlingException;
import common.util.ProductGenerate;
import common.util.User;
import common.utility.Outputer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.TreeSet;


public class DatabaseCollectionManager {
    // PRODUCT_TABLE
    private final String SELECT_ALL_PRODUCT = "SELECT * FROM " + DatabaseHandler.PRODUCT_TABLE;
    private final String SELECT_PRODUCT_BY_ID = SELECT_ALL_PRODUCT + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_PRODUCT_BY_ID_AND_USER_ID = SELECT_PRODUCT_BY_ID + " AND " +
            DatabaseHandler.PRODUCT_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_PRODUCT = "INSERT INTO " +
            DatabaseHandler.PRODUCT_TABLE + " (" +
            DatabaseHandler.PRODUCT_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_PRICE_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_CATEGORY_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_ORG_TYPE_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_MELEE_WEAPON_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_CHAPTER_ID_COLUMN + ", " +
            DatabaseHandler.PRODUCT_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?," +
            "?, ?, ?, ?)";
    private final String DELETE_PRODUCT_BY_ID = "DELETE FROM " + DatabaseHandler.PRODUCT_TABLE +
            " WHERE " + DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCT_NAME_BY_ID = "UPDATE " + DatabaseHandler.PRODUCT_TABLE + " SET " +
            DatabaseHandler.PRODUCT_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCT_PRICE_BY_ID = "UPDATE " + DatabaseHandler.PRODUCT_TABLE + " SET " +
            DatabaseHandler.PRODUCT_TABLE_PRICE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_UNITOFMEASURE_CATEGORY_BY_ID = "UPDATE " + DatabaseHandler.PRODUCT_TABLE + " SET " +
            DatabaseHandler.PRODUCT_TABLE_CATEGORY_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCT_TYPE_TYPE_BY_ID = "UPDATE " + DatabaseHandler.PRODUCT_TABLE + " SET " +
            DatabaseHandler.PRODUCT_TABLE_ORG_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PRODUCT_LOCATION_BY_ID = "UPDATE " + DatabaseHandler.PRODUCT_TABLE + " SET " +
            DatabaseHandler.PRODUCT_TABLE_MELEE_WEAPON_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PRODUCT_TABLE_ID_COLUMN + " = ?";
    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_PRODUCT_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_PRODUCT_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN + " = ?";
    // ORG_TABLE
    private final String SELECT_ALL_ORG = "SELECT * FROM " + DatabaseHandler.ORG_TABLE;
    private final String SELECT_ORG_BY_ID = SELECT_ALL_ORG +
            " WHERE " + DatabaseHandler.ORG_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_ORG = "INSERT INTO " +
            DatabaseHandler.ORG_TABLE + " (" +
            DatabaseHandler.ORG_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.ORG_TABLE_MARINES_COUNT_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_ORG_BY_ID = "UPDATE " + DatabaseHandler.ORG_TABLE + " SET " +
            DatabaseHandler.ORG_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.ORG_TABLE_MARINES_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.ORG_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_ORG_BY_ID = "DELETE FROM " + DatabaseHandler.ORG_TABLE +
            " WHERE " + DatabaseHandler.ORG_TABLE_ID_COLUMN + " = ?" + "AND" + DatabaseHandler.USER_TABLE_ID_COLUMN + " = ?";
    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    private Product createMarine(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(DatabaseHandler.PRODUCT_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.PRODUCT_TABLE_NAME_COLUMN);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.PRODUCT_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        double health = resultSet.getDouble(DatabaseHandler.PRODUCT_TABLE_PRICE_COLUMN);
        UnitOfMeasure category = UnitOfMeasure.valueOf(resultSet.getString(DatabaseHandler.PRODUCT_TABLE_CATEGORY_COLUMN));
        OrganizationType organizationTypeType = OrganizationType.valueOf(resultSet.getString(DatabaseHandler.PRODUCT_TABLE_ORG_TYPE_COLUMN));
        Location location = Location.valueOf(resultSet.getString(DatabaseHandler.PRODUCT_TABLE_MELEE_WEAPON_COLUMN));
        Coordinates coordinates = getCoordinatesByMarineId(id);
        Organization organization = getChapterById(resultSet.getLong(DatabaseHandler.PRODUCT_TABLE_CHAPTER_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.PRODUCT_TABLE_USER_ID_COLUMN));
        return new Product(
                id,
                name,
                coordinates,
                creationDate,
                health,
                category,
                organizationTypeType,
                location,
                organization,
                owner
        );
    }

    public NavigableSet<Product> getCollection() throws DatabaseHandlingException {
        NavigableSet<Product> marineList = new TreeSet<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_PRODUCT, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                marineList.add(createMarine(resultSet));
            }
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return marineList;
    }


    private long getChapterIdByMarineId(long marineId) throws SQLException {
        long chapterId;
        PreparedStatement preparedSelectMarineByIdStatement = null;
        try {
            preparedSelectMarineByIdStatement = databaseHandler.getPreparedStatement(SELECT_PRODUCT_BY_ID, false);
            preparedSelectMarineByIdStatement.setLong(1, marineId);
            ResultSet resultSet = preparedSelectMarineByIdStatement.executeQuery();
            if (resultSet.next()) {
                chapterId = resultSet.getLong(DatabaseHandler.PRODUCT_TABLE_CHAPTER_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectMarineByIdStatement);
        }
        return chapterId;
    }


    private Coordinates getCoordinatesByMarineId(long marineId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByMarineIdStatement = null;
        try {
            preparedSelectCoordinatesByMarineIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_PRODUCT_ID, false);
            preparedSelectCoordinatesByMarineIdStatement.setLong(1, marineId);
            ResultSet resultSet = preparedSelectCoordinatesByMarineIdStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getDouble(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesByMarineIdStatement);
        }
        return coordinates;
    }

    private Organization getChapterById(long chapterId) throws SQLException {
        Organization organization;
        PreparedStatement preparedSelectChapterByIdStatement = null;
        try {
            preparedSelectChapterByIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_ORG_BY_ID, false);
            preparedSelectChapterByIdStatement.setLong(1, chapterId);
            ResultSet resultSet = preparedSelectChapterByIdStatement.executeQuery();
            if (resultSet.next()) {
                organization = new Organization(
                        resultSet.getString(DatabaseHandler.ORG_TABLE_NAME_COLUMN),
                        resultSet.getLong(DatabaseHandler.ORG_TABLE_MARINES_COUNT_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectChapterByIdStatement);
        }
        return organization;
    }

    public Product insertMarine(ProductGenerate productGenerate, User user) throws DatabaseHandlingException {
        Product marine;
        PreparedStatement preparedInsertMarineStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertChapterStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertMarineStatement = databaseHandler.getPreparedStatement(INSERT_PRODUCT, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertChapterStatement = databaseHandler.getPreparedStatement(INSERT_ORG, true);

            preparedInsertChapterStatement.setString(1, productGenerate.getChapter().getName());
            preparedInsertChapterStatement.setLong(2, productGenerate.getChapter().getMarinesCount());
            if (preparedInsertChapterStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedChapterKeys = preparedInsertChapterStatement.getGeneratedKeys();
            long chapterId;
            if (generatedChapterKeys.next()) {
                chapterId = generatedChapterKeys.getLong(1);
            } else throw new SQLException();


            preparedInsertMarineStatement.setString(1, productGenerate.getName());
            preparedInsertMarineStatement.setTimestamp(2, Timestamp.valueOf(creationTime));
            preparedInsertMarineStatement.setDouble(3, productGenerate.getPrice());
            preparedInsertMarineStatement.setString(4, productGenerate.getUnitOfMeasure().toString());
            preparedInsertMarineStatement.setString(5, productGenerate.getWeaponType().toString());
            preparedInsertMarineStatement.setString(6, productGenerate.getMeleeWeapon().toString());
            preparedInsertMarineStatement.setLong(7, chapterId);
            preparedInsertMarineStatement.setLong(8, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertMarineStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedMarineKeys = preparedInsertMarineStatement.getGeneratedKeys();
            long spaceMarineId;
            if (generatedMarineKeys.next()) {
                spaceMarineId = generatedMarineKeys.getLong(1);
            } else throw new SQLException();

            preparedInsertCoordinatesStatement.setLong(1, spaceMarineId);
            preparedInsertCoordinatesStatement.setDouble(2, productGenerate.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setFloat(3, productGenerate.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();

            marine = new Product(
                    spaceMarineId,
                    productGenerate.getName(),
                    productGenerate.getCoordinates(),
                    creationTime,
                    productGenerate.getPrice(),
                    productGenerate.getUnitOfMeasure(),
                    productGenerate.getWeaponType(),
                    productGenerate.getMeleeWeapon(),
                    productGenerate.getChapter(),
                    user
            );

            databaseHandler.commit();
            return marine;
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertMarineStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertChapterStatement);
            databaseHandler.setNormalMode();
        }
    }

    public void updateMarineById(long marineId, ProductGenerate productGenerate) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateMarineNameByIdStatement = null;
        PreparedStatement preparedUpdateMarineHealthByIdStatement = null;
        PreparedStatement preparedUpdateMarineCategoryByIdStatement = null;
        PreparedStatement preparedUpdateMarineWeaponTypeByIdStatement = null;
        PreparedStatement preparedUpdateMarineMeleeWeaponByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesByMarineIdStatement = null;
        PreparedStatement preparedUpdateChapterByIdStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdateMarineNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PRODUCT_NAME_BY_ID, false);
            preparedUpdateMarineHealthByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PRODUCT_PRICE_BY_ID, false);
            preparedUpdateMarineCategoryByIdStatement = databaseHandler.getPreparedStatement(UPDATE_UNITOFMEASURE_CATEGORY_BY_ID, false);
            preparedUpdateMarineWeaponTypeByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PRODUCT_TYPE_TYPE_BY_ID, false);
            preparedUpdateMarineMeleeWeaponByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PRODUCT_LOCATION_BY_ID, false);
            preparedUpdateCoordinatesByMarineIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_PRODUCT_ID, false);
            preparedUpdateChapterByIdStatement = databaseHandler.getPreparedStatement(UPDATE_ORG_BY_ID, false);

            if (productGenerate.getName() != null) {
                preparedUpdateMarineNameByIdStatement.setString(1, productGenerate.getName());
                preparedUpdateMarineNameByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineNameByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getCoordinates() != null) {
                preparedUpdateCoordinatesByMarineIdStatement.setDouble(1, productGenerate.getCoordinates().getX());
                preparedUpdateCoordinatesByMarineIdStatement.setFloat(2, productGenerate.getCoordinates().getY());
                preparedUpdateCoordinatesByMarineIdStatement.setLong(3, marineId);
                if (preparedUpdateCoordinatesByMarineIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getPrice() != -1) {
                preparedUpdateMarineHealthByIdStatement.setDouble(1, productGenerate.getPrice());
                preparedUpdateMarineHealthByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineHealthByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getUnitOfMeasure() != null) {
                preparedUpdateMarineCategoryByIdStatement.setString(1, productGenerate.getUnitOfMeasure().toString());
                preparedUpdateMarineCategoryByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineCategoryByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getWeaponType() != null) {
                preparedUpdateMarineWeaponTypeByIdStatement.setString(1, productGenerate.getWeaponType().toString());
                preparedUpdateMarineWeaponTypeByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineWeaponTypeByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getMeleeWeapon() != null) {
                preparedUpdateMarineMeleeWeaponByIdStatement.setString(1, productGenerate.getMeleeWeapon().toString());
                preparedUpdateMarineMeleeWeaponByIdStatement.setLong(2, marineId);
                if (preparedUpdateMarineMeleeWeaponByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (productGenerate.getChapter() != null) {
                preparedUpdateChapterByIdStatement.setString(1, productGenerate.getChapter().getName());
                preparedUpdateChapterByIdStatement.setLong(2, productGenerate.getChapter().getMarinesCount());
                preparedUpdateChapterByIdStatement.setLong(3, getChapterIdByMarineId(marineId));
                if (preparedUpdateChapterByIdStatement.executeUpdate() == 0) throw new SQLException();
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateMarineNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineHealthByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineCategoryByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineWeaponTypeByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateMarineMeleeWeaponByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateCoordinatesByMarineIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateChapterByIdStatement);
            databaseHandler.setNormalMode();
        }
    }

    public void deleteMarineById(long marineId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteChapterByIdStatement = null;
        try {
            preparedDeleteChapterByIdStatement = databaseHandler.getPreparedStatement(DELETE_ORG_BY_ID, false);
            preparedDeleteChapterByIdStatement.setLong(1, getChapterIdByMarineId(marineId));
            if (preparedDeleteChapterByIdStatement.executeUpdate() == 0) Outputer.println(3);
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteChapterByIdStatement);
        }
    }

    public boolean checkMarineUserId(long marineId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectMarineByIdAndUserIdStatement = null;
        try {
            preparedSelectMarineByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_PRODUCT_BY_ID_AND_USER_ID, false);
            preparedSelectMarineByIdAndUserIdStatement.setLong(1, marineId);
            preparedSelectMarineByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectMarineByIdAndUserIdStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectMarineByIdAndUserIdStatement);
        }
    }

    public void clearCollection() throws DatabaseHandlingException {
        NavigableSet<Product> marineList = getCollection();
        for (Product marine : marineList) {
            deleteMarineById(marine.getId());
        }
    }
}