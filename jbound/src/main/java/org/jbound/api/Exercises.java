package org.jbound.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbound.internal.ExercisesBuilder;

/**
 * @author David Dossot (david@dossot.net)
 */
public class Exercises {

	private final List<ExercisesBuilder> exercisesBuilders;

	public Exercises() {
		exercisesBuilders = new ArrayList<ExercisesBuilder>();
	}

	public void run() {
		for (final ExercisesBuilder exercisesBuilder : exercisesBuilders) {
			exercisesBuilder.run();
		}
	}

	protected Restriction forClass(final Class<?> exercisedClass) {
		if (exercisedClass == null) {
			throw new NullPointerException("exercisedClass can not be null");
		}

		return forClasses(exercisedClass);
	}

	protected Restriction forClasses(final Class<?> exercisedClass,
			final Class<?>... otherExercisedClasses) {

		if (exercisedClass == null) {
			throw new NullPointerException("exercisedClass can not be null");
		}

		if (otherExercisedClasses == null) {
			throw new NullPointerException("otherExercisedClasses can not be null");
		}

		final List<Class<?>> exercisedClasses = new ArrayList<Class<?>>();
		exercisedClasses.add(exercisedClass);
		exercisedClasses.addAll(Arrays.asList(otherExercisedClasses));

		final ExercisesBuilder builder = new ExercisesBuilder(exercisedClasses);
		exercisesBuilders.add(builder);
		return builder;
	}

}
