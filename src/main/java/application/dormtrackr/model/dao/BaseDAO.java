package application.dormtrackr.model.dao;

import application.dormtrackr.util.DatabaseConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO<T> {
    protected final DataSource dataSource;

    public BaseDAO() {
        this.dataSource = DatabaseConnection.getDataSource();
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}