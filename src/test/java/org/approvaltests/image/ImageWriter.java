package org.approvaltests.image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.approvaltests.core.ApprovalWriter;

import com.spun.util.ObjectUtils;

public class ImageWriter implements ApprovalWriter {

	private final BufferedImage image;

	public ImageWriter(BufferedImage image) {
		this.image = image;
	}

	@Override
	public File writeReceivedFile(File received) {
		received.getParentFile().mkdirs();
		ObjectUtils.throwAsError(() -> ImageIO.write(image, "png", received));
		return received;
	}

	@Override
	public String getFileExtensionWithDot() {
		return ".png";
	}

}
