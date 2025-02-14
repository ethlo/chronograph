package com.ethlo.chronograph.output.table;

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

/**
 * Defines a theme for rendering tables with customizable colors and border styles.
 */
public class TableTheme
{
    // Inspiration: https://www.compart.com/en/unicode/search?q=Box+Drawings#characters

    /**
     * Color used for string values.
     */
    private final AnsiColor stringColor;
    /**
     * Color used for numeric values.
     */
    private final AnsiColor numericColor;
    /**
     * Color used for horizontal spacers.
     */
    private final AnsiColor horizontalSpacerColor;
    /**
     * Color used for vertical spacers.
     */
    private final AnsiColor verticalSpacerColor;
    /**
     * Background color of table cells.
     */
    private final AnsiBackgroundColor cellBackground;

    /**
     * Horizontal separator character.
     */
    private final String horizontalSeparator;
    /**
     * Vertical separator character.
     */
    private final String verticalSeparator;
    /**
     * Padding character.
     */
    private final String padding;
    /**
     * Cross intersection character.
     */
    private final String cross;
    /**
     * Left cross character.
     */
    private final String leftCross;
    /**
     * Right cross character.
     */
    private final String rightCross;
    /**
     * Top-left corner character.
     */
    private final String leftTop;
    /**
     * Top-right corner character.
     */
    private final String rightTop;
    /**
     * Bottom-left corner character.
     */
    private final String leftBottom;
    /**
     * Bottom-right corner character.
     */
    private final String rightBottom;
    /**
     * Top cross character.
     */
    private final String topCross;
    /**
     * Bottom cross character.
     */
    private final String bottomCross;
    /**
     * Name of the theme.
     */
    private final String name;

    /**
     * Constructs a {@code TableTheme} instance using the provided builder.
     *
     * @param builder the builder containing the theme configurations
     */
    private TableTheme(Builder builder)
    {
        this.name = builder.name;
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

    /**
     * Creates a new {@code Builder} instance for constructing a TableTheme.
     *
     * @return a new Builder instance
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Creates a builder initialized with the current theme's values.
     *
     * @return a new Builder instance with current values
     */
    public Builder begin()
    {
        final Builder builder = new Builder();
        builder.name = this.name;
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

    /**
     * The color to use for the strings
     *
     * @return The color to use for the string
     */
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

    public String getName()
    {
        return name;
    }

    public boolean hasColors()
    {
        return cellBackground != AnsiBackgroundColor.NONE || stringColor != AnsiColor.NONE || numericColor != AnsiColor.NONE;
    }

    /**
     * Builder class for constructing a {@code TableTheme} instance.
     */
    public static final class Builder
    {
        private String topCross = "+";
        private String bottomCross = "+";
        private AnsiColor stringColor = AnsiColor.NONE;
        private AnsiColor numericColor = AnsiColor.NONE;
        private AnsiColor horizontalSpacerColor = AnsiColor.NONE;
        private AnsiColor verticalSpacerColor = AnsiColor.NONE;
        private AnsiBackgroundColor cellBackground = AnsiBackgroundColor.NONE;
        private String horizontalSeparator = "|";
        private String verticalSeparator = "-";
        private String padding = " ";
        private String cross = "+";
        private String leftCross = "+";
        private String rightCross = "+";
        private String leftTop = "+";
        private String rightTop = "+";
        private String leftBottom = "+";
        private String rightBottom = "+";
        private String name;

        private Builder()
        {
        }

        /**
         * Set the parameter and return the instance
         *
         * @param stringColor The color of strings
         * @return the current instance for chaining
         */
        public Builder stringColor(AnsiColor stringColor)
        {
            this.stringColor = stringColor;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param numericColor The color of numbers
         * @return the current instance for chaining
         */
        public Builder numericColor(AnsiColor numericColor)
        {
            this.numericColor = numericColor;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param horizontalSpacerColor the color of horizontal spacers
         * @return the current instance for chaining
         */
        public Builder horizontalSpacerColor(AnsiColor horizontalSpacerColor)
        {
            this.horizontalSpacerColor = horizontalSpacerColor;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param verticalSpacerColor The color of vertical spacers
         * @return the current instance for chaining
         */
        public Builder verticalSpacerColor(AnsiColor verticalSpacerColor)
        {
            this.verticalSpacerColor = verticalSpacerColor;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param cellBackground The cell background color
         * @return the current instance for chaining
         */
        public Builder cellBackground(AnsiBackgroundColor cellBackground)
        {
            this.cellBackground = cellBackground;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param horizontalSeparator The glyph for the horizontal separator
         * @return the current instance for chaining
         */
        public Builder horizontalSeparator(String horizontalSeparator)
        {
            this.horizontalSeparator = horizontalSeparator;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param verticalSeparator The glyph for the vertical separator
         * @return the current instance for chaining
         */
        public Builder verticalSeparator(String verticalSeparator)
        {
            this.verticalSeparator = verticalSeparator;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param padding The padding surrounding content in the table
         * @return the current instance for chaining
         */
        public Builder padding(String padding)
        {
            this.padding = padding;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param cross The glyph for the intersections of horizontal and vertical intersections
         * @return the current instance for chaining
         */
        public Builder cross(String cross)
        {
            this.cross = cross;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param leftCross The glyph for the left intersections of horizontal and vertical intersections
         * @return the current instance for chaining
         */
        public Builder leftCross(String leftCross)
        {
            this.leftCross = leftCross;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param rightCross The glyph for the right intersections of horizontal and vertical intersections
         * @return the current instance for chaining
         */
        public Builder rightCross(String rightCross)
        {
            this.rightCross = rightCross;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param topCross The glyph for the top intersections of horizontal and vertical intersections
         * @return the current instance for chaining
         */
        public Builder topCross(String topCross)
        {
            this.topCross = topCross;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param bottomCross The glyph for the bottom intersections of horizontal and vertical intersections
         * @return the current instance for chaining
         */
        public Builder bottomCross(String bottomCross)
        {
            this.bottomCross = bottomCross;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param leftTop The glyph for the left top corner
         * @return the current instance for chaining
         */
        public Builder leftTop(final String leftTop)
        {
            this.leftTop = leftTop;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param rightTop The glyph for the right top corner
         * @return the current instance for chaining
         */
        public Builder rightTop(final String rightTop)
        {
            this.rightTop = rightTop;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param leftBottom The glyph for the left bottom corner
         * @return the current instance for chaining
         */
        public Builder leftBottom(final String leftBottom)
        {
            this.leftBottom = leftBottom;
            return this;
        }

        /**
         * Set the parameter and return the instance
         *
         * @param rightBottom The glyph for the right bottom corner
         * @return the current instance for chaining
         */
        public Builder rightBottom(final String rightBottom)
        {
            this.rightBottom = rightBottom;
            return this;
        }

        /**
         * Create a @{@link TableTheme} instance from this builder
         *
         * @return The new theme instance
         */
        public TableTheme build()
        {
            return new TableTheme(this);
        }

        /**
         * Set the name of this theme
         *
         * @param name the name of the theme
         * @return This instance for chaining
         */
        public Builder name(String name)
        {
            this.name = name;
            return this;
        }
    }
}
