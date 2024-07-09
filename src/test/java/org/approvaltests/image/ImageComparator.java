package org.approvaltests.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import org.approvaltests.core.ApprovalFailureReporter;
import org.approvaltests.core.ApprovalReporterWithCleanUp;
import org.approvaltests.core.VerifyResult;
import org.lambda.functions.Function2;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

public class ImageComparator
		implements Function2<File, File, VerifyResult>, ApprovalFailureReporter, ApprovalReporterWithCleanUp {

	private ImageComparisonResult imageComparisonResult;
	private File compared;

	public VerifyResult call(File received, File approved) {
		try {
			if (approved.canRead() && received.canRead()) {
				BufferedImage approvedImage = ImageIO.read(approved);
				BufferedImage receivedImage = ImageIO.read(received);
				ImageComparison imageComparison = new ImageComparison(approvedImage, receivedImage);
				imageComparisonResult = imageComparison.compareImages();
				compared = new File(received.toString().toString() + ".diff" + new ImageWriter(null).getFileExtensionWithDot());
				compared.delete();
				if (imageComparisonResult.getImageComparisonState() == ImageComparisonState.MATCH) {
					return VerifyResult.SUCCESS;
				}
			}
			return VerifyResult.FAILURE;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public boolean report(String received, String approved) {
		if (imageComparisonResult != null) {
			new ImageWriter(imageComparisonResult.getResult()).writeReceivedFile(compared);
		}
		return true;
	}

	@Override
	public void cleanUp(String received, String approved) {
		compared.delete();
		compared.getParentFile().delete();
	}

}
