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

    private boolean useColors = false;

    private String stringColor = Colors.ANSI_GREEN;
    private String numericColor = Colors.ANSI_RED;
    private String horisontalSpacerColor = Colors.ANSI_GRAY;
    private String verticalSpacerColor = Colors.ANSI_GRAY;
    private String cellBackground = Colors.ANSI_BLACK_BACKGROUND;
    private String verticalSep = "|";
    private String horisontalSep = "-";
    private String padding = " ";

    public Table(final TableRow header, final List<TableRow> rows)
    {
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
        final int paddingSpace = maxLengths.size() * 2 * padding.length();
        final int barSpace = (maxLengths.size() + 1) * verticalSep.length();
        final int cellSpace = maxLengths.values().stream().reduce(0, Integer::sum);
        return cellSpace + paddingSpace + barSpace;
    }

    public String render()
    {
        return toString(header, rows);
    }

    private String toString(TableRow header, final List<TableRow> rows)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n").append(StringUtil.repeat(horisontalSep(), tableWidth)).append("\n");
        sb.append(toString(header));
        sb.append("\n").append(StringUtil.repeat(horisontalSep(), tableWidth));

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
            sb.append(color(StringUtil.repeat(horisontalSep(), tableWidth), horisontalSpacerColor));
            sb.append("\n").append(verticalSep()).append(padding()).append(color(row.getCells().get(0).getRendered(tableWidth - ((padding.length() * 2) + verticalSep.length())), stringColor)).append(color(verticalSep, verticalSpacerColor));
            sb.append("\n").append(color(StringUtil.repeat(horisontalSep(), tableWidth), horisontalSpacerColor));
            return sb.toString();
        }
        else
        {
            // Normal row
            for (int i = 0; i < row.getCells().size(); i++)
            {
                final String value = row.getCells().get(i).getRendered(minColumnWidths.get(i));
                final String cellValue = color(verticalSep + padding, verticalSpacerColor) + color(value, isNumeric(value) ? numericColor : stringColor) + padding();
                sb.append(cellValue);
            }
            return sb.append(verticalSep()).toString();
        }
    }

    private String horisontalSep()
    {
        return color(horisontalSep, horisontalSpacerColor);
    }

    private String verticalSep()
    {
        return color(verticalSep, verticalSpacerColor);
    }

    private String padding()
    {
        return color(padding, Colors.ANSI_BLACK);
    }

    private boolean isNumeric(final String value)
    {
        try
        {
            final String stripped = StringUtil.stripAll(value, ",", ".", " ms", " s", " μs", " n", "%");
            Double.parseDouble(stripped);
            return true;
        }
        catch (NumberFormatException exc)
        {
            return false;
        }
    }

    private String color(final String value, String color)
    {
        return useColors ? color + cellBackground + value + Colors.ANSI_RESET : value;
    }
}
