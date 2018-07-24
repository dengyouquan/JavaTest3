package com.hand;

import com.hand.dao.CountryDao;
import com.hand.dao.CountryDaoImpl;
import com.hand.dao.CustomerDao;
import com.hand.dao.CustomerDaoImpl;
import com.hand.entity.City;
import com.hand.entity.Film;
import com.hand.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("得到country_id的city：");
        getCities();
        System.out.println("得到customer_id的film：");
        getRentals();
    }
    private static void getCities() {
        Connection connection = ConnectionFactory.INSTANCE.getConnection();
        CountryDao countryDao = new CountryDaoImpl();
        try {
            //short country_id = 2;
            short country_id = Short.valueOf(System.getenv("country_id"));
            List<City> cities = countryDao.getCitiesByCountryId(connection, country_id);
            System.out.println("City数量："+cities.size());
            for (City c:cities) {
                System.out.println(c.toString());
            }
            ConnectionFactory.release(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRentals() {
        Connection connection = ConnectionFactory.INSTANCE.getConnection();
        CustomerDao customerDao = new CustomerDaoImpl();
        try {
            //short customer_id = 2;
            short customer_id = Short.valueOf(System.getenv("customer_id"));
            List<Film> films = customerDao.getFilmsByCustomerId(connection, customer_id);
            System.out.println("film数量："+films.size());
            for (Film f:films) {
                System.out.println(f.toString());
            }
            ConnectionFactory.release(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
