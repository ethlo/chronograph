package com.ethlo.time;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 Morten Haraldsen (ethlo)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

/**
 * A utility for tracking and timing tasks with high precision.
 * <p>
 * Chronograph allows measuring execution times for different types of tasks,
 * supports hierarchical task structures, and provides formatted output of the collected timing data.
 * </p>
 *
 * <div> Features:
 * <ul>
 *     <li>Supports nested task timing</li>
 *     <li>Provides a structured summary of task execution times</li>
 *     <li>Offers various timing methods for functions, consumers, and suppliers</li>
 *     <li>Formats results for easy visualization</li>
 * </ul>
 * </div>
 */
public class Chronograph
{
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private final Deque<MutableTaskInfo> taskStack = new ArrayDeque<>(); // Tracks the active task
    private final Map<String, MutableTaskInfo> tasksByName = new LinkedHashMap<>();

    private final CaptureConfig captureConfig;
    private final String name;

    private Chronograph(final String name)
    {
        this(name, CaptureConfig.DEFAULT);
    }

    private Chronograph(final String name, final CaptureConfig captureConfig)
    {
        this.name = name;
        this.captureConfig = captureConfig;
    }

    /**
     * Creates a new Chronograph instance without a name.
     *
     * @return a new Chronograph instance.
     */
    public static Chronograph create()
    {
        return new Chronograph("");
    }

    public static Chronograph create(final CaptureConfig captureConfig)
    {
        return create("", captureConfig);
    }

    public static Chronograph create(String name)
    {
        return new Chronograph(name);
    }

