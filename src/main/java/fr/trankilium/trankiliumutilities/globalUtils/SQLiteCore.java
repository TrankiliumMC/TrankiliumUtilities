package fr.trankilium.trankiliumutilities.globalUtils;

import javax.annotation.Nonnull;
import java.sql.*;

public class SQLiteCore {
    private Connection SQLiteConnection = null;

    /**
     * Connexion vers une db : <code>jdbc:sqlite:${_Path}</code>
     * @author Trankilium
     *
     * @param _Path String
     * @return boolean
     */
    public boolean ConnectDB(@Nonnull String _Path) {
        try {
            if (SQLiteConnection == null) {
                SQLiteConnection = DriverManager.getConnection("jdbc:sqlite:" + _Path);}
            else {
                System.err.println("SQL connection is not null.");}
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Déconnexion de la db.
     * @author Trankilium
     *
     * @return boolean
     */
    public boolean DisconnectDB() {
        try {
            if (SQLiteConnection != null)
                SQLiteConnection.close();
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Execute un statement SQL.
     * @author Trankilium
     *
     * @param _Sql String
     * @return boolean
     */
    public boolean Exec(@Nonnull String _Sql) {
        try {
            Statement statement = SQLiteConnection.createStatement();
            statement.execute(_Sql);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Interroger des données à partir d'une table.
     * @author Trankilium
     *
     * @param _Sql String
     * @return ResultSet
     */
    public ResultSet Query(@Nonnull String _Sql) {
        try {
            Statement statement = SQLiteConnection.createStatement();
            return statement.executeQuery(_Sql);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return null;
    }

}