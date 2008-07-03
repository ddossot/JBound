package org.jbound.api;

/**
 * @author David Dossot (david@dossot.net)
 */
public interface Restriction {
	Restriction skipping(EXERCISE... exercises);

	Restriction acceptingGenericExceptionsFrom(String... accessibleSignature);
}
