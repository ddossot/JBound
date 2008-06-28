package org.jbound.api;

/**
 * @author David Dossot (david@dossot.net)
 */
public interface Exerciser extends Runnable {

	Restriction exercise(Class<?> exercisedClass,
			Class<?>... otherExercisedClasses);
}
