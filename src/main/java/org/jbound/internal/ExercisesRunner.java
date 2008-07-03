package org.jbound.internal;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbound.api.EXERCISE;

/**
 * FIXME this class is GROSS - it is under construction and requires a drastic
 * refactoring. JBound is still a spike.
 * 
 * @author David Dossot (david@dossot.net)
 */
public class ExercisesRunner implements Runnable {

    private final List<Class<?>> exercisedClasses;

    private final Set<String> accepted;

    private final Set<EXERCISE> skipped;

    public ExercisesRunner(final List<Class<?>> exercisedClasses,
            final Set<EXERCISE> skipped, final Set<String> accepted) {
        this.exercisedClasses = exercisedClasses;
        this.skipped = skipped;
        this.accepted = accepted;
    }

    private List<Object> newInstancesOf(final Class<?> exercisedClass) {
        final List<Object> result = new ArrayList<Object>();

        for (final Constructor<?> constructor : exercisedClass.getConstructors()) {
            result.addAll(newInstancesFrom(constructor, new ArrayList<Object>()));
        }

        return Collections.unmodifiableList(result);
    }

    private void acceptOrRethrow(final AccessibleObject context,
            final InvocationTargetException ite) {

        final Throwable iteCause = ite.getCause();

        if (iteCause == null) {
            // this is a problem with JBound doing a particular exercise
            ite.printStackTrace();
            return;
        }

        if (iteCause.getMessage() != null) {
            // we got an exception but it has a message, so it is probably a
            // user crafted one, hence we accept it silently
            return;
        }

        final String contextAsString = context.toString();

        if (accepted.contains(contextAsString)) {
            // we have be told to accept this as a source of generic exceptions
            return;
        }

        // this is an exception without a message, so most probably a generic
        // one
        throw new AssertionError("Received a generic " + iteCause.getClass()
                + " when calling " + contextAsString);
    }

    private List<Object> newInstancesFrom(final Constructor<?> constructor,
            final List<Object> parameters) {

        final List<Object> result = new ArrayList<Object>();

        if (parameters.size() >= constructor.getParameterTypes().length) {
            try {
                result.add(constructor.newInstance(parameters.toArray()));
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
                acceptOrRethrow(constructor, e);
            }
        } else {
            final Class<?> parameterType =
                    constructor.getParameterTypes()[parameters.size()];

            final Object testValues = getTestDataFor(parameterType);

            final int testValuesCount = Array.getLength(testValues);
            for (int i = 0; i < testValuesCount; i++) {
                final List<Object> nextParameters =
                        new ArrayList<Object>(parameters);

                nextParameters.add(Array.get(testValues, i));
                result.addAll(newInstancesFrom(constructor, nextParameters));
            }

        }

        return result;
    }

    public void run() {
        for (final Class<?> exercisedClass : exercisedClasses) {

            System.out.println("Exercising class: " + exercisedClass);

            // TODO each test must be run with a catch for acceptable exceptions
            try {

                final List<Object> exercisedObjects =
                        newInstancesOf(exercisedClass);

                for (final Object exercisedObject : exercisedObjects) {

                    // we do not care about the actual result of equal, hashcode
                    // or toString, we just check it is not throwing exceptions.
                    // FIXME accept generic exceptions from these 3 guys
                    if (!skipped.contains(EXERCISE.EQUALS)) {
                        exercisedObject.equals(null);
                        exercisedObject.equals(new Object());

                        for (final Object exercisedObjectBis : exercisedObjects) {
                            exercisedObject.equals(exercisedObjectBis);
                        }
                    }

                    if (!skipped.contains(EXERCISE.HASHCODE)) {
                        exercisedObject.hashCode();
                    }

                    if (!skipped.contains(EXERCISE.TO_STRING)) {
                        exercisedObject.toString();
                    }

                    if (!skipped.contains(EXERCISE.BEAN_PROPERTIES)) {
                        try {
                            final BeanInfo beanInfo =
                                    Introspector.getBeanInfo(exercisedClass);

                            final PropertyDescriptor[] propertyDescriptors =
                                    beanInfo.getPropertyDescriptors();

                            if (propertyDescriptors != null) {
                                for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                                    final Method readMethod =
                                            propertyDescriptor.getReadMethod();

                                    if (readMethod != null) {
                                        try {
                                            readMethod.invoke(exercisedObject);
                                        } catch (final IllegalArgumentException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (final InvocationTargetException e) {
                                            acceptOrRethrow(readMethod, e);
                                        }
                                    }

                                    final Method writeMethod =
                                            propertyDescriptor.getWriteMethod();

                                    if (writeMethod != null) {
                                        final Class<?> setterParameterClass =
                                                writeMethod.getParameterTypes()[0];

                                        final Object testValues =
                                                getTestDataFor(setterParameterClass);

                                        final int testValuesCount =
                                                Array.getLength(testValues);
                                        for (int i = 0; i < testValuesCount; i++) {
                                            try {
                                                writeMethod.invoke(
                                                        exercisedObject,
                                                        Array.get(testValues, i));
                                            } catch (final ArrayIndexOutOfBoundsException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            } catch (final IllegalArgumentException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            } catch (final InvocationTargetException e) {
                                                acceptOrRethrow(writeMethod, e);
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (final IntrospectionException ie) {
                            // TODO Auto-generated catch block
                            ie.printStackTrace();
                        }
                    }
                }

            } catch (final IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

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

    private static Object getTestDataFor(final Class<?> targetClass) {
        final Object testValues = TEST_DATA.get(targetClass);

        return testValues != null ? testValues : TEST_DATA.get(Object.class);
    }
}
