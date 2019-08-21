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
import java.util.List;
import java.util.Map;

import com.ethlo.util.StringUtil;

public class Table
{
    private static final String NEWLINE = System.lineSeparator();

    private final List<TableRow> rows;
    private final Map<Integer, Integer> minColumnWidths;
    private final int tableWidth;

    private final TableTheme theme;

    public Table(TableTheme theme, final List<TableRow> rows)
    {
        this.theme = theme;
        this.rows = rows;

        final Map<Integer, Boolean> hasContent = getHasColumnContent(rows);
        this.minColumnWidths = getMaxContentLengths(rows, hasContent);
        this.tableWidth = calculateTotalWidth(minColumnWidths);
    }

    private static Map<Integer, Integer> getMaxContentLengths(final List<TableRow> rows, final Map<Integer, Boolean> hasContent)
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

        hasContent.entrySet().stream().filter(e -> !e.getValue()).forEach(e -> maxLengths.remove(e.getKey()));

        return maxLengths;
    }

    private Map<Integer, Boolean> getHasColumnContent(final List<TableRow> rows)
    {
        final Map<Integer, Boolean> result = new HashMap<>();
        for (TableRow row : rows)
        {
            for (int i = 0; i < row.getCells().size(); i++)
            {
                result.compute(i, (key, value) -> row.getCells().get(key).getValue().length() > 0);
            }
        }
        return result;
    }

    private int calculateTotalWidth(final Map<Integer, Integer> maxLengths)
    {
        final int paddingSpace = maxLengths.size() * 2 * theme.getPadding().length();
        final int barSpace = (maxLengths.size() + 1) * theme.getVerticalSeparator().length();
        final int cellSpace = maxLengths.values().stream().reduce(0, Integer::sum);
        return cellSpace + paddingSpace + barSpace;
    }

    public String render(String title)
    {
        final String titleRow = title != null ? (theme.getCellBackground().value() + theme.getStringColor().value() + StringUtil.adjustPadRight(theme.getPadding() + title, tableWidth) + AnsiColor.RESET.value()) : "";
        return NEWLINE + titleRow + NEWLINE + theme.getCellBackground().value() + toString(rows);
    }

    private String toString(final List<TableRow> rows)
    {
        final StringBuilder sb = new StringBuilder();

        for (TableRow row : rows)
        {
            if (row instanceof SeparatorRow)
            {
                if (theme.getVerticalSeparator().length() != 0)
                {
                    sb.append(StringUtil.repeat(verticalSep(), tableWidth)).append(NEWLINE);
                }
            }
            else
            {
                sb.append(toString(row)).append(NEWLINE);
            }
        }

        return sb.toString();
    }


    private String toString(final TableRow row)
    {
        final StringBuilder sb = new StringBuilder();
        for (int colIndex = 0; colIndex < row.getCells().size(); colIndex++)
        {
            final Integer minWidth = minColumnWidths.get(colIndex);
            if (minWidth != null)
            {
                final TableCell cell = row.getCells().get(colIndex);
                sb.append(cell.render(theme, minWidth));
            }
        }
        return sb.append(horisontalSep()).toString();
    }

    private String horisontalSep()
    {
        return TableCell.color(theme.getHorizontalSeparator(), theme.getHorizontalSpacerColor(), theme.getCellBackground());
    }

    private String verticalSep()
    {
        return TableCell.color(theme.getVerticalSeparator(), theme.getVerticalSpacerColor(), theme.getCellBackground());
    }
}
