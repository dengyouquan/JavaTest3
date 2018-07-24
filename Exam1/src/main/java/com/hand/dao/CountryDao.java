package com.hand.dao;

import com.hand.entity.City;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CountryDao {
    List<City> getCitiesByCountryId(Connection conn, short countryId) throws SQLException;
}
