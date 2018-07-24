package com.hand.dao;

import com.hand.entity.Film;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {
    List<Film> getFilmsByCustomerId(Connection conn,short customerId) throws SQLException;
}
