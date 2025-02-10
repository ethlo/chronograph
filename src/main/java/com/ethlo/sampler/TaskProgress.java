package com.ethlo.sampler;

import java.time.Duration;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2023 Morten Haraldsen (ethlo)
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
 *
 * @param lastProgress The progress on the last invocation
 * @param progress The current progress
 * @param duration The duration since the last update
 * @param <T> The progress data type
 */
public record TaskProgress<T>(T lastProgress, T progress, Duration duration)
{

}
