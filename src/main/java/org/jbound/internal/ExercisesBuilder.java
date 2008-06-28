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
			System.out.println("*** " + exercisedClass);

			try {

				final Object exercisedObject1 = exercisedClass.newInstance();
				final Object exercisedObject2 = exercisedClass.newInstance();

				if (!skipped.contains(EXERCISE.EQUALS)) {
					if (!exercisedObject1.equals(exercisedObject2)) {
						throw new AssertionError(
								"Two newly created instances are not equal");
					}
				}

				if (!skipped.contains(EXERCISE.HASHCODE)) {
					if (exercisedObject1.hashCode() != exercisedObject2.hashCode()) {
						throw new AssertionError(
								"Two newly created instances are not equal");
					}
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
