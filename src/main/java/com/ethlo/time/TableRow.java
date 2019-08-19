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

import java.util.LinkedList;
import java.util.List;

public class TableRow
{
    private final List<String> cells = new LinkedList<>();

    public List<String> getCells()
    {
        return cells;
    }

    public int getLength()
    {
        return cells.stream().map(String::length).reduce(0, Integer::sum);
    }

    public TableRow append(final String value)
    {
        cells.add(value);
        return this;
    }

    public Integer getCellWidth(final int cellIndex)
    {
        return cells.get(cellIndex).length();
    }
}
