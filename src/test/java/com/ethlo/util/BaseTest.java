package com.ethlo.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ethlo.chronograph.Chronograph;
import com.ethlo.chronograph.ChronographUtil;
import com.ethlo.chronograph.OutputConfig;
import com.ethlo.chronograph.output.table.TableOutputFormatter;
import com.ethlo.chronograph.output.table.TableTheme;
import com.ethlo.chronograph.output.table.TableThemes;

public class BaseTest
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void output(Chronograph c, OutputConfig outputConfig)
    {
        final String result = new TableOutputFormatter(TableThemes.ASCII, outputConfig).format(c.getTaskData());
        logger.info(result);
    }

    protected void output(Chronograph c, OutputConfig outputConfig, TableTheme tableTheme)
    {
        final String result = new TableOutputFormatter(tableTheme, outputConfig).format(c.getTaskData());
        logger.info(result);
    }

    protected void output(final Chronograph c)
    {
        logger.info(ChronographUtil.table(c));
        logger.info(ChronographUtil.json(c));
    }

    protected void output(final Chronograph c, TableTheme tableTheme)
    {
        final String result = new TableOutputFormatter(tableTheme, OutputConfig.DEFAULT).format(c.getTaskData());
        logger.info(result);
    }
}
