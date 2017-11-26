package org.superbiz.rx;

import org.superbiz.LoggingConfig;
import rx.Observable;
import rx.observables.GroupedObservable;

import java.util.logging.Logger;

public class ConcurrencyGroupBy {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(ConcurrencyGroupBy.class.getName());

    public static void main(String[] args) {
        final Observable<GroupedObservable<String, String>> groupedObservableObservable = Observable
                .just("bread", "butter", "egg", "milk", "tomato",
                        "cheese", "tomato", "egg", "egg")
                .groupBy(prod -> prod);

        groupedObservableObservable.subscribe(a -> {
            System.out.println(a.getKey());
            a.subscribe(System.out::println);
        });

//                .flatMap(grouped -> grouped
//                        .count()
//                        .map(quantity -> {
//                            String productName = grouped.getKey();
//                            return Pair.of(productName, quantity);
//                        }))
//                .flatMap(order -> store
//                        .purchase(order.getKey(), order.getValue())
//                        .subscribeOn(schedulerA))
//                .reduce(BigDecimal::add)
//                .single();
    }
}
