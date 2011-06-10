package org.jbound.subject;

import java.util.List;

/**
 * @author David Dossot (david@dossot.net)
 */
public class PlainMutableBean {

	private String string;

	private int primitiveInteger;

	private Integer integer;

	private List<Long> longs;

	public PlainMutableBean() {
		// NOOP
	}

	public PlainMutableBean(final String string, final int primitiveInteger,
			final Integer integer, final List<Long> longs) {

		this.string = string;
		this.primitiveInteger = primitiveInteger;
		this.integer = integer;
		this.longs = longs;
	}

	public String getString() {
		return string;
	}

	public void setString(final String string) {
		this.string = string;
	}

	public int getPrimitiveInteger() {
		return primitiveInteger;
	}

	public void setPrimitiveInteger(final int primitiveInteger) {
		this.primitiveInteger = primitiveInteger;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(final Integer integer) {
		this.integer = integer;
	}

	public List<Long> getLongs() {
		return longs;
	}

	public void setLongs(final List<Long> longs) {
		this.longs = longs;
	}

}
