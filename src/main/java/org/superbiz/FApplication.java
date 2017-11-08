package org.superbiz;

import ratpack.exec.Blocking;
import ratpack.server.RatpackServer;

import java.util.logging.Logger;

public class FApplication {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
        System.setProperty("org.jooq.no-logo", "true");
    }

    private static final Logger logger = Logger.getLogger(FApplication.class.getName());

    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
            .serverConfig(config -> { config
                .threads(8)
                .connectQueueSize(8);
            })
            .handlers(chain -> chain
                .get("block", ctx -> {
                    logger.info(String.format("A. Original compute thread: %s", Thread.currentThread().getName()));
                    Blocking.exec(() -> {
                        logger.info(String.format("B. Promise compute thread: %s", Thread.currentThread().getName()));
                        Thread.sleep(10000);
                        ctx.render("ble");
                    });
                    logger.info(String.format("C. Original compute thread: %s", Thread.currentThread().getName()));
                })
//                .get("block", ctx -> {
//                    logger.info(String.format("A. Original compute thread: %s", Thread.currentThread().getName()));
//                    Promise.async(downstream -> {
//                        Execution.fork().start(forkedExec -> {
//                            logger.info(String.format("B. Promise compute thread: %s", Thread.currentThread().getName()));
//                            Thread.sleep(10000);
//                            ctx.render("ble");
//                            downstream.success("hello from async promise");
//                        });
//                    }).then(result -> {
//                        ctx.render(result);
//                    });
//                    logger.info(String.format("C. Original compute thread: %s", Thread.currentThread().getName()));
//                })
        ));
    }
}
