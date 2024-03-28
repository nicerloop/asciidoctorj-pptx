package nicerloop.asciidoctorj.pptx;

import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PptxSlideLayoutDump {

	public static void main(String[] args) throws IOException {
		try (XMLSlideShow pptx = new XMLSlideShow()) {
			for (XSLFSlideMaster master : pptx.getSlideMasters()) {
				log("master");
				enter();
				extracted(master);
				for (XSLFSlideLayout layout : master.getSlideLayouts()) {
					log(layout.getName());
					enter();
					log(layout.getType());
					log(layout.getFollowMasterGraphics());
					for (XSLFTextShape shape : layout.getPlaceholders()) {
						log(shape.getPlaceholder());
					}
					exit();
				}
			}
		}
	}

	private static void extracted(XSLFSlideMaster master) {
		log(master.getFollowMasterGraphics());
		for (XSLFTextShape shape : master.getPlaceholders()) {
			log(shape.getPlaceholder());
		}
	}
	
	private static String margin = "";
	
	private static void log(Object message) {
		System.out.println(margin + message.toString());
	}

	private static void enter() {
		margin += "  ";
	}

	private static void exit() {
		margin = margin.substring(2);
	}

}
