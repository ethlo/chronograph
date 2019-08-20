package com.ethlo.util;

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

import java.util.Arrays;

public class StringUtil
{
    public static String repeat(final String s, final int count)
    {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String adjustPadRight(final String s, final int width)
    {
        if (s.length() >= width)
        {
            return s.substring(0, width);
        }

        final char[] result = Arrays.copyOf(s.toCharArray(), width);
        for (int i = s.length(); i < width; i++)
        {
            result[i] = ' ';
        }
        return new String(result);
    }

    public static String adjustPadLeft(final String s, final int width)
    {
        if (s.length() >= width)
        {
            return s.substring(0, width);
        }

        final char[] result = new char[width];
        Arrays.fill(result, ' ');
        for (int i = 0; i < s.length(); i++)
        {
            result[i + (width - s.length())] = s.charAt(i);
        }
        return new String(result);
    }

    public static String stripAll(final String value, final String... remove)
    {
        String result = value;
        for (String r : remove)
        {
            result = result.replace(r, "");
        }
        return result;
    }
}
