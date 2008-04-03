package at.ac.tuwien.e0525580.omov.spielwiese;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    private static int BLOCKSIZE = 8192;
    
    public static void main(String[] args) throws Exception {
//        gzip();
//        gunzip();
        zip();
        
        System.out.println("finished");
    }
    
    
    public static void zip() throws Exception {
        System.out.println("zip()");
        ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(new File("/zip/myzip.zip")));
        
        ZipEntry entry = new ZipEntry("asdf.script");
        zipout.putNextEntry(entry);
        
        
        byte buffer[] = new byte[BLOCKSIZE];
        FileInputStream in = new FileInputStream(new File("/zip/asdf.script"));
        
        for ( int length; (length = in.read(buffer, 0, BLOCKSIZE)) != -1; )
          zipout.write( buffer, 0, length );

        in.close();
        zipout.closeEntry();
        zipout.close();
    }
    
//    public static void unzip() throws Exception {
//        ZipFile zip = new ZipFile(new File("/zip/myzip.zip"));
        
//        ZipEntry entry = zip.getEntry("asdf.script");
//        InputStream in = zip.getInputStream(entry);
        // ...
//    }
    
    public static void gzip() throws Exception {
        System.out.println("gzip()");
        GZIPOutputStream zipout = new GZIPOutputStream(new FileOutputStream("/zip/myzip.gz"));
        
        byte buffer[] = new byte[BLOCKSIZE];

        
        File dir = new File("/zip/covers");
        System.out.println("Dir '"+dir.getAbsolutePath()+"' exists: " + dir.exists());
        FileInputStream in = new FileInputStream(dir);

        for ( int length; (length = in.read(buffer, 0, BLOCKSIZE)) != -1; )
          zipout.write( buffer, 0, length );

        in.close();
        zipout.close();
    }
    
    public static void gunzip() throws Exception {
        System.out.println("gunzip()");
        GZIPInputStream zipin = new GZIPInputStream(new FileInputStream("/zip/myzip.gz"));
        
        byte buffer[] = new byte[BLOCKSIZE];
        FileOutputStream out = new FileOutputStream("/zip/covers");

        for ( int length; (length = zipin.read(buffer, 0, BLOCKSIZE)) != -1; )
          out.write( buffer, 0, length );

        out.close();
        zipin.close();
    }
}
