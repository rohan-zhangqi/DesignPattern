package com.designpattern.strategy.demo;

public class GoldVip implements CalPrice{
    @Override
    public Double calPrice(Double orgnicPrice) {
        return orgnicPrice * 0.7;
    }
}
