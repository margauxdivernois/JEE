/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import entities.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Margaux
 */
public class ImagesTools {

    public static void readMetaData(String urlString, String pictureName, Image image)
    {  
        System.out.println("READ IMAGE START !!!");
        try {
            
            String pictureAddress = "D:"+File.separator+"Cours"+File.separator+"Java Entreprise Edition"+File.separator+"Projets"+File.separator+"Pix"+File.separator+"Balloon"+File.separator+"web"+File.separator+
                    "resources"+File.separator+"image"+File.separator; 
            
            pictureAddress += pictureName;
            pictureAddress += ".jpg";
            
            String pictureLongName = pictureName;
            pictureLongName += ".jpg";
            
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(pictureAddress);
            
            byte[] b = new byte[2048];
            int length;
            
            while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

            is.close();
            os.close();

            File file = new File(pictureAddress);
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            /*for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }*/
            
            ExifSubIFDDirectory directorySub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            ExifIFD0Directory directory0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            
            System.out.println("CAMERA IMAGE TAG "+directory0.getString(ExifIFD0Directory.TAG_MODEL));
            System.out.println("COPYRIGHT IMAGE TAG "+directorySub.getString(ExifSubIFDDirectory.TAG_COPYRIGHT));
           
            Date date = directorySub.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if(date != null)
            {
                image.setIdateCapture(date);
            }
            
            String camera = directory0.getString(ExifIFD0Directory.TAG_MODEL);
            if(camera != null)
            {
                image.setICamera(camera);
            }
            
            String copyright = directorySub.getString(ExifSubIFDDirectory.TAG_COPYRIGHT);
            if(copyright != null)
            {
                image.setICopyright(copyright);
            }
            
            image.setIFilename(pictureLongName);
        }
        catch (Exception e) {
            System.out.println("READ IMAGE EXCEPTION !!!");
            e.printStackTrace();
        }
        System.out.println("READ IMAGE END !!!");
    }
}
