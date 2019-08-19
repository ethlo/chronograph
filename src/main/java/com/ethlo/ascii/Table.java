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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ethlo.util.StringUtil;

public class Table
{
    private final TableRow header;
    private final List<TableRow> rows;
    private final Map<Integer, Integer> minColumnWidths;
    private final int tableWidth;

    private final TableTheme theme;

    public Table(TableTheme theme, final TableRow header, final List<TableRow> rows)
    {
        this.theme = theme;
        this.header = header;
        this.rows = rows;

        final List<TableRow> all = new LinkedList<>();
        all.add(header);
        all.addAll(rows);
        this.minColumnWidths = getMaxLengths(all);
        this.tableWidth = calculateTotalWidth(minColumnWidths);
    }

    private static Map<Integer, Integer> getMaxLengths(final List<TableRow> rows)
    {
        final Map<Integer, Integer> maxLengths = new HashMap<>();
        for (TableRow row : rows)
        {
            for (int i = 0; i < row.getCells().size(); i++)
            {
                maxLengths.compute(i, (key, value) ->
                {
                    final int cellLength = row.getCells().get(key).getValue().length();
                    return value != null ? Math.max(value, cellLength) : cellLength;
                });
            }
        }
        return maxLengths;
    }

    private int calculateTotalWidth(final Map<Integer, Integer> maxLengths)
    {
        final int paddingSpace = maxLengths.size() * 2 * theme.getPadding().length();
        final int barSpace = (maxLengths.size() + 1) * theme.getVerticalSeparator().length();
        final int cellSpace = maxLengths.values().stream().reduce(0, Integer::sum);
        return cellSpace + paddingSpace + barSpace;
    }

    public String render()
    {
        return "\n" + toString(header, rows);
    }

    private String toString(TableRow header, final List<TableRow> rows)
    {
        final StringBuilder sb = new StringBuilder();

        // Header top bar
        if (theme.getVerticalSeparator().length() != 0)
        {
            sb.append(StringUtil.repeat(verticalSep(), tableWidth)).append("\n");
        }

        // Header columns
        sb.append(toString(header));

        // Header bottom bar
        if (theme.getVerticalSeparator().length() != 0)
        {
            sb.append("\n").append(StringUtil.repeat(verticalSep(), tableWidth));
        }

        for (TableRow row : rows)
        {
            sb.append("\n").append(toString(row));
        }

        return sb.toString();
    }


    private String toString(final TableRow row)
    {
        final StringBuilder sb = new StringBuilder();

        if (row.getCells().size() == 1)
        {
            // Totals row
            if (theme.getVerticalSeparator().length() != 0)
            {
                sb.append(color(StringUtil.repeat(verticalSep(), tableWidth), theme.getVerticalSpacerColor())).append("\n");
            }

            // Totals
            final int minWidth = tableWidth - ((theme.getPadding().length() * 2) + (theme.getHorizontalSeparator().length() * 2)) + 1;
            sb.append(horisontalSep()).append(padding()).append(color(row.getCells().get(0).getRendered(minWidth), theme.getStringColor())).append(color(theme.getHorizontalSeparator(), theme.getHorizontalSpacerColor()));

            if (theme.getVerticalSeparator().length() != 0)
            {
                sb.append("\n").append(color(StringUtil.repeat(verticalSep(), tableWidth), theme.getVerticalSpacerColor()));
            }
            return sb.toString();
        }
        else
        {
            // Normal row
            for (int i = 0; i < row.getCells().size(); i++)
            {
                final String value = row.getCells().get(i).getRendered(minColumnWidths.get(i));
                final String cellValue = color(theme.getHorizontalSeparator() + theme.getPadding(), theme.getHorizontalSpacerColor()) + color(value, isNumeric(value) ? theme.getNumericColor() : theme.getStringColor()) + padding();
                sb.append(cellValue);
            }
            return sb.append(horisontalSep()).toString();
        }
    }

    private String horisontalSep()
    {
        return color(theme.getHorizontalSeparator(), theme.getHorizontalSpacerColor());
    }

    private String verticalSep()
    {
        return color(theme.getVerticalSeparator(), theme.getVerticalSpacerColor());
    }

    private String padding()
    {
        return color(theme.getPadding(), AnsiColor.BLACK);
    }

    private boolean isNumeric(final String value)
    {
        try
        {
            final String stripped = StringUtil.stripAll(value, ",", ".", ":", " ms", " s", " μs", " n", "%");
            Double.parseDouble(stripped);
            return true;
        }
        catch (NumberFormatException exc)
        {
            return false;
        }
    }

    private String color(final String value, AnsiColor color)
    {
        return color.value() + theme.getCellBackground().value() + value + AnsiColor.RESET.value();
    }
}
