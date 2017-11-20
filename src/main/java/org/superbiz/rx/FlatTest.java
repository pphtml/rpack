package org.superbiz.rx;


import org.superbiz.LoggingConfig;
import rx.Observable;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlatTest {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(FlatTest.class.getName());

    public static void main(String[] args) {
        final Observable<Order> orders = Observable.just(new Order("ABC", 2), new Order("BLE", 3), new Order("MONO", 1));
        final Observable<String> flatObservable = orders.flatMap(o -> {
                    final List<String> list = IntStream.range(0, o.count).mapToObj(i -> String.format("%s %d", o.id, i)).collect(Collectors.toList());
                    return Observable.from(list);
                });
        flatObservable.subscribe(o -> {
            System.out.println(o);
        });
    }

    private static class Order {
        private final String id;
        private final int count;

        public Order(String id, int count) {
            this.id = id;
            this.count = count;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Order{");
            sb.append("id='").append(id).append('\'');
            sb.append(", count=").append(count);
            sb.append('}');
            return sb.toString();
        }
    }
}
