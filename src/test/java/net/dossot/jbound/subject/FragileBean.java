
package net.dossot.jbound.subject;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author David Dossot (david@dossot.net)
 */
public class FragileBean
{
    private String string;
    private int primitiveInteger;
    private Integer integer;
    private List<Long> longs;

    public FragileBean()
    {
        // NOOP
    }

    public FragileBean(final String string,
                       final int primitiveInteger,
                       final Integer integer,
                       final List<Long> longs)
    {

        fragileMethod(string);

        this.string = string;
        this.primitiveInteger = primitiveInteger;
        this.integer = integer;
        this.longs = longs;
    }

    private void fragileMethod(final String string)
    {
        string.length();
    }

    public String getString()
    {
        fragileMethod(string);
        return string;
    }

    public void setString(final String string)
    {
        fragileMethod(string);
        this.string = string;
    }

    public int getPrimitiveInteger()
    {
        return primitiveInteger;
    }

    public void setPrimitiveInteger(final int primitiveInteger)
    {
        this.primitiveInteger = primitiveInteger;
    }

    public Integer getInteger()
    {
        return integer;
    }

    public void setInteger(final Integer integer)
    {
        this.integer = integer;
    }

    public List<Long> getLongs()
    {
        return longs;
    }

    public void setLongs(final List<Long> longs)
    {
        this.longs = longs;
    }

    @Override
    public String toString()
    {
        fragileMethod(string);
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode()
    {
        fragileMethod(string);

        final int prime = 31;
        int result = 1;
        result = prime * result + ((integer == null) ? 0 : integer.hashCode());
        result = prime * result + ((longs == null) ? 0 : longs.hashCode());
        result = prime * result + primitiveInteger;
        result = prime * result + ((string == null) ? 0 : string.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        fragileMethod(string);

        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final FragileBean other = (FragileBean) obj;
        if (integer == null)
        {
            if (other.integer != null) return false;
        }
        else if (!integer.equals(other.integer)) return false;
        if (longs == null)
        {
            if (other.longs != null) return false;
        }
        else if (!longs.equals(other.longs)) return false;
        if (primitiveInteger != other.primitiveInteger) return false;
        if (string == null)
        {
            if (other.string != null) return false;
        }
        else if (!string.equals(other.string)) return false;
        return true;
    }

}
