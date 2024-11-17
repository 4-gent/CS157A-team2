package com.bookie.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO<T, K> {  // T for entity type, K for primary key type (Integer or String)
    protected Connection connection;

    public BaseDAO() {
        try {
            this.connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract T getById(K id) throws Exception;  // Use the generic type K for primary key
    public abstract T add(T t) throws Exception;
    public abstract boolean update(T t) throws Exception;
    public abstract boolean delete(K id) throws Exception;
}