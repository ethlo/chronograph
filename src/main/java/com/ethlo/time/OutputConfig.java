package com.ethlo.time;

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

public class OutputConfig
{
    public static final OutputConfig COMPACT = OutputConfig.builder()
            .median(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true)
            .build();

    public static final OutputConfig DEFAULT = OutputConfig.builder()
            .average(true)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true)
            .build();

    public static final OutputConfig EXTENDED = OutputConfig.builder()
            .average(true)
            .percentiles(95D, 99, 99.9D)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true)
            .build();

    private final String title;
    private final double[] percentiles;
    private final boolean median;
    private final boolean average;
    private final boolean min;
    private final boolean max;
    private final boolean invocations;
    private final boolean standardDeviation;
    private final boolean total;
    private final boolean percentage;
    private final Mode mode;
    private boolean benchmarkMode;

    private OutputConfig(Builder builder)
    {
        this.title = builder.title;
        this.percentiles = builder.percentiles;
        this.median = builder.median;
        this.average = builder.average;
        this.min = builder.min;
        this.max = builder.max;
        this.invocations = builder.invocations;
        this.standardDeviation = builder.standardDeviation;
        this.total = builder.total;
        this.percentage = builder.percentage;
        this.mode = builder.mode;
        this.benchmarkMode = builder.benchmarkMode;

    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String title()
    {
        return title;
    }

    public double[] percentiles()
    {
        return percentiles;
    }

    public boolean median()
    {
        return median;
    }

    public boolean mean()
    {
        return average;
    }

    public boolean min()
    {
        return min;
    }

    public boolean max()
    {
        return max;
    }

    public boolean invocations()
    {
        return invocations;
    }

    public boolean standardDeviation()
    {
        return standardDeviation;
    }

    public boolean total()
    {
        return total;
    }

    public boolean percentage()
    {
        return percentage;
    }

    public Mode getMode()
    {
        return mode;
    }

    public boolean benchmarkMode()
    {
        return benchmarkMode;
    }

    public Builder begin()
    {
        final Builder b = new Builder();
        b.total = total;
        b.percentage = percentage;
        b.standardDeviation = standardDeviation;
        b.min = min;
        b.max = max;
        b.median = median;
        b.percentiles = percentiles;
        b.invocations = invocations;
        b.average = average;
        b.title = title;
        b.mode = mode;
        b.benchmarkMode = benchmarkMode;
        return b;
    }

    public static class Builder
    {
        private Mode mode = Mode.THROUGHPUT;
        private String title;
        private double[] percentiles;
        private boolean median;
        private boolean average;
        private boolean min;
        private boolean max;
        private boolean invocations;
        private boolean standardDeviation;
        private boolean total;
        private boolean percentage;
        private boolean benchmarkMode;

        public Builder title(final String title)
        {
            this.title = title;
            return this;
        }

        public Builder percentiles(final double... percentiles)
        {
            this.percentiles = percentiles;
            return this;
        }

        public Builder median(final boolean median)
        {
            this.median = median;
            return this;
        }

        public Builder average(final boolean average)
        {
            this.average = average;
            return this;
        }

        public Builder min(final boolean min)
        {
            this.min = min;
            return this;
        }

        public Builder max(final boolean max)
        {
            this.max = max;
            return this;
        }

        public Builder invocations(final boolean invocations)
        {
            this.invocations = invocations;
            return this;
        }

        public Builder standardDeviation(final boolean standardDeviation)
        {
            this.standardDeviation = standardDeviation;
            return this;
        }

        public Builder total(final boolean total)
        {
            this.total = total;
            return this;
        }

        public Builder percentage(final boolean percentage)
        {
            this.percentage = percentage;
            return this;
        }

        public Builder mode(final Mode mode)
        {
            this.mode = mode;
            return this;
        }

        public OutputConfig build()
        {
            return new OutputConfig(this);
        }

        public Builder benchmarkMode(final boolean b)
        {
            this.benchmarkMode = b;
            return this;
        }
    }
}
