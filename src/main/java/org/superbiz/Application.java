package org.superbiz;

import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import org.superbiz.config.ApplicationModule;
import org.superbiz.service.UserService;
import org.superbiz.utils.HerokuUtils;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Application {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
//            .registry(Guice.registry(bindings ->
//                    bindings.module(HikariModule.class, config -> {
//                        // config.setDataSourceClassName("org.postgresql.Driver");
//                        config.setDataSourceClassName("org.postgresql.ds.common.BaseDataSource");
//                        //config.setDataSourceClassName("org.postgresql.xa.PGXADataSource");
//                        config.addDataSourceProperty("URL",
//                                "jdbc:postgresql://localhost:5444/myschema?user=myschema&password=myschema&currentSchema=myschema");
//                    })))
            //.registryOf(r -> r.add(UserService.class, new UserServiceImpl()))
            .serverConfig(config -> { config
                //j.json(Paths.get("dbconfig.json")).require("/database", DatabaseConfig.class);
                //j.json(Application.class.getClassLoader().getResource("dbconfig.json")).require("/database", DatabaseConfig.class);
                .yaml(Application.class.getClassLoader().getResource("dbconfig.yaml"))
                .yaml(Application.class.getClassLoader().getResource("postgres.yaml"))
                //.env()
                .sysProps();
                //.args(programArgs.stream().toArray(String[]::new));


                //.require("/database", HikariConfig.class);
                //.require("/database", DatabaseConfig.class);
                //.require("/db", HikariConfig.class);

            })
            .registry(Guice.registry(bindings -> bindings
//                    .bindInstance(UserService.class, new UserServiceImpl())
//                    .bind(UserDAO.class, UserDAOImpl.class)
                    .moduleConfig(ApplicationModule.class, bindings.getServerConfig().get("/user", ApplicationModule.Config.class))
                    //.module(HikariModule.class)
                    .moduleConfig(HikariModule.class, getHikariConfig())
                    //.moduleConfig(HikariModule.class, new HikariConfig(bindings.getServerConfig().get("/database", Properties.class)))
//                    .module(HikariModule.class, config -> {
//                        config.setDataSourceClassName("org.postgresql.ds.common.BaseDataSource");
//                        config.addDataSourceProperty("URLx", "jdbc:mysql://localhost:3306/db");
//                        //config.setJdbcUrl("jdbc:postgresql://localhost:5444/myschema?user=myschema&password=myschema&currentSchema=myschema");
//                    })
//                    .bindInstance(HikariConfig.class, getHikariConfig())
                ))
            .handlers(chain -> chain
//                .all(ctx -> {
//                    logger.info("ALL handler");
//                    ctx.next();
//                })
                .get(ctx -> ctx.render("Welcome to Rat pack!!!"))
                //.get("employees", ctx -> ctx.render(Jackson.json(employees)))
                .get("config", ctx -> ctx.render(Jackson.json(ctx.get(DatabaseConfig.class))))
                .get("foo", ctx -> ctx.render("Foo"))
                .get("foo/:id", ctx -> ctx.render("Foo " + ctx.getPathTokens().get("id")))
                .path("paa", p -> p.byMethod(action ->
                    action.get(ctx -> ctx.render("paa GET"))
                        .post(ctx -> ctx.render("paa POST"))
                ))
                .get("user-service", ctx -> {
                    DataSource dataSource = ctx.get(DataSource.class);
                    final UserService userService = ctx.get(UserService.class);
                    ctx.render("User service says " + userService.getUsername());
                })
                .prefix("poo", action -> action.get(ctx -> ctx.render("poo GET " + ctx.getRequest().getQueryParams())))
        ));
    }

//    // https://github.com/ratpack/ratpack/issues/1270
//    private static HikariConfig makeHikariConfig(Properties properties) {
//        logger.info(String.format("Properties: %s", properties));
//        return new HikariConfig(properties);
//    }

    private static HikariConfig getHikariConfig() {
        String url = System.getProperty("JDBC_DATABASE_URL");
        if (url == null) {
            url = System.getenv("JDBC_DATABASE_URL");
        }
        if (url == null) {
            throw new RuntimeException("Environment variable or property JDBC_DATABASE_URL not set");
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        return config;
    }


//    public static HikariConfig getHikariConfig() {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(System.getenv("DATABASE_URL"));
//        return hikariConfig;
//    }
}
