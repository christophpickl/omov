package at.ac.tuwien.e0525580.omov.tools.webdata;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Tag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.visitors.NodeVisitor;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

class ImdbCoverFetcher extends NodeVisitor {

    private static final Log LOG = LogFactory.getLog(ImdbCoverFetcher.class);
    
    public static void main(String[] args) throws BusinessException {
        final String url = "http://ia.imdb.com/media/imdb/01/M/==/QM/yM/jM/5M/TN/wc/TZ/tF/kX/nB/na/B5/lM/B5/FO/4Y/TO/wE/TM/wA/jM/B5/VM._SY400_SX600_.jpg";
        ImdbCoverFetcher.downloadFile(url, new File("/myimg.jpg"));
        System.out.println("finished.");
    }
    
    private static final SimpleDateFormat FILE_NAME_FORMAT = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss_SSS");
    private static final String DEFAULT_EXTENSION = "jpg";
    
    public void visitTag(Tag tag) {
        if(tag.getTagName().equalsIgnoreCase("table") && tag.getAttribute("id") != null && tag.getAttribute("id").equals("principal")) {
            
            // tag (TableTag) -> [1] TableRow -> [0] TableColumn -> [0] ImageTag 
            ImageTag imageTag = (ImageTag) tag.getChildren().elementAt(1).getChildren().elementAt(0).getChildren().elementAt(0);
            final String absoluteImagePath = imageTag.getAttribute("src");
            LOG.info("found image source '"+absoluteImagePath+"'.");
            
            final String webfileName = absoluteImagePath.substring(absoluteImagePath.lastIndexOf("/") + 1, absoluteImagePath.length());
            
            String extension = FileUtil.extractExtension(webfileName);
            if(extension == null) {
                LOG.warn("Could not get extension from file '"+webfileName+"'! Setting it to default '"+DEFAULT_EXTENSION+"' extension.");
                extension = DEFAULT_EXTENSION;
            }
            final File target = new File(Configuration.getInstance().getTemporaryFolder(), FILE_NAME_FORMAT.format(new Date()) + "." + extension);
            try {
                ImdbCoverFetcher.downloadFile(absoluteImagePath, target);
            } catch (BusinessException e) {
                LOG.error("Could not download file from '"+absoluteImagePath+"' and save it to '"+target.getAbsolutePath()+"'!", e);
            }
            this.downloadedFile = target;
        }
    }
    
    private File downloadedFile = null;
    
    public boolean isCoverDownloaded() {
        return this.downloadedFile != null;
    }
    public File getDownloadedFile() {
        assert(this.isCoverDownloaded() == true);
        return this.downloadedFile;
    }
    
    static void downloadFile(String urlString, File target) throws BusinessException {
        LOG.info("Downloading file form url '"+urlString+"' to path '"+target.getAbsolutePath()+"'.");
        
        if(target.exists() == true) {
            throw new BusinessException("Target file '"+target.getAbsolutePath()+"' already exists!");
        }
        if(target.getParentFile().exists() == false || target.getParentFile().isDirectory() == false) {
            throw new BusinessException("Parent folder '"+target.getParent()+"' does not exist!");
        }
        
        URL realUrl;
        try {
            realUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new BusinessException("Invalid URL given '"+urlString+"'!", e);
        }
        HttpURLConnection http;
        try {
            http = (HttpURLConnection) realUrl.openConnection();
        } catch (IOException e) {
            throw new BusinessException("Could not open connection to server '"+realUrl.getHost()+"'!", e);
        }
        http.setUseCaches(false);
        http.setDoOutput(false);
        
        try {
            http.connect();
        } catch (IOException e) {
            throw new BusinessException("Could not connect to http-url '"+urlString+"'!");
        }
        
        BufferedInputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(http.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(target));

            byte[] buf = new byte[256];
            int n = 0;
            LOG.debug("Reading file and saving it locally...");
            while ((n=in.read(buf))>=0) {
               out.write(buf, 0, n);
            }
            
        } catch (IOException e) {
            throw new BusinessException("", e);
        } finally {
            if(in != null) try { in.close(); } catch(IOException ignore) {}
            if(out != null) try { out.flush(); out.close(); } catch(IOException ignore) {}
        }
        
        http.disconnect();
        LOG.debug("Download complete.");
    }
    
    
    
}
