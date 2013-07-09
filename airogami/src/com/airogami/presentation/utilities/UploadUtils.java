package com.airogami.presentation.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.imgscalr.Scalr;

public class UploadUtils {
	private final static String folderPath = File.separator + "upload";
	private final static String rootPath = ServletActionContext
			.getServletContext().getRealPath(folderPath);
	private final static int width = 68;
	private final static int height = 68;
	private final static String name = "icon";

	public static String uploadImage(Long accountId, File file,
			String fileName, String contentType){

		try {
			String ext = getFileExtension(fileName);
			BufferedImage img = ImageIO.read(file);
			BufferedImage croppedBufferedImage = Scalr.resize(img, Scalr.Mode.FIT_EXACT, width, height);
			ImageIO.write(croppedBufferedImage, ext, getUploadedImageFile(accountId,ext));
		} catch (IOException ioe) {
			return null;
		}

		return name;
	}

	private static File getUploadedImageFile(Long accountId, String ext) {
		File userFolder = new File(rootPath + File.separator + accountId);
		if (!userFolder.exists())
			userFolder.mkdir();
		return new File(userFolder, name + '.' + ext);
	}

	public static String getFileExtension(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		return extension;
	}
}
