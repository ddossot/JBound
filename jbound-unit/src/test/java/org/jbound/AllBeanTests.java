package org.jbound;

import org.jbound.api.EXERCISE;
import org.jbound.api.Exercises;
import org.jbound.api.JBound;
import org.jbound.subject.InequalMutableBean;
import org.jbound.subject.MutableBean;
import org.junit.Test;

public class AllBeanTests {

	@Test
	public void fullyExerciseBeans() {
		JBound.run(new Exercises() {
			{
				forClasses(MutableBean.class, InequalMutableBean.class);
			}
		});
	}

	@Test
	public void exerciseBeansExceptEqualsHashCode() {
		JBound.run(new Exercises() {
			{
				// TODO test a bean that actually needs restrictions
				forClass(MutableBean.class)
						.skipping(EXERCISE.EQUALS, EXERCISE.HASHCODE);
			}
		});
	}
}
