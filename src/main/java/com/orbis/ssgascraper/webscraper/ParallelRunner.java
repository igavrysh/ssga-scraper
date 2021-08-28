package com.orbis.ssgascraper.webscraper;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.apache.commons.lang3.math.NumberUtils.min;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Parallel runner is used for running tasks in parallel,
 * e.g. parsing separate fund details
 */
public class ParallelRunner {
  private static final int MAX_NUMBER_OF_THREADS = 5;

  private ExecutorService threadPool;
  private final List<Runnable> tasks;
  private final CountDownLatch countDownLatch;
  private final List<Exception> errors;

  /**
   * Create instance of parallel runner with list of tasks
   *
   * @param tasks list of tasks that should be ran in parallel
   */
  private ParallelRunner(List<Runnable> tasks) {
    requireNonNull(tasks);
    this.tasks = tasks;
    threadPool = newFixedThreadPool(min(tasks.size(), MAX_NUMBER_OF_THREADS));
    countDownLatch = new CountDownLatch(tasks.size());
    errors = new ArrayList<>();
  }

  private void runningTasks() throws Exception {

    tasks.stream().map(task -> (Runnable) () -> {
      try {
        task.run();
      } catch (Exception e) {
        errors.add(e);
      } finally {
        countDownLatch.countDown();
      }
    }).forEach(threadPool::execute);

    try {
      countDownLatch.await();
    } catch(InterruptedException e) {
      errors.add(e);
    } finally {
      threadPool.shutdown();
      threadPool = null;
    }

    if (!errors.isEmpty()) {
      throw errors.get(0);
    }
  }

  /**
   * Start of execution of all parallel tasks
   *
   * @param tasks
   * @throws Exception
   */
  public static void executeTasksInParallel(List<Runnable> tasks) throws Exception {
    new ParallelRunner(tasks).runningTasks();
  }

}