    public static Chronograph create(String name, final CaptureConfig captureConfig)
    {
        return new Chronograph(name, captureConfig);
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     */
    public void time(final String taskName, final Runnable task)
    {
        try
        {
            start(taskName);
            task.run();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param input    The input to the function
     */
    public <R, T> R time(final String taskName, final Function<T, R> task, T input)
    {
        try
        {
            start(taskName);
            return task.apply(input);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     */
    public <R> R time(final String taskName, final Supplier<R> task)
    {
        try
        {
            start(taskName);
            return task.get();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U> void time(final String taskName, final BiConsumer<T, U> task, T t, U u)
    {
        try
        {
            start(taskName);
            task.accept(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U, R> R time(final String taskName, final BiFunction<T, U, R> task, T t, U u)
    {
        try
        {
            start(taskName);
            return task.apply(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t1       the first value
     * @param t2       the second value
     */
    public <T> T time(final String taskName, final BinaryOperator<T> task, T t1, T t2)
    {
        try
        {
            start(taskName);
            return task.apply(t1, t2);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U> boolean time(final String taskName, final BiPredicate<T, U> task, T t, U u)
    {
        try
        {
            start(taskName);
            return task.test(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     */
    public boolean time(final String taskName, final BooleanSupplier task)
    {
        try
        {
            start(taskName);
            return task.getAsBoolean();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     */
    public <T> void time(final String taskName, final Consumer<T> task, T t)
    {
        try
        {
            start(taskName);
            task.accept(t);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d1       the first value
     * @param d2       the second value
     */
    public double time(final String taskName, final DoubleBinaryOperator task, double d1, double d2)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(d1, d2);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        The value to process
     */
    public void time(final String taskName, final DoubleConsumer task, double d)
    {
        try
        {
            start(taskName);
            task.accept(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        the value to process
     */
    public <R> R time(final String taskName, final DoubleFunction<R> task, double d)
    {
        try
        {
            start(taskName);
            return task.apply(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        the value to process
     */
    public boolean time(final String taskName, final DoublePredicate task, double d)
    {
        try
        {
            start(taskName);
            return task.test(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     **/
    public double time(final String taskName, final DoubleSupplier task)
    {
        try
        {
            start(taskName);
            return task.getAsDouble();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        the value to convert
     */
    public int time(final String taskName, final DoubleToIntFunction task, double d)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        the value to convert
     */
    public long time(final String taskName, final DoubleToLongFunction task, double d)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param d        the value to process
     */
    public double time(final String taskName, final DoubleUnaryOperator task, double d)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i1       the first value
     * @param i2       the second value
     */
    public int time(final String taskName, final IntBinaryOperator task, int i1, int i2)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(i1, i2);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to evaluate
     */
    public void time(final String taskName, final IntConsumer task, int i)
    {
        try
        {
            start(taskName);
            task.accept(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to process
     */
    public <R> R time(final String taskName, final IntFunction<R> task, int i)
    {
        try
        {
            start(taskName);
            return task.apply(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to evaluate
     */
    public boolean time(final String taskName, final IntPredicate task, int i)
    {
        try
        {
            start(taskName);
            return task.test(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     */
    public int time(final String taskName, final IntSupplier task)
    {
        try
        {
            start(taskName);
            return task.getAsInt();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to process
     */
    public double time(final String taskName, final IntToDoubleFunction task, int i)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to convert
     */
    public long time(final String taskName, final IntToLongFunction task, int i)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param i        the value to process
     */
    public int time(final String taskName, final IntUnaryOperator task, int i)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l1       the first value
     * @param l2       the second value
     */
    public long time(final String taskName, final LongBinaryOperator task, long l1, long l2)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(l1, l2);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to consume
     */
    public void time(final String taskName, final LongConsumer task, long l)
    {
        try
        {
            start(taskName);
            task.accept(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to process
     */
    public <R> R time(final String taskName, final LongFunction<R> task, long l)
    {
        try
        {
            start(taskName);
            return task.apply(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to process
     */
    public boolean time(final String taskName, final LongPredicate task, long l)
    {
        try
        {
            start(taskName);
            return task.test(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     */
    public long time(final String taskName, final LongSupplier task)
    {
        try
        {
            start(taskName);
            return task.getAsLong();
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to convert
     */
    public double time(final String taskName, final LongToDoubleFunction task, long l)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to convert
     */
    public int time(final String taskName, final LongToIntFunction task, long l)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param l        the value to process
     */
    public long time(final String taskName, final LongUnaryOperator task, long l)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param d        the second value
     */
    public <T> void time(final String taskName, final ObjDoubleConsumer<T> task, T t, double d)
    {
        try
        {
            start(taskName);
            task.accept(t, d);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param i        the second value
     */
    public <T> void time(final String taskName, final ObjIntConsumer<T> task, T t, int i)
    {
        try
        {
            start(taskName);
            task.accept(t, i);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param l        the second value
     */
    public <T> void time(final String taskName, final ObjLongConsumer<T> task, T t, long l)
    {
        try
        {
            start(taskName);
            task.accept(t, l);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U> double time(final String taskName, final ToDoubleBiFunction<T, U> task, T t, U u)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the value to process
     */
    public <T> double time(final String taskName, final ToDoubleFunction<T> task, T t)
    {
        try
        {
            start(taskName);
            return task.applyAsDouble(t);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U> int time(final String taskName, final ToIntBiFunction<T, U> task, T t, U u)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the value to process
     */
    public <T> int time(final String taskName, final ToIntFunction<T> task, T t)
    {
        try
        {
            start(taskName);
            return task.applyAsInt(t);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     * @param u        the second value
     */
    public <T, U> long time(final String taskName, final ToLongBiFunction<T, U> task, T t, U u)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(t, u);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     */
    public <T> long time(final String taskName, final ToLongFunction<T> task, T t)
    {
        try
        {
            start(taskName);
            return task.applyAsLong(t);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName The name of the task being measured.
     * @param task     The task to execute.
     * @param t        the first value
     */
    public <T> T time(final String taskName, final UnaryOperator<T> task, T t)
    {
        try
        {
            start(taskName);
            return task.apply(t);
        } finally
        {
            stop();
        }
    }

    /**
     * Measures execution time for a given task.
     *
     * @param taskName  The name of the task being measured.
     * @param predicate The task to execute.
     * @param t         the value to evaluate
     */
    public <T> boolean time(String taskName, Predicate<T> predicate, T t)
    {
        try
        {
            start(taskName);
            return predicate.test(t);
        } finally
        {
            stop();
        }
    }

    public boolean start(String task)
    {
        if (task == null)
        {
            throw new IllegalArgumentException("task cannot be null");
        }

        final MutableTaskInfo taskInfo = tasksByName.computeIfAbsent(task, t ->
        {
            final MutableTaskInfo parent = taskStack.peek();
            if (captureConfig.getMinInterval().equals(Duration.ZERO))
            {
                MutableTaskInfo newTask;
                if (captureConfig.getMinInterval().equals(Duration.ZERO))
                {
                    newTask = new MutableTaskInfo(task, parent);
                }
                else
                {
                    newTask = new RateLimitedTaskInfo(task, captureConfig.getMinInterval(), scheduledExecutorService, parent);
                }

                return newTask;
            }
            return new RateLimitedTaskInfo(task, captureConfig.getMinInterval(), scheduledExecutorService, parent);
        });
        taskStack.push(taskInfo);
        return taskInfo.start();
    }

    public boolean stop()
    {
        final long ts = System.nanoTime();
        if (!taskStack.isEmpty())
        {
            final MutableTaskInfo task = taskStack.pop();
            return task.stopped(ts);
        }
        return false;
    }

    public boolean isAnyRunning()
    {
        return !taskStack.isEmpty();
    }

    public void resetAll()
    {
        taskStack.clear();
        tasksByName.clear();
    }

    public TaskInfo getTask(final String task)
    {
        return findByName(task).orElseThrow(() -> new IllegalStateException("Unknown task " + task));
    }

    private Optional<TaskInfo> findByName(String task)
    {
        return Optional.ofNullable(tasksByName.get(task));
    }

    public List<TaskInfo> getTasks()
    {
        return List.copyOf(tasksByName.values().stream().filter(t -> t.getDepth() == 0).toList());
    }

    public boolean isRunning(String task)
    {
        return Optional.ofNullable(tasksByName.get(task)).map(MutableTaskInfo::isRunning).orElse(false);
    }

    public Duration getTotalTime()
    {
        return Duration.ofNanos(tasksByName.values().stream().map(TaskInfo::getTotalTaskTime).map(Duration::toNanos).reduce(0L, Long::sum));
    }

    public ChronographData getTaskData()
    {
        return new ChronographData(name, getTasks());
    }

    public void stopAll()
    {
        final long ts = System.nanoTime();
        while (!taskStack.isEmpty())
        {
            final MutableTaskInfo task = taskStack.pop();
            task.stopped(ts);
        }
    }
}
