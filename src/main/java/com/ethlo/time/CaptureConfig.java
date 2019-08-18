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

public class CaptureConfig
{
    public static final CaptureConfig DEFAULT = CaptureConfig.builder().storeIndividual(false).build();
    public static final CaptureConfig EXTENDED = CaptureConfig.builder().storeIndividual(true).build();

    private final boolean storeIndividual;

    public CaptureConfig(final Builder builder)
    {
        this.storeIndividual = builder.storeIndividual;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public boolean storeIndividual()
    {
        return storeIndividual;
    }

    public static final class Builder
    {
        private boolean storeIndividual;

        private Builder()
        {
        }

        public CaptureConfig build()
        {
            return new CaptureConfig(this);
        }

        public Builder storeIndividual(final boolean storeIndividual)
        {
            this.storeIndividual = storeIndividual;
            return this;
        }
    }
}
