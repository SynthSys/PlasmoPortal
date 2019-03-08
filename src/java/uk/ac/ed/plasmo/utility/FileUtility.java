package uk.ac.ed.plasmo.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class responsible for carrying out a number of file/file-system
 * related processes
 * @author ctindal
 *
 */
public class FileUtility {
	
	/**
	 * uploads a file to the file system
	 * @param newFileName
	 * @param fileToBeUploaded
	 * @return permanentFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File uploadFile(String newFileName, File fileToBeUploaded) throws FileNotFoundException, IOException {

		File permanentFile = new File(newFileName);
		boolean uploaded = fileToBeUploaded.renameTo(permanentFile);

		if(!uploaded) {

			InputStream in = new FileInputStream(fileToBeUploaded);
			OutputStream out = new FileOutputStream(permanentFile);
			byte [] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				synchronized(buf) {
					out.write(buf, 0, len);
				}
			}
			in.close();
			out.close();


			if(!permanentFile.exists()){
				throw new RuntimeException();
			}

		}
		return permanentFile;
	}
	
	/**
	 * checks whether a file exists in a particular file system location
	 * @param locationOfFile
	 * @return
	 */
	public static boolean fileExists(String locationOfFile)  {
		File f = new File(locationOfFile);
		return f.exists();
	}

	/**
	 * creates a directory on the file system determined by the value of
	 * <b>directoryName</b>
	 * @param directoryName the absolute path of the directory to be created
	 * @return <b>true</b> if the directory is created or if it already exists
	 */
	public static boolean createDirectory(String directoryName) {
		boolean exists = true;
		if(!new File(directoryName).exists()){
			exists = new File(directoryName).mkdir();
		}
		return exists;
	}
	
	/**
	 * deletes a specified directory (and children) from the file system
	 * @param dir the absolute path of the directory to be deleted
	 * @return <b>true</b> if the directory is deleted or <b>false</b> otherwise
	 */
	public static boolean deleteDirectory(File dir) {
		String files [] = dir.list();
		if(files != null && files.length > 0) {
			for(int i=0;i<files.length;i++) {
				File file = new File(dir, files[i]);
				if(file.isDirectory()) {
					deleteDirectory(file);
					file.delete();
				}
				else {
					file.delete();
				}
			}
		}
		boolean deleted = dir.delete();
		return deleted;
	}
	
	/**
	 * deletes a file from the file system
	 * @param file the file to be deleted
	 * @return <b>true</b> if the file is deleted or doesn't exist and <b>false</b> otherwise
	 */
	public static boolean deleteFile(File file) {
		boolean deleted = true;
		if(file.exists()) {
			deleted = file.delete();
		}
		return deleted;
	}
	
}
