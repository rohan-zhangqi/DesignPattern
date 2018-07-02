package com.designpattern.strategy.demo;

@PriceRegion(min = 30000)
public class GoldVip implements CalPrice{
    @Override
    public Double calPrice(Double orgnicPrice) {
        return orgnicPrice * 0.7;
    }
}
