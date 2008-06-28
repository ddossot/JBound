package org.jbound;

import org.jbound.api.EXERCISE;
import org.jbound.api.Exerciser;
import org.jbound.subject.InequalMutableBean;
import org.jbound.subject.MutableBean;
import org.jmock.integration.junit4.JBound;
import org.jmock.integration.junit4.JUnit4Exerciser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JBound.class)
public class AllBeanTests {

	Exerciser exerciser = new JUnit4Exerciser();

	@Test
	public void boundaryTestMutableBean() {
		exerciser.exercise(MutableBean.class);
	}

	@Test
	public void boundaryTestInequalMutableBean() {
		exerciser.exercise(InequalMutableBean.class).skipping(EXERCISE.EQUALS,
				EXERCISE.HASHCODE);
	}
}
