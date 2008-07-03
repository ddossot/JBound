package org.jbound.exercise;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author David Dossot (david@dossot.net)
 */
final class Data {
    private static final Map<Class<?>, Object> TEST_DATA =
            new HashMap<Class<?>, Object>();

    static {
        TEST_DATA.put(Object.class, new Object[] { null });

        TEST_DATA.put(boolean.class, new boolean[] { true, false });

        TEST_DATA.put(byte.class, new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE });

        TEST_DATA.put(char.class, new char[] { Character.MIN_VALUE,
                Character.MAX_VALUE });

        TEST_DATA.put(double.class, new double[] { Double.MIN_VALUE,
                Double.MAX_VALUE });

        TEST_DATA.put(float.class, new float[] { Float.MIN_VALUE,
                Float.MAX_VALUE });

        TEST_DATA.put(int.class, new int[] { Integer.MIN_VALUE,
                Integer.MAX_VALUE });

        TEST_DATA.put(long.class, new long[] { Long.MIN_VALUE, Long.MAX_VALUE });

        TEST_DATA.put(short.class, new short[] { Short.MIN_VALUE,
                Short.MAX_VALUE });

        TEST_DATA.put(BigInteger.class, new BigInteger[] { null,
                BigInteger.ZERO });
        TEST_DATA.put(BigDecimal.class, new BigDecimal[] { null,
                BigDecimal.ZERO });

        TEST_DATA.put(String.class, new String[] { null, "" });
        TEST_DATA.put(Date.class, new Date[] { null, new Date(Long.MIN_VALUE),
                new Date(Long.MAX_VALUE) });

        TEST_DATA.put(List.class,
                new List<?>[] { null, Collections.EMPTY_LIST });
        TEST_DATA.put(Set.class, new Set<?>[] { null, Collections.EMPTY_SET });
        TEST_DATA.put(Map.class,
                new Map<?, ?>[] { null, Collections.EMPTY_MAP });
        TEST_DATA.put(Collection.class, TEST_DATA.get(List.class));

        final Map<Class<?>, Object> wrappedTestData =
                new HashMap<Class<?>, Object>();

        for (final Map.Entry<Class<?>, Object> testValueEntry : TEST_DATA.entrySet()) {
            final Class<?> testClass = testValueEntry.getKey();

            if (testClass.isPrimitive()) {
                final Class<?> wrapperClass = getWrapperClassFor(testClass);

                if (wrapperClass != null) {
                    final Object testValues = testValueEntry.getValue();
                    final int testValuesCount = Array.getLength(testValues);

                    final Object[] wrappedTestValues =
                            new Object[1 + testValuesCount];
                    wrappedTestValues[0] = null;

                    try {
                        final Constructor<?> constructor =
                                wrapperClass.getConstructor(testClass);

                        for (int i = 0; i < testValuesCount; i++) {
                            wrappedTestValues[1 + i] =
                                    constructor.newInstance(Array.get(
                                            testValues, i));
                        }

                    } catch (final SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final NoSuchMethodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final ArrayIndexOutOfBoundsException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (final InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    wrappedTestData.put(wrapperClass, wrappedTestValues);
                }
            }
        }

        TEST_DATA.putAll(wrappedTestData);
    }

    private Data() {
        throw new UnsupportedOperationException();
    }

    static Object getTestDataFor(final Class<?> targetClass) {
        final Object testValues = TEST_DATA.get(targetClass);

        if ((testValues == null) && (targetClass.getName().startsWith("java"))) {
            System.err.println(targetClass
                    + " is not fully supported, please report to JBound team.");
        }

        return testValues != null ? testValues : TEST_DATA.get(Object.class);
    }

    private static Class<?> getWrapperClassFor(final Class<?> primitiveClass) {
        if (primitiveClass.equals(boolean.class)) {
            return Boolean.class;
        }
        if (primitiveClass.equals(byte.class)) {
            return Byte.class;
        }
        if (primitiveClass.equals(char.class)) {
            return Character.class;
        }
        if (primitiveClass.equals(double.class)) {
            return Double.class;
        }
        if (primitiveClass.equals(float.class)) {
            return Float.class;
        }
        if (primitiveClass.equals(int.class)) {
            return Integer.class;
        }
        if (primitiveClass.equals(long.class)) {
            return Long.class;
        }
        if (primitiveClass.equals(short.class)) {
            return Short.class;
        }

        return null;
    }
}
