
package net.dossot.jbound.exercise;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dossot.jbound.api.EXERCISE;

/**
 * When we exercise a class, we do not care about the actual result of equal,
 * hashcode, toString or bean properties, we just check they are not throwing generic
 * exceptions.
 *
 * @author David Dossot (david@dossot.net)
 */
final class Runner implements Runnable
{

    private final Set<String> accepted;

    private final List<Class<?>> exercisedClasses;

    private final Set<EXERCISE> skipped;

    private final Map<Class<?>, Object[]> customTestData;

    public Runner(final List<Class<?>> exercisedClasses,
                  final Set<EXERCISE> skipped,
                  final Set<String> accepted,
                  final Map<Class<?>, Object[]> customTestData)
    {

        this.exercisedClasses = exercisedClasses;
        this.skipped = skipped;
        this.accepted = accepted;
        this.customTestData = customTestData;
    }

    private void acceptOrRethrow(final AccessibleObject context, final InvocationTargetException ite)
    {

        final Throwable iteCause = ite.getCause();

        if (iteCause == null)
        {
            // this is a problem with JBound doing a particular exercise
            ite.printStackTrace();
            return;
        }

        if (iteCause.getMessage() != null)
        {
            // we got an exception but it has a message, so it is probably a
            // user crafted one, hence we accept it silently
            return;
        }

        final String contextAsString = context.toString();

        if (accepted.contains(contextAsString))
        {
            // we have be told to accept this as a source of generic exceptions
            return;
        }

        // this is an exception without a message, so most probably a generic
        // one
        final AssertionError assertionError = new AssertionError("Received a generic " + iteCause.getClass()
                                                                 + " when calling: " + contextAsString);

        assertionError.initCause(iteCause);
        throw assertionError;
    }

    /**
     * Recursively build constructor parameters until it can be invoked.
     */
    private List<Object> newInstancesFrom(final Constructor<?> constructor, final List<Object> parameters)
    {

        final List<Object> result = new ArrayList<Object>();

        if (parameters.size() >= constructor.getParameterTypes().length)
        {

            final Object newInstance = instantiateClass(constructor, parameters);

            if (newInstance != null)
            {
                result.add(newInstance);
            }

        }
        else
        {
            final Class<?> parameterType = constructor.getParameterTypes()[parameters.size()];

            for (final Object testValue : getTestDataFor(parameterType))
            {
                final List<Object> nextParameters = new ArrayList<>(parameters);

                nextParameters.add(testValue);
                result.addAll(newInstancesFrom(constructor, nextParameters));
            }
        }

        return result;
    }

    private Object instantiateClass(final Constructor<?> constructor, final List<Object> parameters)
    {

        try
        {
            return constructor.newInstance(parameters.toArray());
        }
        catch (final IllegalArgumentException e)
        {
            Support.handleInternalException(e);
        }
        catch (final InstantiationException e)
        {
            Support.handleInternalException(e);
        }
        catch (final IllegalAccessException e)
        {
            Support.handleInternalException(e);
        }
        catch (final InvocationTargetException e)
        {
            acceptOrRethrow(constructor, e);
        }

        return null;
    }

    private List<Object> newInstancesOf(final Class<?> exercisedClass)
    {
        final List<Object> result = new ArrayList<Object>();

        for (final Constructor<?> constructor : exercisedClass.getConstructors())
        {
            result.addAll(newInstancesFrom(constructor, new ArrayList<>()));
        }

        return Collections.unmodifiableList(result);
    }

    public void run()
    {
        for (final Class<?> exercisedClass : exercisedClasses)
        {
            exerciseClass(exercisedClass);
        }
    }

    private void exerciseClass(final Class<?> exercisedClass)
    {
        System.out.println("Exercising class: " + exercisedClass);
        exerciseObjects(exercisedClass, newInstancesOf(exercisedClass));
    }

    private void exerciseObjects(final Class<?> exercisedClass, final List<Object> exercisedObjects)
    {

        for (final Object exercisedObject : exercisedObjects)
        {
            exerciseObject(exercisedClass, exercisedObjects, exercisedObject);
        }
    }

    private void exerciseObject(final Class<?> exercisedClass,
                                final List<Object> exercisedObjects,
                                final Object exercisedObject)
    {

        exerciseEquals(exercisedObjects, exercisedObject);
        exerciseHashCode(exercisedObject);
        exerciseToString(exercisedObject);
        exerciseBeanProperties(exercisedClass, exercisedObject);
    }

    private void exerciseBeanProperties(final Class<?> exercisedClass, final Object exercisedObject)
    {

        if (!skipped.contains(EXERCISE.BEAN_PROPERTIES))
        {
            try
            {
                final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(exercisedClass)
                    .getPropertyDescriptors();

                if (propertyDescriptors != null)
                {
                    exerciseBeanProperties(exercisedObject, propertyDescriptors);
                }

            }
            catch (final IntrospectionException e)
            {
                Support.handleInternalException(e);
            }
        }
    }

    private void exerciseBeanProperties(final Object exercisedObject,
                                        final PropertyDescriptor[] propertyDescriptors)
    {

        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
            exercisedReadMethod(exercisedObject, propertyDescriptor);
            exercisedWriteMethod(exercisedObject, propertyDescriptor);
        }
    }

    private void exercisedWriteMethod(final Object exercisedObject,
                                      final PropertyDescriptor propertyDescriptor)
    {

        final Method writeMethod = propertyDescriptor.getWriteMethod();

        if (writeMethod != null)
        {
            final Class<?> setterParameterClass = writeMethod.getParameterTypes()[0];

            for (final Object testValue : getTestDataFor(setterParameterClass))
            {
                try
                {
                    writeMethod.invoke(exercisedObject, testValue);
                }
                catch (final IllegalAccessException e)
                {
                    Support.handleInternalException(e);
                }
                catch (final IllegalArgumentException e)
                {
                    Support.handleInternalException(e);
                }
                catch (final InvocationTargetException e)
                {
                    acceptOrRethrow(writeMethod, e);
                }
            }
        }
    }

    private Object[] getTestDataFor(final Class<?> targetClass)
    {
        final Object[] testData = customTestData.get(targetClass);

        if (testData != null)
        {
            return testData;
        }

        return Data.getTestDataFor(targetClass);
    }

    private void exercisedReadMethod(final Object exercisedObject, final PropertyDescriptor propertyDescriptor)
    {
        final Method readMethod = propertyDescriptor.getReadMethod();

        if (readMethod != null)
        {
            try
            {
                readMethod.invoke(exercisedObject);
            }
            catch (final IllegalArgumentException e)
            {
                Support.handleInternalException(e);
            }
            catch (final InvocationTargetException e)
            {
                acceptOrRethrow(readMethod, e);
            }
            catch (final IllegalAccessException e)
            {
                Support.handleInternalException(e);
            }
        }
    }

    private void exerciseToString(final Object exercisedObject)
    {
        if (!skipped.contains(EXERCISE.TO_STRING))
        {
            exercisedObject.toString();
        }
    }

    private void exerciseHashCode(final Object exercisedObject)
    {
        if (!skipped.contains(EXERCISE.HASHCODE))
        {
            exercisedObject.hashCode();
        }
    }

    private void exerciseEquals(final List<Object> exercisedObjects, final Object exercisedObject)
    {

        if (!skipped.contains(EXERCISE.EQUALS))
        {
            exercisedObject.equals(null);
            exercisedObject.equals(new Object());

            for (final Object exercisedObjectBis : exercisedObjects)
            {
                exercisedObject.equals(exercisedObjectBis);
            }
        }
    }

}
