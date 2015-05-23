
package net.dossot.jbound.subject;

/**
 * @author David Dossot (david@dossot.net)
 */
public class BeanWithUnsupportedDataType
{
    private final ImmutableBean immutableBean;

    public BeanWithUnsupportedDataType(final ImmutableBean immutableBean)
    {
        if (immutableBean != null)
        {
            throw new RuntimeException();
        }

        this.immutableBean = immutableBean;
    }

    public ImmutableBean getImmutableBean()
    {
        return immutableBean;
    }
}
