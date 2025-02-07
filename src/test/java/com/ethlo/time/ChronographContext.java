package com.ethlo.time;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2025 Morten Haraldsen (ethlo)
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

import java.util.Optional;

import com.ethlo.Beta;
import com.ethlo.ascii.TableTheme;
import com.ethlo.ascii.TableThemes;

@Beta
public class ChronographContext
{
    private static final ThreadLocal<Chronograph> instances = ThreadLocal.withInitial(Chronograph::create);
    private static OutputConfig outputConfig;

    private static TableTheme theme;

    public static Chronograph get()
    {
        return instances.get();
    }

    public static void remove()
    {
        instances.remove();
    }

    public static String prettyPrint()
    {
        return get().prettyPrint(getConfig(), getTheme());
    }

    private static OutputConfig getConfig()
    {
        return Optional.ofNullable(outputConfig).orElse(OutputConfig.DEFAULT);
    }

    public static void setOutputConfig(final OutputConfig outputConfig)
    {
        ChronographContext.outputConfig = outputConfig;
    }

    public static TableTheme getTheme()
    {
        return Optional.ofNullable(theme).orElse(TableThemes.ASCII);
    }

    public static void setTheme(final TableTheme theme)
    {
        ChronographContext.theme = theme;
    }
}
