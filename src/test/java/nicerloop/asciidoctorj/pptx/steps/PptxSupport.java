package nicerloop.asciidoctorj.pptx.steps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PptxSupport {

    public static BufferedImage image(XSLFSlide slide) {
        Dimension size = slide.getSlideShow().getPageSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(Drawable.BUFFERED_IMAGE, new WeakReference<>(image));
        slide.draw(graphics);
        graphics.dispose();
        return image;
    }

}
