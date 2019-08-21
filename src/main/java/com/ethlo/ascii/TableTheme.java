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
    public static final TableTheme NONE = TableTheme.builder().build();

    public static final TableTheme STRONG = TableTheme.builder()
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .verticalSeparator("-")
            .horizontalSeparator("|")
            .verticalSpacerColor(AnsiColor.RED)
            .horizontalSpacerColor(AnsiColor.RED)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .build();

    public static final TableTheme BRIGHT = TableTheme.builder()
            .stringColor(AnsiColor.BRIGH_BLACK)
            .numericColor(AnsiColor.PURPLE)
            .verticalSeparator("-")
            .horizontalSeparator(" ")
            .verticalSpacerColor(AnsiColor.RED)
            .horizontalSpacerColor(AnsiColor.RED)
            .cellBackground(AnsiBackgroundColor.BRIGHT_WHITE)
            .build();

    public static final TableTheme SIMPLE = TableTheme.builder()
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .verticalSeparator("-")
            .horizontalSeparator(" ")
            .verticalSpacerColor(AnsiColor.RED)
            .horizontalSpacerColor(AnsiColor.RED)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .build();

    public static final TableTheme MINIMAL = TableTheme.builder()
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .verticalSeparator(" ")
            .horizontalSeparator(" ")
            .verticalSpacerColor(AnsiColor.GRAY)
            .horizontalSpacerColor(AnsiColor.GRAY)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .build();

    public static final TableTheme COMPACT = TableTheme.builder()
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .verticalSeparator("")
            .horizontalSeparator("")
            .verticalSpacerColor(AnsiColor.GRAY)
            .horizontalSpacerColor(AnsiColor.GRAY)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .padding(" ")
            .build();

    private final AnsiColor stringColor;
    private final AnsiColor numericColor;
    private final AnsiColor horizontalSpacerColor;
    private final AnsiColor verticalSpacerColor;
    private final AnsiBackgroundColor cellBackground;
    private final String horizontalSeparator;
    private final String verticalSeparator;
    private final String padding;

    private TableTheme(Builder builder)
    {
        this.stringColor = builder.stringColor;
        this.numericColor = builder.numericColor;
        this.horizontalSpacerColor = builder.horizontalSpacerColor;
        this.verticalSpacerColor = builder.verticalSpacerColor;
        this.cellBackground = builder.cellBackground;
        this.horizontalSeparator = builder.horizontalSeparator;
        this.verticalSeparator = builder.verticalSeparator;
        this.padding = builder.padding;
    }

    public AnsiColor getStringColor()
    {
        return stringColor;
    }

    public AnsiColor getNumericColor()
    {
        return numericColor;
    }

    public AnsiColor getHorizontalSpacerColor()
    {
        return horizontalSpacerColor;
    }

    public AnsiColor getVerticalSpacerColor()
    {
        return verticalSpacerColor;
    }

    public AnsiBackgroundColor getCellBackground()
    {
        return cellBackground;
    }

    public String getHorizontalSeparator()
    {
        return horizontalSeparator;
    }

    public String getVerticalSeparator()
    {
        return verticalSeparator;
    }

    public String getPadding()
    {
        return padding;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private AnsiColor stringColor = AnsiColor.NONE;
        private AnsiColor numericColor = AnsiColor.NONE;
        private AnsiColor horizontalSpacerColor = AnsiColor.NONE;
        private AnsiColor verticalSpacerColor = AnsiColor.NONE;
        private AnsiBackgroundColor cellBackground = AnsiBackgroundColor.NONE;
        private String horizontalSeparator = "|";
        private String verticalSeparator = "-";
        private String padding = " ";

        private Builder()
        {
        }

        public Builder stringColor(AnsiColor stringColor)
        {
            this.stringColor = stringColor;
            return this;
        }

        public Builder numericColor(AnsiColor numericColor)
        {
            this.numericColor = numericColor;
            return this;
        }

        public Builder horizontalSpacerColor(AnsiColor horizontalSpacerColor)
        {
            this.horizontalSpacerColor = horizontalSpacerColor;
            return this;
        }

        public Builder verticalSpacerColor(AnsiColor verticalSpacerColor)
        {
            this.verticalSpacerColor = verticalSpacerColor;
            return this;
        }

        public Builder cellBackground(AnsiBackgroundColor cellBackground)
        {
            this.cellBackground = cellBackground;
            return this;
        }

        public Builder horizontalSeparator(String horizontalSeparator)
        {
            this.horizontalSeparator = horizontalSeparator;
            return this;
        }

        public Builder verticalSeparator(String verticalSeparator)
        {
            this.verticalSeparator = verticalSeparator;
            return this;
        }

        public Builder padding(String padding)
        {
            this.padding = padding;
            return this;
        }

        public TableTheme build()
        {
            return new TableTheme(this);
        }
    }
}
