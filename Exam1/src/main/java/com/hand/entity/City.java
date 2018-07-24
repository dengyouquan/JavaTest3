package com.hand.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@ToString
public class City {
    @Id
    @Column(name = "city_id")
    private short cityId;
    private String city;
    @Column(name = "country_id")
    private short countryId;
    @Column(name = "last_update")
    private Date lastUpdate;
}
