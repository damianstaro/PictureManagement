package picsmgmt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class Utils {
	
    private static final String SEP = System.getProperty("file.separator");
    
	public static void rotate(String path, int degrees) {
		try {

		    String imageMagick = Props.instance().getProperty("imageMagick");

		    String s = imageMagick +" -rotate " + degrees + " " + path + " " + path + "";
		    System.out.println(s);
		    Process runJob = Runtime.getRuntime().exec(s);
			
		    InputStream cmdStdErr = null;
		    InputStream cmdStdOut = null;

		    cmdStdErr = runJob.getErrorStream();
		    cmdStdOut = runJob.getInputStream();


		    String line;
		    BufferedReader stdOut = new BufferedReader (new InputStreamReader (cmdStdOut));
		    while ((line = stdOut.readLine ()) != null) {
		        System.out.println(line);

		    }
		    BufferedReader stdErr = new BufferedReader (new InputStreamReader (cmdStdErr));
		    while ((line = stdErr.readLine ()) != null) {
		        System.out.println(line);

		    }

		    cmdStdOut.close();
		    cmdStdErr.close();

			
			runJob.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rotate2(String path) {
		try {
		     BufferedImage image = ImageIO.read(new File(path));
			 
		     BufferedImage rotatedImage = new BufferedImage(image.getHeight(),
		                      image.getWidth(),image.getType());
		 
		     Graphics2D g2d = (Graphics2D) rotatedImage.getGraphics();
		 
		     g2d.rotate(Math.toRadians(90.0));
		 
		     g2d.drawImage(image, 0, -rotatedImage.getWidth(null), null);
		     g2d.dispose();

			// Write Image
			java.util.Iterator<ImageWriter> iter = ImageIO
					.getImageWritersByFormatName("jpeg");
			ImageWriter writer = (ImageWriter) iter.next();
			// instantiate an ImageWriteParam object with default compression
			// options
			ImageWriteParam iwp = writer.getDefaultWriteParam();

			FileImageOutputStream output = null;
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(0.98f); // an integer between 0 and 1
			// 1 specifies minimum compression and maximum quality

			File file = new File(path);
			output = new FileImageOutputStream(file);
			writer.setOutput(output);
			IIOImage iioImage = new IIOImage(rotatedImage, null, null);
			writer.write(null, iioImage, iwp);
			output.flush();
			output.close();
			writer.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /** retrieves all files in a dir and any subdirs */
    public static void getFiles(File dir, final List<PicFile> result) {
        dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    getFiles(pathname, result);
                } else {
                    System.out.println(pathname);
                    if (pathname.getName().toLowerCase().endsWith("jpg")) {
                    	PicFile pf = new PicFile();
                    	pf.setPath(pathname.getPath());
                    	pf.setDate(new Date(pathname.lastModified()));
                        result.add(pf);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    /** bundles files into a zip file */
    public static void bundleFiles(File picsDir, Collection<File> files, File toFile) {
        ZipOutputStream out = null;
        InputStream in = null;
        try {
            byte []buf = new byte[1024];

            // zip and encrypt
            out = new ZipOutputStream(new FileOutputStream(toFile));

            // Add all files to the bundle
            for (File file : files) {
                in = new BufferedInputStream(new FileInputStream(file));

                String entryName = file.getPath().substring(picsDir.getAbsolutePath().length());
                if (entryName.startsWith("/"))
                    entryName = entryName.substring(1);
                
                out.putNextEntry(new ZipEntry(entryName));

                int len;

                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);

                out.closeEntry();
                
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

}
