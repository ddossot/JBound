package org.jmock.integration.junit4;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jbound.api.Exerciser;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

/**
 * Largely inspired by <code>org.jmock.integration.junit4.JMock</code>.
 * 
 * @author David Dossot (david@dossot.net)
 */
public final class JBound extends JUnit4ClassRunner {

	private final Field exerciserField;

	public JBound(final Class<?> testClass) throws InitializationError {
		super(testClass);
		exerciserField = findExerciserField(testClass);
		exerciserField.setAccessible(true);
	}

	@Override
	protected TestMethod wrapMethod(final Method method) {
		return new TestMethod(method, getTestClass()) {
			@Override
			public void invoke(final Object testFixture)
					throws IllegalAccessException, InvocationTargetException {

				try {
					super.invoke(testFixture);
					runExercisesOf(testFixture);
				} catch (final InvocationTargetException e) {
					final Throwable actual = e.getTargetException();
					final Class<? extends Throwable> expectedType = this
							.getExpectedException();

					if (expectedType != null && expectedType.isInstance(actual)) {
						runExercisesOf(testFixture);
					}

					throw e;
				}
			}
		};
	}

	private void runExercisesOf(final Object testFixture) {
		exerciserOf(testFixture).run();
	}

	protected Exerciser exerciserOf(final Object test) {
		try {
			final Exerciser exerciser = (Exerciser) exerciserField.get(test);
			if (exerciser == null) {
				throw new NullPointerException("Exerciser named '"
						+ exerciserField.getName() + "' is null");
			}
			return exerciser;
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException("cannot get value of field "
					+ exerciserField.getName(), e);
		}
	}

	private static Field findExerciserField(final Class<?> testClass)
			throws InitializationError {
		for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
			for (final Field field : c.getDeclaredFields()) {
				if (Exerciser.class.isAssignableFrom(field.getType())) {
					return field;
				}
			}
		}

		throw new InitializationError("no Exerciser found in test class "
				+ testClass);
	}

}
