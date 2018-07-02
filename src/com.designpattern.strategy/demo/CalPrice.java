package com.designpattern.strategy.demo;
/*
    计算价格
 */
public interface CalPrice {
    //根据原价返回一个最终的价格
    Double calPrice(Double orgnicPrice);
}
