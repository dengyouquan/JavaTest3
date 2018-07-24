package com.hand.dao;

import com.hand.entity.Film;
import com.hand.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public List<Film> getFilmsByCustomerId(Connection conn,short customerId) throws SQLException {
        String sql = "select * from film f join inventory i on f.film_id=i.film_id join rental r on r.inventory_id=i.inventory_id where r.customer_id=? order by r.rental_date DESC";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setShort(1,customerId);
        ResultSet rs = statement.executeQuery();
        List<Film> films = setResultToFilmList(rs);
        ConnectionFactory.release(statement,rs);
        return  films;
    }

    private List<Film> setResultToFilmList(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        while (rs.next()){
            Film film = new Film();
            film.setFilmId(rs.getShort("film_id"));
            film.setTitle(rs.getString("title"));
            film.setDescription(rs.getString("description"));
            film.setRelease_year(rs.getShort("release_year"));
            film.setLanguageId(rs.getShort("language_id"));
            film.setOriginalLanguageId(rs.getShort("original_language_id"));
            film.setRentalDuration(rs.getShort("rental_duration"));
            film.setRentalRate(rs.getDouble("rental_rate"));
            film.setLength(rs.getShort("length"));
            film.setReplacementCost(rs.getDouble("replacement_cost"));
            film.setRating(rs.getString("rating"));
            film.setSpecialFeatures(rs.getString("special_features"));
            film.setLastUpdate(rs.getDate("last_update"));
            films.add(film);
        }
        return films;
    }
}
