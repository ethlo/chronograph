package com.ethlo.ascii;

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

import com.ethlo.util.StringUtil;

public class TableCell
{
    private final String value;
    private final int length;
    private final boolean left;

    public TableCell(final String value)
    {
        this(value, true);
    }

    public TableCell(final String value, final boolean left)
    {
        this(value, value.length(), left);
    }

    public TableCell(final String value, final int length, final boolean left)
    {
        this.value = value;
        this.length = length;
        this.left = left;
    }

    public String getValue()
    {
        return value;
    }

    public String getRendered(int minWidth)
    {
        return left ? StringUtil.adjustPadRight(value, minWidth) : StringUtil.adjustPadLeft(value, minWidth);
    }

    public int getLength()
    {
        return Math.min(value.length(), length);
    }
}
