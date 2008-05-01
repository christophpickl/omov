package net.sourceforge.omov.core.tools.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sourceforge.omov.core.util.CloseableUtil;
import net.sourceforge.omov.core.util.GuiUtil;

public class HtmlFileConverter {

    private HtmlFileConverter() {
        /* no instantiation */
    }

    public static void main(String[] args) throws IOException {
        final File file = GuiUtil.getFile();
        if(file != null) {
            convert(file);
        }
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
