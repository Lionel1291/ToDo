package ch.bztf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDBToDo {

    /**
     * Methode um die Verbindung mit der DatenBank herzustellen
     * 
     * @return : Connection und null als Rückgabewerte
     * @throws SQLException
     */
    public static Connection sQLiteDBToDoConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbconn;
            dbconn = DriverManager.getConnection("jdbc:sqlite:ToDoList.sqlite");
            return dbconn;
        } catch (ClassNotFoundException | SQLException exeption) {
            System.out.println(exeption.getMessage());
            return null;
        }
    }
}