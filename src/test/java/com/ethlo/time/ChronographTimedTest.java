package com.ethlo.time;

/*-
 * #%L
 * Chronograph
 * %%
 * Copyright (C) 2019 - 2024 Morten Haraldsen (ethlo)
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChronographTimedTest
{
    private Chronograph chronograph;

    @BeforeEach
    void setUp()
    {
        chronograph = spy(Chronograph.create());
    }

    @Test
    void testTimedSupplier()
    {
        String taskName = "Supplier Task";
        Supplier<String> supplier = () -> "Hello, World!";

        String result = chronograph.time(taskName, supplier);

        assertThat(result).isEqualTo("Hello, World!");
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedFunction()
    {
        String taskName = "Function Task";
        Function<Integer, String> function = i -> "Result: " + i;

        String result = chronograph.time(taskName, function, 42);

        assertThat(result).isEqualTo("Result: 42");
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedBiFunction()
    {
        String taskName = "BiFunction Task";
        BiFunction<Integer, Integer, Integer> biFunction = Integer::sum;

        Integer result = chronograph.time(taskName, biFunction, 5, 10);

        assertThat(result).isEqualTo(15);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedPredicate()
    {
        String taskName = "Predicate Task";
        Predicate<String> predicate = s -> s.length() > 5;

        boolean result = chronograph.time(taskName, predicate, "HelloWorld");

        assertThat(result).isTrue();
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedConsumer()
    {
        String taskName = "Consumer Task";
        Consumer<String> consumer = mock(Consumer.class);

        chronograph.time(taskName, consumer, "Hello");

        verify(consumer).accept("Hello");
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoubleUnaryOperator()
    {
        String taskName = "DoubleUnaryOperator Task";
        DoubleUnaryOperator operator = d -> d * 2;

        double result = chronograph.time(taskName, operator, 3.5);

        assertThat(result).isEqualTo(7.0);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedIntBinaryOperator()
    {
        String taskName = "IntBinaryOperator Task";
        IntBinaryOperator operator = Integer::sum;

        int result = chronograph.time(taskName, operator, 2, 3);

        assertThat(result).isEqualTo(5);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedBiConsumer()
    {
        String taskName = "BiConsumer Task";
        BiConsumer<String, Integer> biConsumer = mock(BiConsumer.class);

        chronograph.time(taskName, biConsumer, "Test", 10);

        verify(biConsumer).accept("Test", 10);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedBinaryOperator()
    {
        String taskName = "BinaryOperator Task";
        BinaryOperator<Integer> operator = Integer::max;

        int result = chronograph.time(taskName, operator, 5, 10);

        assertThat(result).isEqualTo(10);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedBiPredicate()
    {
        String taskName = "BiPredicate Task";
        BiPredicate<String, String> biPredicate = String::equals;

        boolean result = chronograph.time(taskName, biPredicate, "test", "test");

        assertThat(result).isTrue();
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedBooleanSupplier()
    {
        String taskName = "BooleanSupplier Task";
        BooleanSupplier booleanSupplier = () -> true;

        boolean result = chronograph.time(taskName, booleanSupplier);

        assertThat(result).isTrue();
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoubleConsumer()
    {
        String taskName = "DoubleConsumer Task";
        DoubleConsumer doubleConsumer = mock(DoubleConsumer.class);

        chronograph.time(taskName, doubleConsumer, 5.5);

        verify(doubleConsumer).accept(5.5);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoubleFunction()
    {
        String taskName = "DoubleFunction Task";
        DoubleFunction<String> doubleFunction = d -> "Value: " + d;

        String result = chronograph.time(taskName, doubleFunction, 2.5);

        assertThat(result).isEqualTo("Value: 2.5");
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoublePredicate()
    {
        String taskName = "DoublePredicate Task";
        DoublePredicate predicate = d -> d > 10.0;

        boolean result = chronograph.time(taskName, predicate, 15.0);

        assertThat(result).isTrue();
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoubleToIntFunction()
    {
        String taskName = "DoubleToIntFunction Task";
        DoubleToIntFunction function = d -> (int) Math.floor(d);

        int result = chronograph.time(taskName, function, 3.8);

        assertThat(result).isEqualTo(3);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedDoubleToLongFunction()
    {
        String taskName = "DoubleToLongFunction Task";
        DoubleToLongFunction function = d -> (long) Math.ceil(d);

        long result = chronograph.time(taskName, function, 7.3);

        assertThat(result).isEqualTo(8L);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedIntUnaryOperator()
    {
        String taskName = "IntUnaryOperator Task";
        IntUnaryOperator operator = i -> i * i;

        int result = chronograph.time(taskName, operator, 5);

        assertThat(result).isEqualTo(25);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedLongUnaryOperator()
    {
        String taskName = "LongUnaryOperator Task";
        LongUnaryOperator operator = l -> l * 2;

        long result = chronograph.time(taskName, operator, 6L);

        assertThat(result).isEqualTo(12L);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedObjDoubleConsumer()
    {
        String taskName = "ObjDoubleConsumer Task";
        ObjDoubleConsumer<String> consumer = mock(ObjDoubleConsumer.class);

        chronograph.time(taskName, consumer, "Value", 3.14);

        verify(consumer).accept("Value", 3.14);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedObjIntConsumer()
    {
        String taskName = "ObjIntConsumer Task";
        ObjIntConsumer<String> consumer = mock(ObjIntConsumer.class);

        chronograph.time(taskName, consumer, "Value", 42);

        verify(consumer).accept("Value", 42);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedObjLongConsumer()
    {
        String taskName = "ObjLongConsumer Task";
        ObjLongConsumer<String> consumer = mock(ObjLongConsumer.class);

        chronograph.time(taskName, consumer, "Value", 100L);

        verify(consumer).accept("Value", 100L);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedToDoubleFunction()
    {
        String taskName = "ToDoubleFunction Task";
        ToDoubleFunction<String> function = Double::parseDouble;

        double result = chronograph.time(taskName, function, "3.14");

        assertThat(result).isEqualTo(3.14);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedToLongFunction()
    {
        String taskName = "ToLongFunction Task";
        ToLongFunction<String> function = Long::parseLong;

        long result = chronograph.time(taskName, function, "12345");

        assertThat(result).isEqualTo(12345L);
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

    @Test
    void testTimedUnaryOperator()
    {
        String taskName = "UnaryOperator Task";
        UnaryOperator<String> operator = String::toUpperCase;

        String result = chronograph.time(taskName, operator, "test");

        assertThat(result).isEqualTo("TEST");
        verify(chronograph).start(taskName);
        verify(chronograph).stop(taskName);
    }

}
