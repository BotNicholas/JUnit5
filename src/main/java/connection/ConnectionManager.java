package connection;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Properties;

import static DAO.Constants.PROPERTIES_FILE_PATH;

public class ConnectionManager {
    private static Connection connection;

    private ConnectionManager() {
    }

    //Have to be called only ones before working with database
    public static Connection buildConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileReader(PROPERTIES_FILE_PATH));

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(properties.getProperty("connection.url"),
                    properties.getProperty("connection.user"),
                    properties.getProperty("connection.password"));
        }
        return connection;
    }

    public static Statement getStatement() throws SQLException {
        return connection.createStatement();
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public static PreparedStatement prepareStatement(String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = prepareStatement(sql);

        for (int i = 1; i <= parameters.length; i++) {
            statement.setObject(i, parameters[i - 1]);
        }

        return statement;
    }

    public static PreparedStatement prepareStatementFromObject(String sql, Object object) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = prepareStatement(sql);
        Field[] fields = object.getClass().getDeclaredFields();
        int parameterPosition = 1;

        for (Field field : fields) {
//            System.out.println(field.getName());
            preparedStatement.setObject(parameterPosition, invokeGetter(object, obtainGetter(field)));
        }

        return preparedStatement;
    }

    private static String obtainGetter(Field field) {
        String getter = "get" +
                field.getName().toUpperCase().charAt(0) +
                field.getName().substring(1);
        return getter;
    }

    private static Object invokeGetter(Object object, String getterName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getterMethod = object.getClass().getDeclaredMethod(getterName, null);
        getterMethod.setAccessible(true);
        return getterMethod.invoke(object, null);
    }

    public static void updateIncrementForTable(String tableName) throws SQLException {
        String sql = "alter table " + tableName + " AUTO_INCREMENT=1";
        Statement statement = getStatement();

        statement.executeUpdate(sql);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }
}
