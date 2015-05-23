
package net.dossot.jbound.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author David Dossot (david@dossot.net)
 */
public final class JBound
{
    private static final Map<Class<?>, Object[]> CUSTOM_TEST_DATA = new HashMap<>();

    public static void run(final Exercises exercises)
    {
        exercises.run(CUSTOM_TEST_DATA);
    }

    public static void registerCustomDataType(final Class<?> customClass, final Object... customClassValues)
    {
        CUSTOM_TEST_DATA.put(customClass, customClassValues);
    }

    private JBound()
    {
        throw new UnsupportedOperationException();
    }
}
