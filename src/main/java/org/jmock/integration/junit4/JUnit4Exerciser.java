package org.jmock.integration.junit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbound.api.Exerciser;
import org.jbound.api.Restriction;
import org.jbound.internal.ExercisesBuilder;

/**
 * @author David Dossot (david@dossot.net)
 */
public final class JUnit4Exerciser implements Exerciser {

	private ExercisesBuilder builder;

	public Restriction exercise(final Class<?> exercisedClass,
			final Class<?>... otherExercisedClasses) {

		final List<Class<?>> exercisedClasses = new ArrayList<Class<?>>();
		exercisedClasses.add(exercisedClass);
		exercisedClasses.addAll(Arrays.asList(otherExercisedClasses));

		builder = new ExercisesBuilder(exercisedClasses);
		return builder;
	}

	public void run() {
		builder.run();
	}

}
