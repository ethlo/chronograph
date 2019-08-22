package com.ethlo.time;

import java.time.Duration;

public class CaptureConfig
{
    private final Duration sampleRate;

    private CaptureConfig(Builder builder)
    {
        this.sampleRate = builder.sampleRate;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private Duration sampleRate;

        private Builder()
        {
        }

        public Builder sampleRate(Duration sampleRate)
        {
            this.sampleRate = sampleRate;
            return this;
        }

        public CaptureConfig build()
        {
            return new CaptureConfig(this);
        }
    }
}
