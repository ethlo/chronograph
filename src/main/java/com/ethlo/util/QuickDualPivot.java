package com.ethlo.util;

public class QuickDualPivot
{
    public static void sort(LongList a)
    {
        sort(a, 0, a.size() - 1);
        if (!isSorted(a))
        {
            throw new IllegalStateException("Not sorted correctly");
        }
    }

    private static void sort(LongList a, int lo, int hi)
    {
        if (hi <= lo) return;

        // make sure a[lo] <= a[hi]
        if (less(a.get(hi), a.get(lo)))
        {
            exch(a, lo, hi);
        }

        int lt = lo + 1, gt = hi - 1;
        int i = lo + 1;
        while (i <= gt)
        {
            if (less(a.get(i), a.get(lo)))
            {
                exch(a, lt++, i++);
            }
            else if (less(a.get(hi), a.get(i)))
            {
                exch(a, i, gt--);
            }
            else
            {
                i++;
            }
        }
        exch(a, lo, --lt);
        exch(a, hi, ++gt);

        // recursively sort three subarrays
        sort(a, lo, lt - 1);
        if (less(a.get(lt), a.get(gt)))
        {
            sort(a, lt + 1, gt - 1);
        }
        sort(a, gt + 1, hi);
    }

    // is a < b ?
    private static boolean less(long a, Long b)
    {
        return a < b;
    }

    // exchange a[i] and a[j]
    private static void exch(LongList a, int i, int j)
    {
        long swap = a.get(i);
        a.set(i, a.get(j));
        a.set(j, swap);
    }

    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     **************************************************************************
     * @param a*/
    private static boolean isSorted(LongList a)
    {
        return isSorted(a, 0, a.size() - 1);
    }

    private static boolean isSorted(LongList a, int lo, int hi)
    {
        for (int i = lo + 1; i <= hi; i++)
        {
            if (less(a.get(i), a.get(i - 1)))
            {
                return false;
            }
        }
        return true;
    }

}