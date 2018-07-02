package com.designpattern.strategy.demo;

@PriceRegion(max = 10000)
public class Orgnic implements CalPrice{
    @Override
    public Double calPrice(Double orgnicPrice) {
        return orgnicPrice;
    }
}
