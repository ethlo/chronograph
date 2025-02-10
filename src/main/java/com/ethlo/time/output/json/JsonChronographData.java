package com.ethlo.time.output.json;

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

import com.ethlo.time.ChronographData;
import com.ethlo.time.OutputConfig;

public class JsonChronographData implements Serializable
{
    private final String name;
    private final List<JsonTaskInfo> tasks;

    public JsonChronographData(ChronographData data, OutputConfig outputConfig)
    {
        this(data.getName(), data.getRootTasks().stream().map(task -> JsonTaskInfo.create(task, outputConfig)).toList());
    }

    public JsonChronographData(String name, final List<JsonTaskInfo> tasks)
    {
        this.name = name;
        this.tasks = tasks;
    }

    public List<JsonTaskInfo> getTasks()
    {
        return tasks;
    }

    public String getName()
    {
        return name;
    }
}
