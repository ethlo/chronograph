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

/**
 * Configuration for output settings related to performance statistics.
 * Provides options to customize which statistics are included in the output.
 * The available statistics include average, median, minimum, maximum,
 * standard deviation, total, invocations, percentage, and percentiles.
 *
 * <p>This class also allows setting a custom name for overhead and a threshold
 * value for overhead filtering.</p>
 */
public class OutputConfig
{

    /**
     * A compact configuration including only median, invocations, standard deviation, total, and percentage.
     */
    public static final OutputConfig COMPACT = new OutputConfig()
            .median(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

    /**
     * The default configuration, including average, median, min, max, invocations,
     * standard deviation, total, and percentage.
     */
    public static final OutputConfig DEFAULT = new OutputConfig()
            .average(true)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

    /**
     * An extended configuration, adding percentiles (95, 99, and 99.9) along with the default statistics.
     */
    public static final OutputConfig EXTENDED = new OutputConfig()
            .average(true)
            .percentiles(95D, 99D, 99.9D)
            .median(true)
            .min(true)
            .max(true)
            .invocations(true)
            .standardDeviation(true)
            .total(true)
            .percentage(true);

    private double[] percentiles;
    private boolean median;
    private boolean average;
    private boolean min;
    private boolean max;
    private boolean invocations;
    private boolean standardDeviation;
    private boolean total;
    private boolean percentage;
    private String overheadName = "<unknown>";
    private double overheadThreshold = 0.05;

    /**
     * Default constructor for creating an empty configuration.
     */
    public OutputConfig()
    {
    }

    /**
     * Constructor that initializes the configuration with values from the provided builder.
     *
     * @param builder the builder containing the configuration values
     */
    private OutputConfig(Builder builder)
    {
        this.percentiles = builder.percentiles;
        this.median = builder.median;
        this.average = builder.average;
        this.min = builder.min;
        this.max = builder.max;
        this.invocations = builder.invocations;
        this.standardDeviation = builder.standardDeviation;
        this.total = builder.total;
        this.percentage = builder.percentage;
        this.overheadName = builder.overheadName;
        this.overheadThreshold = builder.overheadThreshold;
    }

    /**
     * Returns the percentiles for the output.
     *
     * @return an array of percentiles
     */
    public double[] percentiles()
    {
        return percentiles;
    }

    /**
     * Returns whether the median should be included in the output.
     *
     * @return {@code true} if median is included, {@code false} otherwise
     */
    public boolean median()
    {
        return median;
    }

    /**
     * Returns whether the average should be included in the output.
     *
     * @return {@code true} if average is included, {@code false} otherwise
     */
    public boolean average()
    {
        return average;
    }

    /**
     * Returns whether the minimum value should be included in the output.
     *
     * @return {@code true} if minimum is included, {@code false} otherwise
     */
    public boolean min()
    {
        return min;
    }

    /**
     * Returns whether the maximum value should be included in the output.
     *
     * @return {@code true} if maximum is included, {@code false} otherwise
     */
    public boolean max()
    {
        return max;
    }

    /**
     * Returns whether the number of invocations should be included in the output.
     *
     * @return {@code true} if invocations are included, {@code false} otherwise
     */
    public boolean invocations()
    {
        return invocations;
    }

    /**
     * Returns whether the standard deviation should be included in the output.
     *
     * @return {@code true} if standard deviation is included, {@code false} otherwise
     */
    public boolean standardDeviation()
    {
        return standardDeviation;
    }

    /**
     * Returns whether the total time should be included in the output.
     *
     * @return {@code true} if total time is included, {@code false} otherwise
     */
    public boolean total()
    {
        return total;
    }

    /**
     * Returns whether the percentage should be included in the output.
     *
     * @return {@code true} if percentage is included, {@code false} otherwise
     */
    public boolean percentage()
    {
        return percentage;
    }

    /**
     * Sets the percentiles for the output.
     *
     * @param percentiles the percentiles to include in the output
     * @return a new {@link OutputConfig} instance with the updated percentiles
     */
    public OutputConfig percentiles(final double... percentiles)
    {
        return new OutputConfig(new Builder(this).percentiles(percentiles));
    }

    /**
     * Sets whether the median should be included in the output.
     *
     * @param median {@code true} if median should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated median setting
     */
    public OutputConfig median(final boolean median)
    {
        return new OutputConfig(new Builder(this).median(median));
    }

    /**
     * Sets whether the average should be included in the output.
     *
     * @param average {@code true} if average should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated average setting
     */
    public OutputConfig average(final boolean average)
    {
        return new OutputConfig(new Builder(this).average(average));
    }

    /**
     * Sets whether the minimum value should be included in the output.
     *
     * @param min {@code true} if minimum value should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated minimum setting
     */
    public OutputConfig min(final boolean min)
    {
        return new OutputConfig(new Builder(this).min(min));
    }

    /**
     * Sets whether the maximum value should be included in the output.
     *
     * @param max {@code true} if maximum value should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated maximum setting
     */
    public OutputConfig max(final boolean max)
    {
        return new OutputConfig(new Builder(this).max(max));
    }

    /**
     * Sets whether the number of invocations should be included in the output.
     *
     * @param invocations {@code true} if invocations should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated invocations setting
     */
    public OutputConfig invocations(final boolean invocations)
    {
        return new OutputConfig(new Builder(this).invocations(invocations));
    }

    /**
     * Sets whether the standard deviation should be included in the output.
     *
     * @param standardDeviation {@code true} if standard deviation should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated standard deviation setting
     */
    public OutputConfig standardDeviation(final boolean standardDeviation)
    {
        return new OutputConfig(new Builder(this).standardDeviation(standardDeviation));
    }

    /**
     * Sets whether the total time should be included in the output.
     *
     * @param total {@code true} if total time should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated total setting
     */
    public OutputConfig total(final boolean total)
    {
        return new OutputConfig(new Builder(this).total(total));
    }

    /**
     * Sets whether the percentage should be included in the output.
     *
     * @param percentage {@code true} if percentage should be included, {@code false} otherwise
     * @return a new {@link OutputConfig} instance with the updated percentage setting
     */
    public OutputConfig percentage(final boolean percentage)
    {
        return new OutputConfig(new Builder(this).percentage(percentage));
    }

    /**
     * Gets the name of the overhead.
     *
     * @return the overhead name
     */
    public String overheadName()
    {
        return overheadName;
    }

    /**
     * Sets the name of the overhead.
     *
     * @param overheadName the overhead name
     * @return a new {@link OutputConfig} instance with the updated overhead name
     */
    public OutputConfig overheadName(String overheadName)
    {
        return new OutputConfig(new Builder(this).overheadName(overheadName));
    }

    /**
     * Sets the threshold for overhead filtering.
     *
     * @param threshold the overhead threshold, must be between 0 and 1
     * @return a new {@link OutputConfig} instance with the updated overhead threshold
     * @throws IllegalArgumentException if the threshold is outside the range (0, 1)
     */
    public OutputConfig overheadThreshold(double threshold)
    {
        if (threshold <= 0)
        {
            throw new IllegalArgumentException("Number is too small. Must be between 0 and 1");
        }
        if (threshold >= 1)
        {
            throw new IllegalArgumentException("Number is too large. Must be between 0 and 1");
        }

        return new OutputConfig(new Builder(this).overheadThreshold(threshold));
    }

    /**
     * Gets the overhead threshold.
     *
     * @return the overhead threshold
     */
    public double overheadThreshold()
    {
        return overheadThreshold;
    }

    /**
     * Builder class for constructing {@link OutputConfig} instances.
     */
    public static class Builder
    {

        private double[] percentiles;
        private boolean median;
        private boolean average;
        private boolean min;
        private boolean max;
        private boolean invocations;
        private boolean standardDeviation;
        private boolean total;
        private boolean percentage;
        private String overheadName;
        private double overheadThreshold;

        /**
         * Initializes the builder with an existing {@link OutputConfig} instance.
         *
         * @param config the existing configuration
         */
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
            this.overheadName = config.overheadName;
            this.overheadThreshold = config.overheadThreshold;
        }

        /**
         * Sets the percentiles for the output.
         *
         * @param percentiles the percentiles to include
         * @return this builder instance
         */
        public Builder percentiles(final double... percentiles)
        {
            this.percentiles = percentiles;
            return this;
        }

        /**
         * Sets whether the median should be included in the output.
         *
         * @param median {@code true} if median should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder median(final boolean median)
        {
            this.median = median;
            return this;
        }

        /**
         * Sets whether the average should be included in the output.
         *
         * @param average {@code true} if average should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder average(final boolean average)
        {
            this.average = average;
            return this;
        }

        /**
         * Sets whether the minimum value should be included in the output.
         *
         * @param min {@code true} if minimum should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder min(final boolean min)
        {
            this.min = min;
            return this;
        }

        /**
         * Sets whether the maximum value should be included in the output.
         *
         * @param max {@code true} if maximum should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder max(final boolean max)
        {
            this.max = max;
            return this;
        }

        /**
         * Sets whether the number of invocations should be included in the output.
         *
         * @param invocations {@code true} if invocations should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder invocations(final boolean invocations)
        {
            this.invocations = invocations;
            return this;
        }

        /**
         * Sets whether the standard deviation should be included in the output.
         *
         * @param standardDeviation {@code true} if standard deviation should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder standardDeviation(final boolean standardDeviation)
        {
            this.standardDeviation = standardDeviation;
            return this;
        }

        /**
         * Sets whether the total time should be included in the output.
         *
         * @param total {@code true} if total time should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder total(final boolean total)
        {
            this.total = total;
            return this;
        }

        /**
         * Sets whether the percentage should be included in the output.
         *
         * @param percentage {@code true} if percentage should be included, {@code false} otherwise
         * @return this builder instance
         */
        public Builder percentage(final boolean percentage)
        {
            this.percentage = percentage;
            return this;
        }

        /**
         * Sets the name of the overhead.
         *
         * @param overheadName the overhead name
         * @return this builder instance
         */
        public Builder overheadName(final String overheadName)
        {
            this.overheadName = overheadName;
            return this;
        }

        /**
         * Sets the overhead threshold.
         *
         * @param threshold the overhead threshold
         * @return this builder instance
         */
        public Builder overheadThreshold(double threshold)
        {
            this.overheadThreshold = threshold;
            return this;
        }
    }
}