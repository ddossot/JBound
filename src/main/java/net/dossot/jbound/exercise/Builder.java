
package net.dossot.jbound.exercise;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dossot.jbound.api.EXERCISE;
import net.dossot.jbound.api.Restriction;

/**
 * @author David Dossot (david@dossot.net)
 */
public final class Builder implements Restriction
{

    private final List<Class<?>> exercisedClasses;

    private final Set<EXERCISE> skipped;

    private final Set<String> accepted;

    public Builder(final List<Class<?>> exercisedClasses)
    {
        this.exercisedClasses = exercisedClasses;
        skipped = EnumSet.noneOf(EXERCISE.class);
        accepted = new HashSet<String>();
    }

    public Restriction skipping(final EXERCISE... exercises)
    {

        if (exercises == null)
        {
            throw new NullPointerException("Null is not valid for exercises");
        }

        skipped.addAll(Arrays.asList(exercises));
        return this;
    }

    public void run(final Map<Class<?>, Object[]> customTestData)
    {
        new Runner(exercisedClasses, skipped, accepted, customTestData).run();
    }

    public Restriction acceptingGenericExceptionsFrom(final String... accessibleSignatures)
    {

        if (accessibleSignatures == null)
        {
            throw new NullPointerException("Null is not valid for accessibleSignatures");
        }

        // we merge all the signatures into one set because it is not necessary
        // to scope them to a particular class or set of classes
        accepted.addAll(asList(accessibleSignatures));

        return null;
    }
}
