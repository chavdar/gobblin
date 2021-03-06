/* (c) 2014 LinkedIn Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.util;

import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;

import com.google.common.base.Optional;


/**
 * A utility class to use with {@link java.util.concurrent.Executors} in cases such as when creating new thread pools.
 *
 * @author ynli
 */
public class ExecutorsUtils {

  private static final ThreadFactory DEFAULT_THREAD_FACTORY = newThreadFactory(Optional.<Logger>absent());

  /**
   * Get a default {@link java.util.concurrent.ThreadFactory}.
   *
   * @return the default {@link java.util.concurrent.ThreadFactory}
   */
  public static ThreadFactory defaultThreadFactory() {
    return DEFAULT_THREAD_FACTORY;
  }

  /**
   * Get a new {@link java.util.concurrent.ThreadFactory} that uses a {@link LoggingUncaughtExceptionHandler}
   * to handle uncaught exceptions.
   *
   * @param logger an {@link com.google.common.base.Optional} wrapping the {@link org.slf4j.Logger} that the
   *               {@link LoggingUncaughtExceptionHandler} uses to log uncaught exceptions thrown in threads
   * @return a new {@link java.util.concurrent.ThreadFactory}
   */
  public static ThreadFactory newThreadFactory(final Optional<Logger> logger) {
    return new ThreadFactory() {
      @Override
      public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler(logger));
        return thread;
      }
    };
  }
}
