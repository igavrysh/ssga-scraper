package com.orbis.ssgascraper.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
@Component
public class ExecutorServiceManager {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ExecutorServiceManager.class));

    public ExecutorService getNewFixedThreadPool(int maxThreadCount, String executorServiceName) {
        try {
            Executors.newFixedThreadPool(maxThreadCount);
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Provided maxThreadCount [value=%s] is not an integer: %s ",
                            maxThreadCount, ex));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Failed to get new NewFixedThreadPool %s executor service. Reason %s",
                            executorServiceName, ex));
        }
        return null;
    }

    public ExecutorService getCachedThreadPool(String executorServiceName) {
        try {
            return Executors.newCachedThreadPool();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Failed to get CachedThreadPool for %s executor service. Reason %s",
                            executorServiceName, ex));
        }
        return null;
    }

    public void waitForTaskCompletion(List<Future<?>> futureList, ExecutorService executorService) {
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the value form the future.");
            }
        }
    }

    public void scheduleTermination(int seconds, String executorServiceName, ExecutorService executorService) {
        try {
            if (executorService.awaitTermination(seconds, TimeUnit.SECONDS)) {
                LOGGER.log(Level.INFO,
                        String.format("Task completed for executive service %s", executorServiceName));
            } else {
                LOGGER.log(Level.INFO,
                        String.format("Forced shutdown for executive service %s", executorServiceName));
                executorService.shutdownNow();
            }

        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Executor service is interrupeted for executive service %s", executorServiceName));
        }
    }

}
