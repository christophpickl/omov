package net.sourceforge.omov.core.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CloseableUtil {

    private static final Log LOG = LogFactory.getLog(CloseableUtil.class);

    private CloseableUtil() {
        /* no instantiation */
    }

    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.warn("Could not close closeable of type "+closeable.getClass().getName()+"!", e);
            }
        }
    }
}
