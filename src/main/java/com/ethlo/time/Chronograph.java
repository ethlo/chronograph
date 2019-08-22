package com.ethlo.time;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Chronograph
{
    default Chronograph create()
    {
        return new ChronographImpl();
    }

    default void timed(final String taskName, final Runnable task)
    {
        try
        {
            start(taskName);
            task.run();
        } finally
        {
            stop(taskName);
        }
    }

    default <R, T> R timedFunction(final String taskName, final Function<T, R> task, T input)
    {
        try
        {
            start(taskName);
            return task.apply(input);
        } finally
        {
            stop(taskName);
        }
    }

    default <R, T> R timedSupplier(final String taskName, final Supplier<R> task)
    {
        try
        {
            start(taskName);
            return task.get();
        }
        finally
        {
            stop(taskName);
        }
    }

    default String prettyPrint(final String title)
    {
        return Report.prettyPrint(this, outputConfig.begin().title(title).build(), theme);
    }


    void start(String task);

    void stop();

    boolean isAnyRunning();

    void stop(String task);

    void resetAll();

    TaskInfo getTasks(String task);

    List<TaskInfo> getTasks();

    boolean isRunning(String task);

    Duration getTotalTime();
}
