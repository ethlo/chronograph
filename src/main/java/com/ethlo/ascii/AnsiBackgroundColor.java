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

public enum AnsiBackgroundColor
{
    NONE(""),
    BLACK("\u001B[40m"),
    RED("\u001B[41m"),
    GREEN("\u001B[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    CYAN("\u001B[46m"),
    GRAY("\u001B[47m"),
    BRIGH_BLACK("\u001b[40;1m"),
    BRIGHT_RED("\u001b[41;1m"),
    BRIGHT_GREEN("\u001b[42;1m"),
    BRIGHT_YELLOW("\u001b[43;1m"),
    BRIGHT_BLUE("\u001b[44;1m"),
    BRIGHT_MAGENTA("\u001b[45;1m"),
    BRIGHT_CYAN("\u001b[46;1m"),
    BRIGHT_WHITE("\u001b[47;1m");

    private String s;

    AnsiBackgroundColor(String s)
    {
        this.s = s;
    }

    public String value()
    {
        return s;
    }
}
