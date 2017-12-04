package org.superbiz.rx;

import org.superbiz.LoggingConfig;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SingleThread {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(SingleThread.class.getName());

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        logger.info("starting");

        Observable<Integer> vals = Observable.range(1,10);

        vals.flatMap(val -> Observable.just(val)
                //.subscribeOn(Schedulers. io())
                .subscribeOn(Schedulers.from(singleThreadExecutor))
                .map(i -> myComputation(i))
        ).subscribe(val -> {
            logger.info("result " + val);
        });



//        final Observable<Long> observable = Observable.interval(300, TimeUnit.MILLISECONDS)
//                .flatMap(o -> Observable.just(o))
//                .observeOn(Schedulers.io())
//                //.subscribeOn(Schedulers.io())
//                .map(o -> o);
//

        Thread.sleep(15000);
        singleThreadExecutor.shutdownNow();
    }

    private static Integer myComputation(Integer i) {
        logger.info("computing " + i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }
}
