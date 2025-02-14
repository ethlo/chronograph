package com.ethlo.time.chronograph.internal.ascii;

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

import com.ethlo.time.chronograph.internal.util.StringUtil;
import com.ethlo.time.chronograph.output.table.AnsiBackgroundColor;
import com.ethlo.time.chronograph.output.table.AnsiColor;
import com.ethlo.time.chronograph.output.table.TableTheme;

public class TableCell
{
    private final String value;
    private final boolean left;
    private final boolean isNumeric;

    public TableCell(final String value)
    {
        this(value, true, false);
    }

    public TableCell(final String value, final boolean left, final boolean isNumeric)
    {
        this.value = value;
        this.left = left;
        this.isNumeric = isNumeric;
    }

    public static String color(final String value, AnsiColor color, AnsiBackgroundColor backgroundColor)
    {
        if (color == AnsiColor.NONE && backgroundColor == AnsiBackgroundColor.NONE)
        {
            return value;
        }
        return color.value() + backgroundColor.value() + value + AnsiColor.RESET.value();
    }

    public String getValue()
    {
        return value;
    }

    public String render(TableTheme theme, int minWidth)
    {
        final String paddedValue = left ? StringUtil.adjustPadRight(value, minWidth) : StringUtil.adjustPadLeft(value, minWidth);
        return color(theme.getHorizontalSeparator() + theme.getPadding(), theme.getHorizontalSpacerColor(), theme.getCellBackground()) + color(paddedValue, isNumeric ? theme.getNumericColor() : theme.getStringColor(), theme.getCellBackground()) + padding(theme);
    }

    private String padding(TableTheme theme)
    {
        return color(theme.getPadding(), theme.getStringColor(), theme.getCellBackground());
    }
}
