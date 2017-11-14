package org.superbiz.utils;

import org.superbiz.LoggingConfig;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RxWebpackProcess {
    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(RxWebpackProcess.class.getName());

    interface Node {
        String getText();
    }

    static class NodeInterval implements Node {
        private final Long ordinal;

        private NodeInterval(Long ordinal) {
            this.ordinal = ordinal;
        }

        public static NodeInterval of(Long ordinal) {
            return new NodeInterval(ordinal);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("NodeInterval{");
            sb.append("ordinal=").append(ordinal);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public String getText() {
            throw new UnsupportedOperationException();
        }
    }

    static class NodeText implements Node {
        private final String text;

        private NodeText(String text) {
            this.text = text;
        }

        public static NodeText of(String text) {
            return new NodeText(text);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("NodeText{");
            sb.append("text='").append(text).append('\'');
            sb.append('}');
            return sb.toString();
        }

        @Override
        public String getText() {
            return this.text;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Observable<Node> observableInterval = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(l -> NodeInterval.of(l));
        final Observable<Node> observableText = Observable
                .interval(200, TimeUnit.MILLISECONDS)
                .map(l -> NodeText.of(l.toString()));
        final Observable<Node> observableCombined = observableInterval.mergeWith(observableText);

        final Action1<Node> subscriber = new Action1<Node>() {
            private long lastUpdateText = -1;
            private long lastUpdateInterval = -1;
            private StringBuilder stringBuilder = new StringBuilder();
            private boolean freshBuilder = true;

            @Override
            public void call(Node node) {
                if (node instanceof NodeInterval) {
                    this.lastUpdateInterval = System.currentTimeMillis();
                    if (lastUpdateText > -1) {
                        if (lastUpdateInterval - lastUpdateText > 300) {
                            logger.info(String.format("AGGREGATED: %s", stringBuilder.toString()));
                            this.stringBuilder = new StringBuilder();
                            this.freshBuilder = true;
                        }
                    }
                } else if (node instanceof NodeText) {
                    if (!freshBuilder) {
                        this.stringBuilder.append('\n');
                        this.freshBuilder = false;
                    }
                    this.stringBuilder.append(node.getText());
                    this.lastUpdateText = System.currentTimeMillis();
                }
            }
        };
        //observableCombined.subscribe(l -> System.out.println(l));
        observableCombined.subscribe(subscriber);
        Thread.sleep(5000);
        //runWebpack();
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
