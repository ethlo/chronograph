package com.ethlo.chronograph.internal.util;

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

import java.util.stream.Stream;

public interface IndexedCollection<T> extends Iterable<T>
{
    void add(long T);

    T get(int index);

    int size();

    void set(int index, T value);

    void sort();

    Stream<T> stream();

    IndexedCollection<Long> addAll(Iterable<T> values);

    boolean isEmpty();
}
