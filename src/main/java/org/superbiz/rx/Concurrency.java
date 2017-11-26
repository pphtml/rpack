package org.superbiz.rx;

import org.superbiz.LoggingConfig;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Concurrency {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(Concurrency.class.getName());

    private static class RxGroceries {
        Observable<BigDecimal> purchase(String productName, int quantity) {
            return Observable.fromCallable(() ->
                    doPurchase(productName, quantity));
        }
        BigDecimal doPurchase(String productName, int quantity) {
            logger.info("Purchasing " + quantity + " " + productName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            logger.info("Done " + quantity + " " + productName);
            return new BigDecimal(1.0);
        }
    }

    public static void main(String[] args) {
        final RxGroceries rxGroceries = new RxGroceries();

//        final Observable<BigDecimal> single = Observable
//                .just("bread", "butter", "milk", "tomato", "cheese")
//                .subscribeOn(Schedulers.io())
//                .flatMap(prod -> rxGroceries.purchase(prod, 1))
//                .reduce(BigDecimal::add)
//                .single();

        final Observable<BigDecimal> single = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
//                .flatMap(prod -> rxGroceries.purchase(prod, 1).subscribeOn(Schedulers.computation()))
                .flatMap(prod -> rxGroceries.purchase(prod, 1).subscribeOn(Schedulers.io()))
                .reduce(BigDecimal::add)
                .single();

        single.subscribe(result -> logger.info(String.format("Result is: %s", result)));

        Runnable task = () -> logger.info("ZZZ");
        new Thread(task, "custom thread").start();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
