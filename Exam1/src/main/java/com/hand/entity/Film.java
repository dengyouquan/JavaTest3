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
public class Film {
    @Id
    @Column(name = "film_id")
    private short filmId;
    private String title;
    private String description;
    @Column(name = "releaseYear")
    private short release_year;
    @Column(name = "language_id")
    private short languageId;
    @Column(name = "original_language_id")
    private short originalLanguageId;
    @Column(name = "rental_duration")
    private short rentalDuration;
    @Column(name = "rental_rate")
    private double rentalRate;
    @Column(name = "length")
    private short length;
    @Column(name = "replacement_cost")
    private double replacementCost;
    @Column(name = "rating")
    private String rating;
    @Column(name = "special_features")
    private String specialFeatures;
    @Column(name = "last_update")
    private Date lastUpdate;
}
