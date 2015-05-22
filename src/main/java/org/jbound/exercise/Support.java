
package org.jbound.exercise;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author David Dossot (david@dossot.net)
 */
final class Support
{

    /**
     * Handle JBound internal issues that should be reported but must not break
     * people's builds.
     */
    static void handleInternalException(final Exception e)
    {
        final StringWriter stringWriter = new StringWriter();
        stringWriter.write("---------- Internal JBound issue - Please report! ----------\n");
        e.printStackTrace(new PrintWriter(stringWriter));
        System.err.println(stringWriter.toString());
    }
}
