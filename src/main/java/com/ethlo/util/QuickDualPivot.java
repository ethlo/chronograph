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

public class QuickDualPivot
{
    public static void sort(LongList a)
    {
        sort(a, 0, a.size() - 1);
    }

    private static void sort(LongList a, int lo, int hi)
    {
        if (hi <= lo)
        {
            return;
        }

        if (a.get(hi) < a.get(lo))
        {
            swap(a, lo, hi);
        }

        int lt = lo + 1, gt = hi - 1;
        int i = lo + 1;
        while (i <= gt)
        {
            if (a.get(i) < a.get(lo))
            {
                swap(a, lt++, i++);
            }
            else if (a.get(hi) < a.get(i))
            {
                swap(a, i, gt--);
            }
            else
            {
                i++;
            }
        }
        swap(a, lo, --lt);
        swap(a, hi, ++gt);

        // recursively sort three subarrays
        sort(a, lo, lt - 1);
        if (a.get(lt) < a.get(gt))
        {
            sort(a, lt + 1, gt - 1);
        }
        sort(a, gt + 1, hi);
    }

    private static void swap(LongList a, int i, int j)
    {
        long swap = a.get(i);
        a.set(i, a.get(j));
        a.set(j, swap);
    }
}
