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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil
{
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    public static BigDecimal sqrt(BigDecimal value)
    {
        return sqrt(value, 10);
    }

    public static BigDecimal sqrt(BigDecimal value, final int SCALE)
    {
        if (value.compareTo(BigDecimal.ZERO) == 0)
        {
            return value;
        }
        
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = BigDecimal.valueOf(Math.sqrt(value.doubleValue()));
        while (!x0.equals(x1))
        {
            x0 = x1;
            x1 = value.divide(x0, SCALE, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, RoundingMode.HALF_UP);
        }
        return x1;
    }
}
