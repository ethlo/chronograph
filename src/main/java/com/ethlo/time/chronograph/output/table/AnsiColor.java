package com.ethlo.time.chronograph.output.table;

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
 * Enum representing ANSI color codes used for terminal text styling.
 * Each constant corresponds to a color or a variation of a color (e.g., bright or regular).
 * The associated string is the ANSI escape code for the color.
 */
public enum AnsiColor
{
    /**
     * Represents no color (reset).
     */
    NONE(""),

    /**
     * Resets any previous color formatting.
     */
    RESET("\u001B[0m"),

    /**
     * Represents the color black.
     */
    BLACK("\u001B[30m"),

    /**
     * Represents the color red.
     */
    RED("\u001B[31m"),

    /**
     * Represents the color green.
     */
    GREEN("\u001B[32m"),

    /**
     * Represents the color yellow.
     */
    YELLOW("\u001B[33m"),

    /**
     * Represents the color blue.
     */
    BLUE("\u001B[34m"),

    /**
     * Represents the color purple (magenta).
     */
    PURPLE("\u001B[35m"),

    /**
     * Represents the color cyan.
     */
    CYAN("\u001B[36m"),

    /**
     * Represents the color gray.
     */
    GRAY("\u001B[37m"),

    /**
     * Represents the bright variation of the color black.
     */
    BRIGHT_BLACK("\u001b[30;1m"),

    /**
     * Represents the bright variation of the color red.
     */
    BRIGHT_RED("\u001b[31;1m"),

    /**
     * Represents the bright variation of the color green.
     */
    BRIGHT_GREEN("\u001b[32;1m"),

    /**
     * Represents the bright variation of the color yellow.
     */
    BRIGHT_YELLOW("\u001b[33;1m"),

    /**
     * Represents the bright variation of the color blue.
     */
    BRIGHT_BLUE("\u001b[34;1m"),

    /**
     * Represents the bright variation of the color magenta (purple).
     */
    BRIGHT_MAGENTA("\u001b[35;1m"),

    /**
     * Represents the bright variation of the color cyan.
     */
    BRIGHT_CYAN("\u001b[36;1m"),

    /**
     * Represents the bright variation of the color white.
     */
    BRIGHT_WHITE("\u001b[37;1m");

    private String s;

    /**
     * Constructor for initializing the enum with an ANSI escape code.
     *
     * @param s the ANSI escape code string for the color
     */
    AnsiColor(String s)
    {
        this.s = s;
    }

    /**
     * Returns the ANSI escape code corresponding to the color.
     *
     * @return the ANSI escape code string
     */
    public String value()
    {
        return s;
    }
}

