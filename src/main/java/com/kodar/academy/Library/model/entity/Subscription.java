package com.kodar.academy.Library.model.entity;

import com.kodar.academy.Library.model.enums.SubscriptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    private SubscriptionType tier;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "max_rent_days")
    private int maxRentDays;

    @Column(name = "max_rent_books")
    private int maxRentBooks;

    public Subscription() {

    }

    public int getId() {
        return id;
    }

    public SubscriptionType getTier() {
        return tier;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getMaxRentDays() {
        return maxRentDays;
    }

    public int getMaxRentBooks() {
        return maxRentBooks;
    }
}
