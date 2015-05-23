
package net.dossot.jbound.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.dossot.jbound.exercise.Builder;

/**
 * @author David Dossot (david@dossot.net)
 */
public class Exercises
{
    private final List<Builder> exercisesBuilders;

    public Exercises()
    {
        exercisesBuilders = new ArrayList<Builder>();
    }

    public void run(final Map<Class<?>, Object[]> customTestData)
    {
        for (final Builder exercisesBuilder : exercisesBuilders)
        {
            exercisesBuilder.run(customTestData);
        }
    }

    protected Restriction forClass(final Class<?> exercisedClass)
    {
        if (exercisedClass == null)
        {
            throw new NullPointerException("exercisedClass can not be null");
        }

        return forClasses(exercisedClass);
    }

    protected Restriction forClasses(final Class<?> exercisedClass, final Class<?>... otherExercisedClasses)
    {

        if (exercisedClass == null)
        {
            throw new NullPointerException("exercisedClass can not be null");
        }

        if (otherExercisedClasses == null)
        {
            throw new NullPointerException("otherExercisedClasses can not be null");
        }

        final List<Class<?>> exercisedClasses = new ArrayList<Class<?>>();
        exercisedClasses.add(exercisedClass);
        exercisedClasses.addAll(Arrays.asList(otherExercisedClasses));

        final Builder builder = new Builder(exercisedClasses);
        exercisesBuilders.add(builder);
        return builder;
    }

}
