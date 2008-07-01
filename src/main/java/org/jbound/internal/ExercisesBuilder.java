package org.jbound.internal;

import java.util.Arrays;
import java.util.EnumSet;
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

	public ExercisesBuilder(final List<Class<?>> exercisedClasses) {
		this.exercisedClasses = exercisedClasses;
		skipped = EnumSet.noneOf(EXERCISE.class);
	}

	public Restriction skipping(final EXERCISE exercise,
			final EXERCISE... additionalExercises) {

		skipped.add(exercise);
		skipped.addAll(Arrays.asList(additionalExercises));

		return this;
	}

	public void run() {
		// TODO extract in an exercise runner
		for (final Class<?> exercisedClass : exercisedClasses) {

			// TODO each test must be run with a catch for acceptable exceptions
			try {

				final Object exercisedObject = exercisedClass.newInstance();

				if (!skipped.contains(EXERCISE.EQUALS)) {
					// we do not care about the result of equal, we just check it is to
					// throwing exceptions.
					exercisedObject.equals(null);
					exercisedObject.equals(new Object());
					exercisedObject.equals(exercisedClass.newInstance());
				}

				if (!skipped.contains(EXERCISE.HASHCODE)) {
					// we do not care about the result of hashcode, we just check it is to
					// throwing exceptions.
					exercisedObject.hashCode();
				}

			} catch (final InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
