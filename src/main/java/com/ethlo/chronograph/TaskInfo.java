package com.ethlo.chronograph;

/*-
 * #%L
 * stopwatch
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
import java.util.List;

import com.ethlo.chronograph.statistics.PerformanceStatistics;

/**
 * Represents information about a task, including its performance data,
 * execution times, and its relationship to other tasks (subtasks and parent).
 */
public interface TaskInfo
{

    /**
     * Gets the name of the task.
     *
     * @return the name of the task
     */
    String getName();

    /**
     * Gets the total execution time of the task.
     *
     * @return the total time taken by the task
     */
    Duration getTime();

    /**
     * Gets the number of times this task has been invoked.
     *
     * @return the number of invocations
     */
    long getInvocations();

    /**
     * Gets the sample size used for performance statistics.
     *
     * @return the sample size
     */
    long getSampleSize();

    /**
     * Gets the performance statistics for the task, including average times, etc.
     *
     * @return the performance statistics of the task
     */
    PerformanceStatistics getStatistics();

    /**
     * Gets the time spent executing the task itself, excluding time spent in subtasks.
     *
     * @return the self time of the task
     */
    Duration getSelfTime();

    /**
     * Gets the total time spent executing all subtasks of this task.
     *
     * @return the subtasks time
     */
    Duration getSubtasksTime();

    /**
     * Gets the depth of the task within its hierarchy.
     *
     * @return the depth of the task
     */
    int getDepth();

    /**
     * Gets the list of subtasks associated with this task.
     *
     * @return the list of subtasks
     */
    List<TaskInfo> getSubtasks();

    /**
     * Gets the parent task of this task.
     *
     * @return the parent task, or null if this is the top-level task
     */
    TaskInfo getParent();
}
