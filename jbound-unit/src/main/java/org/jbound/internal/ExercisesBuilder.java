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
		ExercisesRunner.run(exercisedClasses, skipped);
	}

}
