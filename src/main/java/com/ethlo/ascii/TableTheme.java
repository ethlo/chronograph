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

public class TableTheme
{
    public static final TableTheme NONE = new TableTheme();

    public static final TableTheme STRONG = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator("-")
            .setHorizontalSeparator("|")
            .setVerticalSpacerColor(AnsiColor.RED)
            .setHorizontalSpacerColor(AnsiColor.RED)
            .setCellBackground(AnsiBackgroundColor.BLACK);

    public static final TableTheme BRIGHT = new TableTheme()
            .setStringColor(AnsiColor.BRIGH_BLACK)
            .setNumericColor(AnsiColor.PURPLE)
            .setVerticalSeparator("-")
            .setHorizontalSeparator(" ")
            .setVerticalSpacerColor(AnsiColor.RED)
            .setHorizontalSpacerColor(AnsiColor.RED)
            .setCellBackground(AnsiBackgroundColor.BRIGHT_WHITE);

    public static final TableTheme SIMPLE = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator("-")
            .setHorizontalSeparator(" ")
            .setVerticalSpacerColor(AnsiColor.RED)
            .setHorizontalSpacerColor(AnsiColor.RED)
            .setCellBackground(AnsiBackgroundColor.BLACK);

    public static final TableTheme MINIMAL = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator(" ")
            .setHorizontalSeparator(" ")
            .setVerticalSpacerColor(AnsiColor.GRAY)
            .setHorizontalSpacerColor(AnsiColor.GRAY)
            .setCellBackground(AnsiBackgroundColor.BLACK);

    public static final TableTheme COMPACT = new TableTheme()
            .setStringColor(AnsiColor.GRAY)
            .setNumericColor(AnsiColor.GREEN)
            .setVerticalSeparator("")
            .setHorizontalSeparator("")
            .setVerticalSpacerColor(AnsiColor.GRAY)
            .setHorizontalSpacerColor(AnsiColor.GRAY)
            .setCellBackground(AnsiBackgroundColor.BLACK)
            .setPadding(" ");

    private AnsiColor stringColor = AnsiColor.NONE;
    private AnsiColor numericColor = AnsiColor.NONE;
    private AnsiColor horizontalSpacerColor = AnsiColor.NONE;
    private AnsiColor verticalSpacerColor = AnsiColor.NONE;
    private AnsiBackgroundColor cellBackground = AnsiBackgroundColor.NONE;
    private String horizontalSeparator = "|";
    private String verticalSeparator = "-";
    private String padding = " ";

    public String getPadding()
    {
        return padding;
    }

    public TableTheme setPadding(final String padding)
    {
        this.padding = padding;
        return this;
    }

    public String getHorizontalSeparator()
    {
        return horizontalSeparator;
    }

    public TableTheme setHorizontalSeparator(final String sep)
    {
        this.horizontalSeparator = sep;
        return this;
    }

    public AnsiColor getHorizontalSpacerColor()
    {
        return horizontalSpacerColor;
    }

    public TableTheme setHorizontalSpacerColor(final AnsiColor horizontalSpacerColor)
    {
        this.horizontalSpacerColor = horizontalSpacerColor;
        return this;
    }

    public AnsiColor getVerticalSpacerColor()
    {
        return verticalSpacerColor;
    }

    public TableTheme setVerticalSpacerColor(final AnsiColor verticalSpacerColor)
    {
        this.verticalSpacerColor = verticalSpacerColor;
        return this;
    }

    public AnsiColor getStringColor()
    {
        return stringColor;
    }

    public TableTheme setStringColor(final AnsiColor stringColor)
    {
        this.stringColor = stringColor;
        return this;
    }

    public AnsiColor getNumericColor()
    {
        return numericColor;
    }

    public TableTheme setNumericColor(final AnsiColor numericColor)
    {
        this.numericColor = numericColor;
        return this;
    }

    public String getVerticalSeparator()
    {
        return verticalSeparator;
    }

    public TableTheme setVerticalSeparator(final String sep)
    {
        this.verticalSeparator = sep;
        return this;
    }

    public AnsiBackgroundColor getCellBackground()
    {
        return cellBackground;
    }

    public TableTheme setCellBackground(final AnsiBackgroundColor cellBackground)
    {
        this.cellBackground = cellBackground;
        return this;
    }
}
