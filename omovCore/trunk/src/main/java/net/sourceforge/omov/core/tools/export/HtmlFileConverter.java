package net.sourceforge.omov.core.tools.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sourceforge.omov.core.util.CloseableUtil;

public class HtmlFileConverter {

    private HtmlFileConverter() {
        /* no instantiation */
    }

    public static void convert(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            System.out.println("    return ");
            while( (line = reader.readLine()) != null) {
                System.out.print("            \"");
                System.out.print(line.replace("\"", "\\\""));
                System.out.print("\\n\" +");
                System.out.println();
            }
        } finally {
            CloseableUtil.close(reader);
        }
    }
}
