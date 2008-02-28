package at.ac.tuwien.e0525580.omov.tools.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;

import com.thoughtworks.xstream.XStream;

public class ExporterXml {

    public static void main(String[] args) throws BusinessException {
//        List<Movie> m = new LinkedList<Movie>();
//        m.add(Movie.getDummy());
//        new ExporterXml(m, new OutputStreamWriter(System.out)).process();
        
        for (Movie m : ExporterXml.encodeMovies(new File("/movie.xml"))) {
            System.out.println(m);
        }
    }
    
    public ExporterXml() {
        
    }

    public void process(List<Movie> movies, File target) throws BusinessException {
        XStream xstream = new XStream();
        String xml = xstream.toXML(movies);
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(target));
            writer.write(xml);
        } catch(IOException e) {
            throw new BusinessException("Could not generate HTML report!", e);
        } finally {
            if(writer != null) try { writer.close(); } catch(IOException e) {};
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Movie> encodeMovies(File xmlFile) throws BusinessException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(xmlFile));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while( (line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            XStream xstream = new XStream();
            return (List<Movie>) xstream.fromXML(sb.toString());
        } catch(IOException e) {
            throw new BusinessException("Could not encode movies from: " + xmlFile.getAbsolutePath(), e);
        } finally {
            if(reader != null) try { reader.close(); } catch(IOException e) {};
        }
    }

//    @Override
//    public String getFormat() {
//        return FORMAT;
//    }
}
