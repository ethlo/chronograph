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
    public static final OutputConfig COMPACT = new OutputConfig()
            .median(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

    public static final OutputConfig DEFAULT = new OutputConfig()
            .average(true)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

    public static final OutputConfig EXTENDED = new OutputConfig()
            .average(true)
            .percentiles(95D, 99, 99.9D)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

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
    private boolean benchmarkMode = false;

    public OutputConfig()
    {

    }

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
        this.benchmarkMode = builder.benchmarkMode;

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

    public boolean benchmarkMode()
    {
        return benchmarkMode;
    }

    public OutputConfig title(final String title)
    {
        return new OutputConfig(new Builder(this).title(title));
    }

    public OutputConfig percentiles(final double... percentiles)
    {
        return new OutputConfig(new Builder(this).percentiles(percentiles));
    }

    public OutputConfig median(final boolean median)
    {
        return new OutputConfig(new Builder(this).median(median));
    }

    public OutputConfig average(final boolean average)
    {
        return new OutputConfig(new Builder(this).average(average));
    }

    public OutputConfig min(final boolean min)
    {
        return new OutputConfig(new Builder(this).min(min));
    }

    public OutputConfig max(final boolean max)
    {
        return new OutputConfig(new Builder(this).max(max));
    }

    public OutputConfig invocations(final boolean invocations)
    {
        return new OutputConfig(new Builder(this).invocations(invocations));
    }

    public OutputConfig standardDeviation(final boolean standardDeviation)
    {
        return new OutputConfig(new Builder(this).standardDeviation(standardDeviation));
    }

    public OutputConfig total(final boolean total)
    {
        return new OutputConfig(new Builder(this).total(total));
    }

    public OutputConfig percentage(final boolean percentage)
    {
        return new OutputConfig(new Builder(this).percentage(percentage));
    }

    public OutputConfig benchmarkMode(final boolean b)
    {
        return new OutputConfig(new Builder(this).benchmarkMode(b));
    }

    public static class Builder
    {
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

        private Builder(OutputConfig config)
        {
            this.total = config.total;
            this.percentage = config.percentage;
            this.standardDeviation = config.standardDeviation;
            this.min = config.min;
            this.max = config.max;
            this.median = config.median;
            this.percentiles = config.percentiles;
            this.invocations = config.invocations;
            this.average = config.average;
            this.title = config.title;
            this.benchmarkMode = config.benchmarkMode;
        }

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

        public Builder benchmarkMode(final boolean b)
        {
            this.benchmarkMode = b;
            return this;
        }
    }
}
