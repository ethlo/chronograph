package com.ethlo.chronograph.internal.ascii;

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
    private final List<TableCell> cells = new LinkedList<>();

    public List<TableCell> getCells()
    {
        return cells;
    }

    public TableRow append(final TableCell cell)
    {
        cells.add(cell);
        return this;
    }

    public TableRow append(final String value)
    {
        this.append(new TableCell(value));
        return this;
    }
}
