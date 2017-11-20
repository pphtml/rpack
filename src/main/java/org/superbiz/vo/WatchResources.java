package org.superbiz.vo;

import org.superbiz.Application;
import org.superbiz.LoggingConfig;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatchResources {
    // http://www.baeldung.com/java-nio2-watchservice

    static {
        System.setProperty("java.util.logging.config.class", LoggingConfig.class.getName());
    }

    private static final Logger logger = Logger.getLogger(WatchResources.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        watchResourcesDirectory();
    }

    private static void watchResourcesDirectory() throws IOException, InterruptedException {
        final Path resourcesPath = Paths.get("src/main/resources");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        resourcesPath.register(watchService, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE, OVERFLOW);
        logger.info(String.format("Started watching %s", resourcesPath.toAbsolutePath()));
        while (true) {
            final WatchKey watchKey = watchService.take();
            for (WatchEvent<?> pollEvent : watchKey.pollEvents()) {
                logger.info(String.format("%s, %s", pollEvent, pollEvent.kind()));
            }
        }
    }
}
