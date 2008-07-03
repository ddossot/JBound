package org.jbound.api;

/**
 * @author David Dossot (david@dossot.net)
 */
public interface Restriction {
    Restriction skipping(EXERCISE exercise, EXERCISE... additionalExercises);

    Restriction acceptingGenericExceptionsFrom(String accessibleSignature,
            String... additionalAccessibleSignature);
}
