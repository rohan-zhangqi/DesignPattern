package com.designpattern.strategy.demo;

public class Orgnic implements CalPrice{
    @Override
    public Double calPrice(Double orgnicPrice) {
        return orgnicPrice;
    }
}
