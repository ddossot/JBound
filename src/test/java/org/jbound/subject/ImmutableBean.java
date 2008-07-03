package org.jbound.subject;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author David Dossot (david@dossot.net)
 */
public class ImmutableBean {

	private final String string;

	private final int primitiveInteger;

	private final Integer integer;

	private final List<Long> longs;

	public ImmutableBean(final String string, final int primitiveInteger,
			final Integer integer, final List<Long> longs) {

		this.string = string;
		this.primitiveInteger = primitiveInteger;
		this.integer = integer;
		this.longs = longs;
	}

	public String getString() {
		return string;
	}

	public int getPrimitiveInteger() {
		return primitiveInteger;
	}

	public Integer getInteger() {
		return integer;
	}

	public List<Long> getLongs() {
		return longs;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((integer == null) ? 0 : integer.hashCode());
		result = prime * result + ((longs == null) ? 0 : longs.hashCode());
		result = prime * result + primitiveInteger;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ImmutableBean other = (ImmutableBean) obj;
		if (integer == null) {
			if (other.integer != null)
				return false;
		} else if (!integer.equals(other.integer))
			return false;
		if (longs == null) {
			if (other.longs != null)
				return false;
		} else if (!longs.equals(other.longs))
			return false;
		if (primitiveInteger != other.primitiveInteger)
			return false;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

}
