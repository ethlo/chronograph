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
 * Enum representing ANSI background color codes used for terminal text styling.
 * Each constant corresponds to a background color or a variation of a color (e.g., bright or regular).
 * The associated string is the ANSI escape code for the background color.
 */
public enum AnsiBackgroundColor
{
    /**
     * Represents no background color (reset).
     */
    NONE(""),

    /**
     * Represents the black background color.
     */
    BLACK("\u001B[40m"),

    /**
     * Represents the red background color.
     */
    RED("\u001B[41m"),

    /**
     * Represents the green background color.
     */
    GREEN("\u001B[42m"),

    /**
     * Represents the yellow background color.
     */
    YELLOW("\u001B[43m"),

    /**
     * Represents the blue background color.
     */
    BLUE("\u001B[44m"),

    /**
     * Represents the purple (magenta) background color.
     */
    PURPLE("\u001B[45m"),

    /**
     * Represents the cyan background color.
     */
    CYAN("\u001B[46m"),

    /**
     * Represents the gray background color.
     */
    GRAY("\u001B[47m"),

    /**
     * Represents the bright variation of the black background color.
     */
    BRIGHT_BLACK("\u001b[40;1m"),

    /**
     * Represents the bright variation of the red background color.
     */
    BRIGHT_RED("\u001b[41;1m"),

    /**
     * Represents the bright variation of the green background color.
     */
    BRIGHT_GREEN("\u001b[42;1m"),

    /**
     * Represents the bright variation of the yellow background color.
     */
    BRIGHT_YELLOW("\u001b[43;1m"),

    /**
     * Represents the bright variation of the blue background color.
     */
    BRIGHT_BLUE("\u001b[44;1m"),

    /**
     * Represents the bright variation of the magenta (purple) background color.
     */
    BRIGHT_MAGENTA("\u001b[45;1m"),

    /**
     * Represents the bright variation of the cyan background color.
     */
    BRIGHT_CYAN("\u001b[46;1m"),

    /**
     * Represents the bright variation of the white background color.
     */
    BRIGHT_WHITE("\u001b[47;1m");

    private String s;

    /**
     * Constructor for initializing the enum with an ANSI background color escape code.
     *
     * @param s the ANSI escape code string for the background color
     */
    AnsiBackgroundColor(String s)
    {
        this.s = s;
    }

    /**
     * Returns the ANSI escape code corresponding to the background color.
     *
     * @return the ANSI escape code string for the background color
     */
    public String value()
    {
        return s;
    }
}
