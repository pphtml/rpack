package org.superbiz.utils;

import org.superbiz.LoggingConfig;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebpackProcess {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(WebpackProcess.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        runWebpack();
//        new StreamGobbler(p.getErrorStream()).start();
//        new StreamGobbler(p.getInputStream()).start();
    }

    public static void runWebpack() {
        logger.info("Starting Webpack process");

        try {
            new ProcessBuilder()
                    .command("node/node",
                            "node_modules/webpack/bin/webpack.js",
                            "--watch")
                    .inheritIO()
                    .directory(new File("src/main/frontend"))
                    .start()
                    .waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Webpack process stopped", e);
        }
    }
}
