package org.approvaltests.image;

import java.awt.image.BufferedImage;

import org.approvaltests.Approvals;
import org.approvaltests.core.ApprovalWriter;
import org.approvaltests.core.Options;

public class ImageApprovals {

	public static void verify(BufferedImage bufferedImage) {
		verify(bufferedImage, new Options());
	}

	public static void verify(BufferedImage bufferedImage, Options options) {
		ApprovalWriter writer = new ImageWriter(bufferedImage);
		ImageComparator comparator = new ImageComparator();
		Approvals.verify(writer, options.withComparator(comparator).withReporter(comparator));
	}

}
