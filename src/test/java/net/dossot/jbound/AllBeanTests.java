
package net.dossot.jbound;

import net.dossot.jbound.api.EXERCISE;
import net.dossot.jbound.api.Exercises;
import net.dossot.jbound.api.JBound;
import net.dossot.jbound.subject.BeanWithUnsupportedDataType;
import net.dossot.jbound.subject.DefensiveBean;
import net.dossot.jbound.subject.DefensiveBeanWithWeakConstructor;
import net.dossot.jbound.subject.FragileBean;
import net.dossot.jbound.subject.ImmutableBean;
import net.dossot.jbound.subject.MutableBean;
import net.dossot.jbound.subject.PlainMutableBean;

import org.junit.BeforeClass;
import org.junit.Test;

public class AllBeanTests
{
    @Test
    public void fullyExerciseBeans()
    {
        JBound.run(new Exercises()
        {
            {
                forClasses(MutableBean.class, PlainMutableBean.class, ImmutableBean.class,
                    DefensiveBean.class);
            }
        });
    }

    @Test
    public void exerciseBeanWithSkippingAndAccepting()
    {
        JBound.run(new Exercises()
        {
            {
                forClass(FragileBean.class).skipping(EXERCISE.EQUALS, EXERCISE.HASHCODE, EXERCISE.TO_STRING)
                .acceptingGenericExceptionsFrom(
                    "public org.jbound.subject.FragileBean(java.lang.String,int,java.lang.Integer,java.util.List)",
                    "public java.lang.String org.jbound.subject.FragileBean.getString()",
                                "public void org.jbound.subject.FragileBean.setString(java.lang.String)");
            }
        });
    }

    @Test
    public void exerciseBeanWithAccepting()
    {
        JBound.run(new Exercises()
        {
            {
                forClass(DefensiveBeanWithWeakConstructor.class).acceptingGenericExceptionsFrom(
                                "public org.jbound.subject.DefensiveBeanWithWeakConstructor(java.lang.String,int,java.lang.Integer,java.util.List)");
            }
        });
    }

    @BeforeClass
    public static void registerCustomDataType()
    {
        JBound.registerCustomDataType(ImmutableBean.class, null, new ImmutableBean(null, 0, null, null));
    }

    @Test(expected = AssertionError.class)
    public void exerciseBeanWithUnsupportedDataType()
    {
        JBound.run(new Exercises()
        {
            {
                forClass(BeanWithUnsupportedDataType.class);
            }
        });
    }
}
