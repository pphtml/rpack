package org.superbiz.utils;

import org.superbiz.LoggingConfig;
import rx.subjects.PublishSubject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RxWebpackProcess {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(RxWebpackProcess.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        runWebpack();
//        new StreamGobbler(p.getErrorStream()).start();
//        new StreamGobbler(p.getInputStream()).start();
    }

    public static void runWebpack() {
        logger.info("Starting Webpack process");

        try {
            //Observable<String> observable = Observable.create(subscriber -> subscriber.onNext("ble"));

//            Observable<String> observable = Observable.fromCallable(() -> {
//                return "abcd";
//            });

            PublishSubject<String> subject = PublishSubject.create();


            subject.subscribe(action -> {
                System.out.println(action);
            });

            final Process process = new ProcessBuilder()
                    .command("node/node",
                            "node_modules/webpack/bin/webpack.js",
                            "--watch")
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .directory(new File("src/main/frontend"))
                    .start();
            try (final InputStream stdOut = process.getInputStream();
                 final InputStreamReader isr = new InputStreamReader(stdOut);
                 final BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    logger.info(String.format("-> %s", line));
                    subject.onNext(line);
                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Webpack process stopped", e);
        }
    }
}
