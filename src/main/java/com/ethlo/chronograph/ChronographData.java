package com.ethlo.chronograph;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ethlo.chronograph.internal.MutableTaskInfo;

/**
 * Holder of the data for a {@link Chronograph} run
 */
public class ChronographData
{
    private final String name;
    private final List<TaskInfo> rootTasks;
    private final Duration totalTime;

    /**
     * Create a new insatnce with the given name and tasks
     *
     * @param name      The name of the {@link Chronograph}
     * @param rootTasks The tasks collected
     */
    public ChronographData(final String name, final List<TaskInfo> rootTasks)
    {
        this.name = name;
        this.rootTasks = rootTasks;
        this.totalTime = Duration.ofNanos(rootTasks.stream().mapToLong(t -> t.getTime().toNanos()).sum());
    }

    /**
     * Merge the data of the provided list
     *
     * @param input The data to merge
     * @return The merged data
     */
    public static ChronographData merge(final List<Chronograph> input)
    {
        return merge(null, input);
    }

    /**
     * Merge all data from multiple instances.
     *
     * @param name  The name of the merged data
     * @param input The instances to merge
     * @return A merged instance
     */
    public static ChronographData merge(final String name, final List<Chronograph> input)
    {
        if (input.isEmpty())
        {
            throw new IllegalArgumentException("No results to combine");
        }
        if (input.size() == 1)
        {
            return input.get(0).getTaskData();
        }
        else
        {
            ChronographData last = input.get(0).getTaskData();
            for (int i = 1; i < input.size(); i++)
            {
                last = last.merge(name, input.get(i).getTaskData());
            }
            return last;
        }
    }

    private static void flattenTaskInfo(TaskInfo task, List<TaskInfo> result)
    {
        result.add(task);
        for (TaskInfo child : task.getSubtasks())
        {
            flattenTaskInfo(child, result);
        }
    }

    private static List<TaskInfo> mergeTaskInfoLists(List<TaskInfo> list1, List<TaskInfo> list2)
    {
        // Create a new list to hold the merged results
        List<TaskInfo> mergedList = new ArrayList<>(list1);

        for (TaskInfo task2 : list2)
        {
            // Check if task2 already exists in the merged list
            Optional<TaskInfo> existingTaskOpt = mergedList.stream()
                    .filter(existingTask -> existingTask.getName().equals(task2.getName()))
                    .findFirst();

            if (existingTaskOpt.isPresent())
            {
                // If the task exists, merge the data
                MutableTaskInfo existingTask = (MutableTaskInfo) existingTaskOpt.get();
                existingTask.merge((MutableTaskInfo) task2);  // Merge the data from task2 into the existing task
            }
            else
            {
                // If the task does not exist, add it to the merged list
                mergedList.add(task2);
            }
        }

        return mergedList;
    }

    /**
     * Get the name of the Chronograph
     *
     * @return the name of the Chronograph or null if it was not set
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the root (top-level) tasks
     *
     * @return The root tasks
     */
    public List<TaskInfo> getRootTasks()
    {
        return rootTasks;
    }

    /**
     * Get the total execution time of all tasks
     *
     * @return the total execution time of all tasks
     */
    public Duration getTotalTime()
    {
        return totalTime;
    }

    /**
     * Check if there are any tasks
     *
     * @return True if empty, otherwise false
     */
    public boolean isEmpty()
    {
        return this.rootTasks.isEmpty();
    }

    /**
     * Merge with another instance
     *
     * @param name            The name of the new instance
     * @param chronographData The data to merge with
     * @return A new instance with the data of this and the specified instance
     */
    public ChronographData merge(String name, ChronographData chronographData)
    {
        return new ChronographData(name, mergeTaskInfoLists(this.rootTasks, chronographData.rootTasks));
    }

    /**
     * Get a flat list of all the tasks
     *
     * @return a flat list of all the tasks
     */
    public List<TaskInfo> getTasks()
    {
        final List<TaskInfo> result = new ArrayList<>();
        getRootTasks().forEach(root -> flattenTaskInfo(root, result));
        return result;
    }

    /**
     * Get the total number of task invocations summed across all tasks
     *
     * @return the total number of task invocations
     */
    public long getTotalInvocations()
    {
        return getTasks().stream().map(TaskInfo::getInvocations).reduce(0L, Long::sum);
    }
}
