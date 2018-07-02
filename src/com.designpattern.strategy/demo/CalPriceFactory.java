package com.designpattern.strategy.demo;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/*
    简单工厂
 */
public class CalPriceFactory {

//    private CalPriceFactory() {
//    }

    /*
        虽然结合简单工厂模式，我们的策略模式灵活了一些，但不免发现在工厂中多了if-else判断，
        也就是如果增加一个会员类别，我又得增加一个else-if语句，这是简单工厂的缺点，对修改开放。
    */
//    public static CalPrice createCalPrice(Player customer){
//        if (customer.getTotalAmount() > 30000) {//30000则改为金牌会员计算方式
//            return new GoldVip();
//        } else if (customer.getTotalAmount() > 20000){//类似
//            return new SuperVip();
//        } else if (customer.getTotalAmount() > 10000){//类似
//            return new Vip();
//        } else {
//          return new Orgnic();
//        }
//    }

    private static final String CAL_PRICE_PACKAGE = "com.designpattern.strategy.demo";//这里是一个常量，表示我们扫描策略的包

    private ClassLoader classLoader = getClass().getClassLoader();

    private List<Class<? extends CalPrice>> calPriceList;//策略列表

    //根据玩家的总金额产生相应的策略
    public CalPrice createCalPrice(Player player){
        for (Class<? extends CalPrice> clazz : calPriceList) {
            PriceRegion validRegion = handleAnnotation(clazz);//获取该策略的注解
            //判断金额是否在注解的区间
            if (player.getTotalAmount() > validRegion.min() && player.getTotalAmount() < validRegion.max()){
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("策略获取失败");
                }
            }
        }
        throw new RuntimeException("策略获取失败");
    }

    private PriceRegion handleAnnotation(Class<? extends CalPrice> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0){
            return null;
        }
        for (int i = 0; i < annotations.length; i++){
            if (annotations[i] instanceof PriceRegion){
                return (PriceRegion) annotations[i];
            }
        }
        return null;
    }

    private CalPriceFactory() {
        init();
    }

    //在工厂初始化时要初始化策略列表
    private void init(){
        calPriceList = new ArrayList<>();
        File[] resources = getResources();//获取到包下所有的class文件
        Class<CalPrice> calPriceClazz = null;
        try {
            calPriceClazz = (Class<CalPrice>)classLoader.loadClass(CalPrice.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到策略接口");
        }

        for (int i = 0; i < resources.length; i++){
            try {
                //载入包下的类
                Class<?> clazz = classLoader.loadClass(CAL_PRICE_PACKAGE + "." + resources[i].getName().replace(".class",""));
                if (CalPrice.class.isAssignableFrom(clazz) && clazz != calPriceClazz){
                    calPriceList.add((Class<? extends CalPrice>)clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private File[] getResources() {
        try {
            File file = new File(classLoader.getResource(CAL_PRICE_PACKAGE.replace(".", "/")).toURI());
            return file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.getName().endsWith(".class")){
                        return true;
                    }
                    return false;
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException("未找到策略资源");
        }
    }

    public static CalPriceFactory getInstance(){
        return CalPriceFactoryInstance.instance;
    }

    private static class CalPriceFactoryInstance{
        private static CalPriceFactory instance = new CalPriceFactory();
    }
}
