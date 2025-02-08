package com.ethlo.time;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2025 Morten Haraldsen (ethlo)
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

import java.io.Serializable;
import java.util.List;

public class SerializableChronographData implements Serializable
{
    private final String name;
    private final List<SerializableTaskInfo> tasks;

    public SerializableChronographData(ChronographData data, OutputConfig outputConfig)
    {
        this(data.getName(), data.getRootTasks().stream().map(task -> SerializableTaskInfo.create(task, outputConfig)).toList());
    }

    public SerializableChronographData(String name, final List<SerializableTaskInfo> tasks)
    {
        this.name = name;
        this.tasks = tasks;
    }

    public List<SerializableTaskInfo> getTasks()
    {
        return tasks;
    }

    public String getName()
    {
        return name;
    }
}
