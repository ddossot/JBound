package org.jbound.internal;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbound.api.EXERCISE;
import org.jbound.api.Restriction;

/**
 * @author David Dossot (david@dossot.net)
 */
public final class ExercisesBuilder implements Restriction, Runnable {

    private final List<Class<?>> exercisedClasses;

    private final Set<EXERCISE> skipped;

    private final Set<String> accepted;

    public ExercisesBuilder(final List<Class<?>> exercisedClasses) {
        this.exercisedClasses = exercisedClasses;
        skipped = EnumSet.noneOf(EXERCISE.class);
        accepted = new HashSet<String>();
    }

    public Restriction skipping(final EXERCISE exercise,
            final EXERCISE... additionalExercises) {

        if (exercise == null) {
            throw new NullPointerException("Null is not a valid exercice");
        }

        if (additionalExercises == null) {
            throw new NullPointerException(
                    "Null is not a valid additional exercice");
        }

        skipped.add(exercise);
        skipped.addAll(Arrays.asList(additionalExercises));

        return this;
    }

    public void run() {
        new ExercisesRunner(exercisedClasses, skipped, accepted).run();
    }

    public Restriction acceptingGenericExceptionsFrom(
            final String accessibleSignature,
            final String... additionalAccessibleSignature) {

        if (accessibleSignature == null) {
            throw new NullPointerException(
                    "Null is not a valid accessible signature");
        }

        if (additionalAccessibleSignature == null) {
            throw new NullPointerException(
                    "Null is not a valid additional accessible signature");
        }

        // we merge all the signatures into one set because it is not necessary
        // to scope them to a particular class or set of classes
        accepted.add(accessibleSignature);
        accepted.addAll(Arrays.asList(additionalAccessibleSignature));

        return null;
    }
}
