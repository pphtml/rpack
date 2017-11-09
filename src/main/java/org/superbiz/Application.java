package org.superbiz;

import com.zaxxer.hikari.HikariConfig;
import org.superbiz.config.ApplicationModule;
import org.superbiz.config.JooqModule;
import org.superbiz.service.ApiChainAction;
import org.superbiz.service.EmployeeChainAction;
import org.superbiz.utils.CustomErrorHandler;
import org.superbiz.utils.WebpackProcess;
import ratpack.error.ClientErrorHandler;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.rx.RxRatpack;
import ratpack.server.RatpackServer;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import java.util.logging.Logger;

public class Application {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
        System.setProperty("org.jooq.no-logo", "true");
    }

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
            .serverConfig(config -> config
                .findBaseDir()
                .sysProps()) // not needed
            .registry(Guice.registry(bindings -> bindings
                .moduleConfig(ApplicationModule.class, bindings.getServerConfig().get("/user", ApplicationModule.Config.class))
                .moduleConfig(HikariModule.class, getHikariConfig())
                .module(JooqModule.class)
                .bindInstance(ClientErrorHandler.class, new CustomErrorHandler())
                .bind(EmployeeChainAction.class)
                .bind(ApiChainAction.class)
                .bindInstance(new Service() {
                    @Override
                    public void onStart(StartEvent event) throws Exception {
                        RxRatpack.initialize();
                    }
                })
            ))
            .handlers(chain -> chain
                .get("foo/:id", ctx -> ctx.render("Foo " + ctx.getPathTokens().get("id")))
                .prefix("employee", EmployeeChainAction.class) // clean up
                .prefix("api", ApiChainAction.class)
                .files(f -> f.dir("static"))
                .all(ctx -> {
                    ctx.render(ctx.file("static/index.html"));
                })
        ));

        if (getDevMode()) {
            final Runnable task = () -> WebpackProcess.runWebpack();
            task.run();
        }
    }

    private static HikariConfig getHikariConfig() {
        final String url = System.getenv("JDBC_DATABASE_URL");;
        if (url == null) {
            throw new RuntimeException("Environment variable or property JDBC_DATABASE_URL not set");
        }
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        return config;
    }

    private static boolean getDevMode() {
        String devMode = System.getProperty("devMode", "true");
        return "true".equalsIgnoreCase(devMode);
    }
}
