package com.ethlo.time;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import com.ethlo.ascii.TableTheme;

public abstract class Chronograph
{
    private final CaptureConfig captureConfig = CaptureConfig.builder().sampleRate(Duration.ofMillis(10)).build();

    private static TableTheme theme = TableTheme.DEFAULT;
    private static OutputConfig outputConfig = OutputConfig.DEFAULT;

    public static void configure(TableTheme theme, OutputConfig outputConfig)
    {
        configure(theme);
        configure(outputConfig);
    }

    public static void configure(final TableTheme theme)
    {
        Chronograph.theme = Objects.requireNonNull(theme, "theme cannot be null");
    }

    public static void configure(final OutputConfig outputConfig)
    {
        Chronograph.outputConfig = Objects.requireNonNull(outputConfig, "outputConfig cannot be null");
    }

    public static Chronograph create()
    {
        return new ChronographImpl();
    }


    public void timed(final String taskName, final Runnable task)
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

    public <R, T> R timedFunction(final String taskName, final Function<T, R> task, T input)
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

    public <R, T> R timedSupplier(final String taskName, final Supplier<R> task)
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

    public String prettyPrint(final String title)
    {
        return Report.prettyPrint(this, outputConfig.begin().title(title).build(), theme);
    }

    public String prettyPrint()
    {
        return Report.prettyPrint(this, outputConfig, theme);
    }

    public abstract void start(String task);

    public abstract void stop();

    public abstract boolean isAnyRunning();

    public abstract void stop(String task);

    public abstract void resetAll();

    public abstract TaskInfo getTasks(String task);

    public abstract List<TaskInfo> getTasks();

    public abstract boolean isRunning(String task);

    public abstract Duration getTotalTime();
}
