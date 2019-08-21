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
/*
Characters	Unicode	Name
─	U+2500	Box Drawings Light Horizontal
━	U+2501	Box Drawings Heavy Horizontal
│	U+2502	Box Drawings Light Vertical
┃	U+2503	Box Drawings Heavy Vertical
┄	U+2504	Box Drawings Light Triple Dash Horizontal
┅	U+2505	Box Drawings Heavy Triple Dash Horizontal
┆	U+2506	Box Drawings Light Triple Dash Vertical
┇	U+2507	Box Drawings Heavy Triple Dash Vertical
┈	U+2508	Box Drawings Light Quadruple Dash Horizontal
┉	U+2509	Box Drawings Heavy Quadruple Dash Horizontal
┊	U+250A	Box Drawings Light Quadruple Dash Vertical
┋	U+250B	Box Drawings Heavy Quadruple Dash Vertical
┌	U+250C	Box Drawings Light Down and Right
┍	U+250D	Box Drawings Down Light and Right Heavy
┎	U+250E	Box Drawings Down Heavy and Right Light
┏	U+250F	Box Drawings Heavy Down and Right
┐	U+2510	Box Drawings Light Down and Left
┑	U+2511	Box Drawings Down Light and Left Heavy
┒	U+2512	Box Drawings Down Heavy and Left Light
┓	U+2513	Box Drawings Heavy Down and Left
└	U+2514	Box Drawings Light Up and Right
┕	U+2515	Box Drawings Up Light and Right Heavy
┖	U+2516	Box Drawings Up Heavy and Right Light
┗	U+2517	Box Drawings Heavy Up and Right
┘	U+2518	Box Drawings Light Up and Left
┙	U+2519	Box Drawings Up Light and Left Heavy
┚	U+251A	Box Drawings Up Heavy and Left Light
┛	U+251B	Box Drawings Heavy Up and Left
├	U+251C	Box Drawings Light Vertical and Right
┝	U+251D	Box Drawings Vertical Light and Right Heavy
┞	U+251E	Box Drawings Up Heavy and Right Down Light
┟	U+251F	Box Drawings Down Heavy and Right Up Light
┠	U+2520	Box Drawings Vertical Heavy and Right Light
┡	U+2521	Box Drawings Down Light and Right Up Heavy
┢	U+2522	Box Drawings Up Light and Right Down Heavy
┣	U+2523	Box Drawings Heavy Vertical and Right
┤	U+2524	Box Drawings Light Vertical and Left
┥	U+2525	Box Drawings Vertical Light and Left Heavy
┦	U+2526	Box Drawings Up Heavy and Left Down Light
┧	U+2527	Box Drawings Down Heavy and Left Up Light
┨	U+2528	Box Drawings Vertical Heavy and Left Light
┩	U+2529	Box Drawings Down Light and Left Up Heavy
┪	U+252A	Box Drawings Up Light and Left Down Heavy
┫	U+252B	Box Drawings Heavy Vertical and Left
┬	U+252C	Box Drawings Light Down and Horizontal
┭	U+252D	Box Drawings Left Heavy and Right Down Light
┮	U+252E	Box Drawings Right Heavy and Left Down Light
┯	U+252F	Box Drawings Down Light and Horizontal Heavy
┰	U+2530	Box Drawings Down Heavy and Horizontal Light
┱	U+2531	Box Drawings Right Light and Left Down Heavy
┲	U+2532	Box Drawings Left Light and Right Down Heavy
┳	U+2533	Box Drawings Heavy Down and Horizontal
┴	U+2534	Box Drawings Light Up and Horizontal
┵	U+2535	Box Drawings Left Heavy and Right Up Light
┶	U+2536	Box Drawings Right Heavy and Left Up Light
┷	U+2537	Box Drawings Up Light and Horizontal Heavy
┸	U+2538	Box Drawings Up Heavy and Horizontal Light
┹	U+2539	Box Drawings Right Light and Left Up Heavy
┺	U+253A	Box Drawings Left Light and Right Up Heavy
┻	U+253B	Box Drawings Heavy Up and Horizontal
┼	U+253C	Box Drawings Light Vertical and Horizontal
┽	U+253D	Box Drawings Left Heavy and Right Vertical Light
┾	U+253E	Box Drawings Right Heavy and Left Vertical Light
┿	U+253F	Box Drawings Vertical Light and Horizontal Heavy
╀	U+2540	Box Drawings Up Heavy and Down Horizontal Light
╁	U+2541	Box Drawings Down Heavy and Up Horizontal Light
╂	U+2542	Box Drawings Vertical Heavy and Horizontal Light
╃	U+2543	Box Drawings Left Up Heavy and Right Down Light
╄	U+2544	Box Drawings Right Up Heavy and Left Down Light
╅	U+2545	Box Drawings Left Down Heavy and Right Up Light
╆	U+2546	Box Drawings Right Down Heavy and Left Up Light
╇	U+2547	Box Drawings Down Light and Up Horizontal Heavy
╈	U+2548	Box Drawings Up Light and Down Horizontal Heavy
╉	U+2549	Box Drawings Right Light and Left Vertical Heavy
╊	U+254A	Box Drawings Left Light and Right Vertical Heavy
╋	U+254B	Box Drawings Heavy Vertical and Horizontal
╌	U+254C	Box Drawings Light Double Dash Horizontal
╍	U+254D	Box Drawings Heavy Double Dash Horizontal
╎	U+254E	Box Drawings Light Double Dash Vertical
╏	U+254F	Box Drawings Heavy Double Dash Vertical
═	U+2550	Box Drawings Double Horizontal
║	U+2551	Box Drawings Double Vertical
╒	U+2552	Box Drawings Down Single and Right Double
╓	U+2553	Box Drawings Down Double and Right Single
╔	U+2554	Box Drawings Double Down and Right
╕	U+2555	Box Drawings Down Single and Left Double
╖	U+2556	Box Drawings Down Double and Left Single
╗	U+2557	Box Drawings Double Down and Left
╘	U+2558	Box Drawings Up Single and Right Double
╙	U+2559	Box Drawings Up Double and Right Single
╚	U+255A	Box Drawings Double Up and Right
╛	U+255B	Box Drawings Up Single and Left Double
╜	U+255C	Box Drawings Up Double and Left Single
╝	U+255D	Box Drawings Double Up and Left
╞	U+255E	Box Drawings Vertical Single and Right Double
╟	U+255F	Box Drawings Vertical Double and Right Single
╠	U+2560	Box Drawings Double Vertical and Right
╡	U+2561	Box Drawings Vertical Single and Left Double
╢	U+2562	Box Drawings Vertical Double and Left Single
╣	U+2563	Box Drawings Double Vertical and Left
╤	U+2564	Box Drawings Down Single and Horizontal Double
╥	U+2565	Box Drawings Down Double and Horizontal Single
╦	U+2566	Box Drawings Double Down and Horizontal
╧	U+2567	Box Drawings Up Single and Horizontal Double
╨	U+2568	Box Drawings Up Double and Horizontal Single
╩	U+2569	Box Drawings Double Up and Horizontal
╪	U+256A	Box Drawings Vertical Single and Horizontal Double
╫	U+256B	Box Drawings Vertical Double and Horizontal Single
╬	U+256C	Box Drawings Double Vertical and Horizontal
╭	U+256D	Box Drawings Light Arc Down and Right
╮	U+256E	Box Drawings Light Arc Down and Left
╯	U+256F	Box Drawings Light Arc Up and Left
╰	U+2570	Box Drawings Light Arc Up and Right
╱	U+2571	Box Drawings Light Diagonal Upper Right to Lower Left
╲	U+2572	Box Drawings Light Diagonal Upper Left to Lower Right
╳	U+2573	Box Drawings Light Diagonal Cross
╴	U+2574	Box Drawings Light Left
╵	U+2575	Box Drawings Light Up
╶	U+2576	Box Drawings Light Right
╷	U+2577	Box Drawings Light Down
╸	U+2578	Box Drawings Heavy Left
╹	U+2579	Box Drawings Heavy Up
╺	U+257A	Box Drawings Heavy Right
╻	U+257B	Box Drawings Heavy Down
╼	U+257C	Box Drawings Light Left and Heavy Right
╽	U+257D	Box Drawings Light Up and Heavy Down
╾	U+257E	Box Drawings Heavy Left and Light Right
╿	U+257F	Box Drawings Heavy Up and Light Down
 */
    public static final TableTheme DEFAULT = TableTheme.builder().build();

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

    public static final TableTheme RED_GREEN = TableTheme.builder()
            .stringColor(AnsiColor.BRIGHT_WHITE)
            .numericColor(AnsiColor.GREEN)
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
            .verticalSeparator("")
            .horizontalSeparator("")
            .padding(" ")
            .build();

    private final AnsiColor stringColor;
    private final AnsiColor numericColor;
    private final AnsiColor horizontalSpacerColor;
    private final AnsiColor verticalSpacerColor;
    private final AnsiBackgroundColor cellBackground;

    // Inspiration: https://www.compart.com/en/unicode/search?q=Box+Drawings#characters

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
        public String topCross = "┬";
        public String bottomCross = "┴";
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
