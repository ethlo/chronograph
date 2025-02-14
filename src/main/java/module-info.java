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
/**
 * Java 9+ module definition
 */
module com.ethlo.chronograph
{
    exports com.ethlo.chronograph;
    exports com.ethlo.chronograph.output;
    exports com.ethlo.chronograph.output.table;
    exports com.ethlo.chronograph.output.json;
    exports com.ethlo.chronograph.statistics;
    exports com.ethlo.chronograph.context;
}
