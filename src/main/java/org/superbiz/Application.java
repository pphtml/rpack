package org.superbiz;

import com.zaxxer.hikari.HikariConfig;
import org.jooq.DSLContext;
import org.superbiz.config.ApplicationModule;
import org.superbiz.config.JooqModule;
import org.superbiz.model.Employee;
import org.superbiz.service.ApiChainAction;
import org.superbiz.service.EmployeeChainAction;
import org.superbiz.service.UserService;
import org.superbiz.utils.CustomErrorHandler;
import org.superbiz.utils.WebpackProcess;
import ratpack.error.ClientErrorHandler;
import ratpack.exec.Blocking;
import ratpack.exec.Execution;
import ratpack.exec.Promise;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Logger;

public class Application {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
        System.setProperty("org.jooq.no-logo", "true");
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
                .threads(8)
                //j.json(Paths.get("dbconfig.json")).require("/database", DatabaseConfig.class);
                //j.json(Application.class.getClassLoader().getResource("dbconfig.json")).require("/database", DatabaseConfig.class);
                .findBaseDir()
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
                .moduleConfig(HikariModule.class, getHikariConfig())
                .module(JooqModule.class)
                .bindInstance(ClientErrorHandler.class, new CustomErrorHandler())
                .bind(EmployeeChainAction.class)
                .bind(ApiChainAction.class)
            ))
            .handlers(chain -> chain
//                .all(ctx -> {
//                    logger.info("ALL handler");
//                    ctx.next();
//                })
                //.get(ctx -> ctx.render("Welcome to Rat pack!!!"))
                //.get("employees", ctx -> ctx.render(Jackson.json(employees)))
//                .get("block", ctx -> {
//                    logger.info(String.format("A. Original compute thread: %s", Thread.currentThread().getName()));
//                    Blocking.exec(() -> {
//                        logger.info(String.format("B. Promise compute thread: %s", Thread.currentThread().getName()));
//                        Thread.sleep(15000);
//                        ctx.render("abc");
//                    });
//                    logger.info(String.format("C. Original compute thread: %s", Thread.currentThread().getName()));
//                })
//                .get("block", ctx -> {
//                    logger.info(String.format("A. Original compute thread: %s", Thread.currentThread().getName()));
//                    Promise.async(downstream -> {
//                        logger.info(String.format("B. Promise compute thread: %s", Thread.currentThread().getName()));
//                        Thread.sleep(15000);
//                        downstream.success("hello from async promise");
//                    }).then(result -> {
//                        ctx.render(result);
//                    });
//                    logger.info(String.format("C. Original compute thread: %s", Thread.currentThread().getName()));
//                })
//                .get("block", ctx -> {
//                    logger.info(String.format("A. Original compute thread: %s", Thread.currentThread().getName()));
//                    Promise.async(downstream -> {
////                        Execution.fork().start(forkedExec -> {
//                            logger.info(String.format("B. Promise compute thread: %s", Thread.currentThread().getName()));
//                            Thread.sleep(15000);
//                            downstream.success("hello from async promise");
////                        });
//                    }).fork().then(result -> {
//                        ctx.render(result);
//                    });
//                    logger.info(String.format("C. Original compute thread: %s", Thread.currentThread().getName()));
//                })
                .get("config", ctx -> ctx.render(Jackson.json(ctx.get(DatabaseConfig.class))))
                .get("foo", ctx -> {
                    final DSLContext dsl = ctx.get(DSLContext.class);
                    List<Employee> result = dsl.select().from(org.superbiz.model.jooq.tables.Employee.EMPLOYEE)
                            .fetchInto(Employee.class);
                    ctx.render(Jackson.json(result));
                })
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
                .prefix("employee", EmployeeChainAction.class)
                .prefix("api", ApiChainAction.class)
                .files(f -> f.dir("static"))
                .all(ctx -> {
                    ctx.render(ctx.file("static/index.html"));
                })
        ));

//        if (getDevMode()) {
//            final Runnable task = () -> WebpackProcess.runWebpack();
//            task.run();
//        }
    }

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

    private static boolean getDevMode() {
        String devMode = System.getProperty("devMode", "true");
        return "true".equalsIgnoreCase(devMode);
    }
}
