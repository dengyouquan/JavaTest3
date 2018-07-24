package com.hand.dao;

import com.hand.entity.City;
import com.hand.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDaoImpl implements CountryDao {

    @Override
    public List<City> getCitiesByCountryId(Connection conn,short countryId) throws SQLException {
        String sql = "select * from city where country_id=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setShort(1,countryId);
        ResultSet rs = statement.executeQuery();
        List<City> cities = setResultToCityList(rs);
        ConnectionFactory.release(statement,rs);
        return  cities;
    }

    private List<City> setResultToCityList(ResultSet rs) throws SQLException {
        List<City> cities = new ArrayList<>();
        while (rs.next()){
            City city = new City();
            city.setCountryId(rs.getShort("country_id"));
            city.setCity(rs.getString("city"));
            city.setCityId(rs.getShort("city_id"));
            city.setLastUpdate(rs.getDate("last_update"));
            cities.add(city);
        }
        return cities;
    }
}
