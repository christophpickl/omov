package at.phudy;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// http://commons.apache.org/fileupload/using.html
// http://www.itavenue.de/java/einfacher-dateiupload-per-servlet/
public class UploadServlet extends HttpServlet {

	private static final Log LOG = LogFactory.getLog(UploadServlet.class);

	private static final long serialVersionUID = -7887682452673965747L;

	private File uploadFolder;
	private File uploadTempFolder;

	private static final String UPLOAD_TEMP_FOLDER_NAME = "upload-temp";
	
	private static final String UPLOAD_FOLDER_NAME = "upload";

	private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 5; // 5MB
	private static final int MAX_UPLOAD_FILE_SIZE = 1024 * 1024 * 5; // 5MB
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		LOG.info("doPost() invoked");
		
		if(ServletFileUpload.isMultipartContent(req) == false) {
			LOG.warn("Method doPost() got non multipart content!");
			return;
		}

		if(this.uploadFolder == null) {
			this.uploadFolder = createFolder(this.getServletContext(), UPLOAD_FOLDER_NAME);
		}
		if(this.uploadTempFolder == null) {
			this.uploadTempFolder = createFolder(this.getServletContext(), UPLOAD_TEMP_FOLDER_NAME);
		}
		
		
		final DiskFileItemFactory factory = new DiskFileItemFactory(MAX_MEMORY_SIZE, this.uploadTempFolder);
		final ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(MAX_UPLOAD_FILE_SIZE);
		
		try {
			final List<FileItem> items = upload.parseRequest(req);
			for (final FileItem item : items) {

			    if(item.isFormField() == true) {
			    	LOG.warn("FileItem [" + item.getFieldName() + "] seems to come from a form field!");
			        continue;
			    }
			    
//			    final String uploadFileName = item.getName();
			    final File targetFile = new File(this.uploadFolder, "tut.txt");
			    if(targetFile.exists() == true) {
			    	
			    }
			    LOG.debug("Saving upload file to [" + targetFile.getAbsolutePath() + "].");
			    item.write(targetFile);
			    
			}
			
		} catch (Exception e) {
			LOG.error("Could not process post data!", e);
			throw new RuntimeException("File upload failed!", e);
		}
		
		LOG.debug("Handling file upload finished.");
	}
	
	private static final File createFolder(final ServletContext context, final String folderName) {
		final File folder = new File(context.getRealPath(folderName));
		
		if(folder.exists() == false) {
			LOG.debug("Creating folder [" +  folder.getAbsolutePath() + "].");
			final boolean folderCreated = folder.mkdir();
			if(folderCreated == false) {
				throw new RuntimeException("Could not create folder [" + folder.getAbsolutePath() + "]!");
			}
		}
		
		return folder;
	}
	
}
