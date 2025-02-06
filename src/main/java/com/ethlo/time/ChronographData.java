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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChronographData
{
    private final String name;
    private final List<TaskInfo> rootTasks;
    private final Duration totalTime;

    public ChronographData(final String name, final List<TaskInfo> rootTasks)
    {
        this.name = name;
        this.rootTasks = rootTasks;
        this.totalTime = Duration.ofNanos(rootTasks.stream().mapToLong(t -> t.getTotalTaskTime().toNanos()).sum());
    }

    public static ChronographData combine(final String name, final List<Chronograph> toCombine)
    {
        if (toCombine.isEmpty())
        {
            throw new IllegalArgumentException("No results to combine");
        }
        if (toCombine.size() == 1)
        {
            return toCombine.get(0).getTaskData();
        }
        else
        {
            ChronographData last = toCombine.get(0).getTaskData();
            for (int i = 1; i < toCombine.size(); i++)
            {
                last = last.merge(name, toCombine.get(i).getTaskData());
            }
            return last;
        }
    }

    private static void flattenTaskInfo(TaskInfo task, List<TaskInfo> result)
    {
        result.add(task);
        for (TaskInfo child : task.getChildren())
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
                TaskInfo existingTask = existingTaskOpt.get();
                existingTask.merge(task2);  // Merge the data from task2 into the existing task
            }
            else
            {
                // If the task does not exist, add it to the merged list
                mergedList.add(task2);
            }
        }

        return mergedList;
    }

    public String getName()
    {
        return name;
    }

    public List<TaskInfo> getRootTasks()
    {
        return rootTasks;
    }

    public Duration getTotalTime()
    {
        return totalTime;
    }

    public boolean isEmpty()
    {
        return this.rootTasks.isEmpty();
    }

    public ChronographData merge(String chronographName, ChronographData chronographData)
    {
        return new ChronographData(chronographName, mergeTaskInfoLists(this.rootTasks, chronographData.rootTasks));
    }

    public List<TaskInfo> getTasks()
    {
        final List<TaskInfo> result = new ArrayList<>();
        getRootTasks().forEach(root -> flattenTaskInfo(root, result));
        return result;
    }
}
