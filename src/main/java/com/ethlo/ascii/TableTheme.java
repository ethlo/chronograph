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
    // Inspiration: https://www.compart.com/en/unicode/search?q=Box+Drawings#characters

    public static final TableTheme DEFAULT = TableTheme.builder().build();

    public static final TableTheme TSV = TableTheme.builder()
            .leftTop("")
            .rightTop("")
            .bottomCross("")
            .topCross("")
            .rightBottom("")
            .leftBottom("")
            .verticalSeparator("")
            .horizontalSeparator("\t")
            .build();

    public static final TableTheme DOUBLE = TableTheme.builder()
            .cross("╬")
            .rightCross("╣")
            .leftCross("╠")
            .topCross("╦")
            .bottomCross("╩")
            .leftTop("╔")
            .rightTop("╗")
            .leftBottom("╚")
            .rightBottom("╝")
            .verticalSeparator("═")
            .horizontalSeparator("║")
            .build();

    public static final TableTheme ROUNDED = DEFAULT.begin()
            .leftTop("╭")
            .rightTop("╮")
            .leftBottom("╰")
            .rightBottom("╯")
            .build();

    public static final TableTheme RED_HERRING = TableTheme.builder()
            .stringColor(AnsiColor.BRIGHT_WHITE)
            .numericColor(AnsiColor.GREEN)
            .verticalSpacerColor(AnsiColor.RED)
            .horizontalSpacerColor(AnsiColor.RED)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .build();

    public static final TableTheme MINIMAL = TableTheme.builder()
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .horizontalSeparator(" ")
            .verticalSpacerColor(AnsiColor.GRAY)
            .horizontalSpacerColor(AnsiColor.GRAY)
            .cellBackground(AnsiBackgroundColor.BLACK)
            .build();

    public static final TableTheme COMPACT = TableTheme.builder()
            .verticalSeparator("")
            .horizontalSeparator("")
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
    private final String cross;
    private final String leftCross;
    private final String rightCross;
    private final String leftTop;
    private final String rightTop;
    private final String leftBottom;
    private final String rightBottom;
    private final String topCross;
    private final String bottomCross;

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
        this.cross = builder.cross;
        this.topCross = builder.topCross;
        this.bottomCross = builder.bottomCross;
        this.leftCross = builder.leftCross;
        this.rightCross = builder.rightCross;
        this.leftTop = builder.leftTop;
        this.rightTop = builder.rightTop;
        this.leftBottom = builder.leftBottom;
        this.rightBottom = builder.rightBottom;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public Builder begin()
    {
        final Builder builder = new Builder();
        builder.stringColor = this.stringColor;
        builder.numericColor = this.numericColor;
        builder.horizontalSpacerColor = this.horizontalSpacerColor;
        builder.verticalSpacerColor = this.verticalSpacerColor;
        builder.cellBackground = this.cellBackground;
        builder.horizontalSeparator = this.horizontalSeparator;
        builder.verticalSeparator = this.verticalSeparator;
        builder.padding = this.padding;
        builder.cross = this.cross;
        builder.topCross = this.topCross;
        builder.bottomCross = this.bottomCross;
        builder.leftCross = this.leftCross;
        builder.rightCross = this.rightCross;
        builder.leftTop = this.leftTop;
        builder.rightTop = this.rightTop;
        builder.leftBottom = this.leftBottom;
        builder.rightBottom = this.rightBottom;
        return builder;
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

    public String getCross()
    {
        return cross;
    }

    public String getLeftCross()
    {
        return leftCross;
    }

    public String getRightCross()
    {
        return rightCross;
    }

    public String getTopCross()
    {
        return topCross;
    }

    public String getBottomCross()
    {
        return bottomCross;
    }

    public String getLeftTop()
    {
        return leftTop;
    }

    public String getRightTop()
    {
        return rightTop;
    }

    public String getLeftBottom()
    {
        return leftBottom;
    }

    public String getRightBottom()
    {
        return rightBottom;
    }

    public static final class Builder
    {
        public String topCross = "┬";
        public String bottomCross = "┴";
        private AnsiColor stringColor = AnsiColor.NONE;
        private AnsiColor numericColor = AnsiColor.NONE;
        private AnsiColor horizontalSpacerColor = AnsiColor.NONE;
        private AnsiColor verticalSpacerColor = AnsiColor.NONE;
        private AnsiBackgroundColor cellBackground = AnsiBackgroundColor.NONE;
        private String horizontalSeparator = "│";
        private String verticalSeparator = "─";
        private String padding = " ";
        private String cross = "┼";
        private String leftCross = "├";
        private String rightCross = "┤";
        private String leftTop = "┌";
        private String rightTop = "┐";
        private String leftBottom = "└";
        private String rightBottom = "┘";

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

        public Builder cross(String cross)
        {
            this.cross = cross;
            return this;
        }

        public Builder leftCross(String leftCross)
        {
            this.leftCross = leftCross;
            return this;
        }

        public Builder rightCross(String rightCross)
        {
            this.rightCross = rightCross;
            return this;
        }

        public Builder topCross(String topCross)
        {
            this.topCross = topCross;
            return this;
        }

        public Builder bottomCross(String bottomCross)
        {
            this.bottomCross = bottomCross;
            return this;
        }

        public Builder leftTop(final String leftTop)
        {
            this.leftTop = leftTop;
            return this;
        }

        public Builder rightTop(final String rightTop)
        {
            this.rightTop = rightTop;
            return this;
        }

        public Builder leftBottom(final String leftBottom)
        {
            this.leftBottom = leftBottom;
            return this;
        }

        public Builder rightBottom(final String rightBottom)
        {
            this.rightBottom = rightBottom;
            return this;
        }

        public TableTheme build()
        {
            return new TableTheme(this);
        }
    }
}
