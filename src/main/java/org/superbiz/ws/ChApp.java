package org.superbiz.ws;

import org.superbiz.LoggingConfig;
import ratpack.rx.RxRatpack;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class ChApp {

    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    public static void main(String... args) throws Exception {
        RxRatpack.initialize();

        RatpackServer.start(
                s -> s.serverConfig(
                        c -> c.baseDir(BaseDir.find()))
                        .registryOf(registry -> registry.add(new ChatHandler()))
                        .handlers(chain ->
                                chain
                                        .get("chat", ChatHandler.class)
                                        .files(f -> f.dir("static").indexFiles("chat-index.html"))));
    }
}
