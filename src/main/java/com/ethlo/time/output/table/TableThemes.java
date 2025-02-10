package com.ethlo.time.output.table;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2025 Morten Haraldsen (ethlo)
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
 * A collection of predefined {@link TableTheme} configurations for different table styles.
 * Each theme defines various visual elements such as separators, colors, and border characters
 * to customize the appearance of tables.
 */
public class TableThemes
{
    /**
     * ASCII table theme with basic characters for table borders.
     */
    public static final TableTheme ASCII = TableTheme.builder().name("ASCII").build();

    /**
     * Silver Steel theme with a mix of bright and colored separators and borders.
     */
    public static final TableTheme SILVER_STEEL = TableTheme.builder()
            .name("Silver Steel")
            .stringColor(AnsiColor.BRIGHT_WHITE)
            .numericColor(AnsiColor.CYAN)
            .verticalSpacerColor(AnsiColor.BLUE)
            .horizontalSpacerColor(AnsiColor.BLUE)
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

    /**
     * Ocean Breeze theme with a light and airy feel using cool blues and whites.
     */
    public static final TableTheme OCEAN_BREEZE = TableTheme.builder()
            .name("Ocean Breeze")
            .stringColor(AnsiColor.BRIGHT_CYAN)
            .numericColor(AnsiColor.BRIGHT_BLUE)
            .verticalSpacerColor(AnsiColor.BLUE)
            .horizontalSpacerColor(AnsiColor.BRIGHT_WHITE)
            .cross("╫")
            .rightCross("╢")
            .leftCross("╟")
            .topCross("╤")
            .bottomCross("╧")
            .leftTop("╒")
            .rightTop("╕")
            .leftBottom("╘")
            .rightBottom("╛")
            .verticalSeparator("─")
            .horizontalSeparator("│")
            .build();

    /**
     * Midnight Gold theme with dark tones and golden highlights.
     */
    public static final TableTheme MIDNIGHT_GOLD = TableTheme.builder()
            .name("Midnight Gold")
            .stringColor(AnsiColor.BRIGHT_YELLOW)
            .numericColor(AnsiColor.BRIGHT_CYAN)
            .verticalSpacerColor(AnsiColor.BLACK)
            .horizontalSpacerColor(AnsiColor.BLACK)
            .cross("╋")
            .rightCross("┨")
            .leftCross("┠")
            .topCross("┯")
            .bottomCross("┷")
            .leftTop("┏")
            .rightTop("┓")
            .leftBottom("┗")
            .rightBottom("┛")
            .verticalSeparator("━")
            .horizontalSeparator("┃")
            .build();

    /**
     * Royal Indigo theme featuring bright magenta and yellow with purple accents.
     */
    public static final TableTheme ROYAL_INDIGO = TableTheme.builder()
            .name("Royal Indigo")
            .stringColor(AnsiColor.BRIGHT_MAGENTA)
            .numericColor(AnsiColor.BRIGHT_YELLOW)
            .verticalSpacerColor(AnsiColor.PURPLE)
            .horizontalSpacerColor(AnsiColor.PURPLE)
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

    /**
     * Graphite Ember theme with dark and fiery tones using red and black colors.
     */
    public static final TableTheme GRAPHITE_EMBER = TableTheme.builder()
            .name("Graphite Ember")
            .stringColor(AnsiColor.BRIGHT_RED)
            .numericColor(AnsiColor.BRIGHT_WHITE)
            .verticalSpacerColor(AnsiColor.BRIGHT_BLACK)
            .horizontalSpacerColor(AnsiColor.BRIGHT_BLACK)
            .cross("┼")
            .rightCross("┤")
            .leftCross("├")
            .topCross("┬")
            .bottomCross("┴")
            .leftTop("┌")
            .rightTop("┐")
            .leftBottom("└")
            .rightBottom("┘")
            .verticalSeparator("─")
            .horizontalSeparator("│")
            .build();

    /**
     * Single-line border theme for a minimalist table design.
     */
    public static final TableTheme SINGLE = TableTheme.builder()
            .name("Single")
            .cross("┼")
            .rightCross("┤")
            .leftCross("├")
            .topCross("┬")
            .bottomCross("┴")
            .leftTop("┌")
            .rightTop("┐")
            .leftBottom("└")
            .rightBottom("┘")
            .verticalSeparator("─")
            .horizontalSeparator("│")
            .build();

    /**
     * Double-line border theme for a more intricate table design.
     */
    public static final TableTheme DOUBLE = TableTheme.builder()
            .name("Double")
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

    /**
     * Red Herring theme with contrasting red and green colors for a unique appearance.
     */
    public static final TableTheme RED_HERRING = TableTheme.builder()
            .name("Red Herring")
            .stringColor(AnsiColor.BRIGHT_WHITE)
            .numericColor(AnsiColor.GREEN)
            .verticalSpacerColor(AnsiColor.RED)
            .horizontalSpacerColor(AnsiColor.RED)
            .build();

    /**
     * Minimal theme with subtle coloring and simplified table separators.
     */
    public static final TableTheme MINIMAL = TableTheme.builder()
            .name("Minimal")
            .stringColor(AnsiColor.GRAY)
            .numericColor(AnsiColor.GREEN)
            .horizontalSeparator(" ")
            .verticalSpacerColor(AnsiColor.GRAY)
            .horizontalSpacerColor(AnsiColor.GRAY)
            .build();

    /**
     * Compact theme with no separators and minimal padding.
     */
    public static final TableTheme COMPACT = TableTheme.builder()
            .name("Compact")
            .verticalSeparator("")
            .horizontalSeparator("")
            .padding(" ")
            .build();

    /**
     * Rounded theme with curved corners for a smooth appearance.
     */
    public static final TableTheme ROUNDED = SINGLE.begin()
            .name("Rounded")
            .leftTop("╭")
            .rightTop("╮")
            .leftBottom("╰")
            .rightBottom("╯")
            .build();
}
