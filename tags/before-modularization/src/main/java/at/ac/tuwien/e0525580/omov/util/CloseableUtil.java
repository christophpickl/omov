/*
 * @(#) $Id:  $
 *
 * Copyright 2004/2005 by SPARDAT Sparkassen-Datendienst Ges.m.b.H.,
 * A-1110 Wien, Geiselbergstr.21-25.
 * All rights reserved.
 *
 */
package at.ac.tuwien.e0525580.omov.util;

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
