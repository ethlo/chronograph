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

import java.time.Duration;

/**
 * Represents the configuration for capturing samples, including the minimum interval
 * between samples (i.e., the sample rate).
 */
public class CaptureConfig
{
    /**
     * The default configuration, which has a minimum interval of zero.
     */
    public static final CaptureConfig DEFAULT = CaptureConfig.minInterval(Duration.ZERO);

    private final Duration sampleRate;

    /**
     * Private constructor for initializing the {@link CaptureConfig} from a {@link Builder}.
     *
     * @param builder the {@link Builder} used to create the {@link CaptureConfig}
     */
    private CaptureConfig(Builder builder)
    {
        this.sampleRate = builder.minInterval;
    }

    /**
     * Creates a {@link CaptureConfig} with the specified minimum interval.
     *
     * @param minInterval the minimum interval between samples
     * @return a new {@link CaptureConfig} instance with the provided minimum interval
     */
    public static CaptureConfig minInterval(final Duration minInterval)
    {
        return builder().minInterval(minInterval).build();
    }

    /**
     * Creates a new {@link Builder} instance to start building a {@link CaptureConfig}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Gets the minimum interval between samples.
     *
     * @return the minimum interval as a {@link Duration}
     */
    public Duration getMinInterval()
    {
        return sampleRate;
    }

    /**
     * A builder class used to construct {@link CaptureConfig} instances.
     */
    public static final class Builder
    {
        private Duration minInterval;

        private Builder()
        {
        }

        /**
         * Sets the minimum interval between samples.
         *
         * @param minInterval the minimum interval as a {@link Duration}
         * @return the current {@link Builder} instance
         */
        public Builder minInterval(Duration minInterval)
        {
            this.minInterval = minInterval;
            return this;
        }

        /**
         * Builds a {@link CaptureConfig} instance using the current builder state.
         *
         * @return a new {@link CaptureConfig} instance
         */
        public CaptureConfig build()
        {
            return new CaptureConfig(this);
        }
    }
}