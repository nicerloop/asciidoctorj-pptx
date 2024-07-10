package nicerloop.asciidoctorj.pptx.steps;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.parser.XMLParserException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

public class PptxSupport {

    private static void drawSlide(XSLFSlide slide, Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        slide.draw(graphics);
        graphics.dispose();
    }

    public static BufferedImage image(XSLFSlide slide) {
        Dimension size = slide.getSlideShow().getPageSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        drawSlide(slide, graphics);
        return image;
    }

    public static String svg(XSLFSlide slide) {
        SVGGraphics2D graphics = createSVGGraphics2D(true, true);
        drawSlide(slide, graphics);
        String svg = getSVGString(graphics);
        return canonicalize(svg);
    }

    private static SVGGraphics2D createSVGGraphics2D(boolean embeddedFonts, boolean textAsShapes) {
        try {
            DOMImplementation domImpl = DOMImplementationRegistry.newInstance().getDOMImplementation("XML");
            String svgNS = "http://www.w3.org/2000/svg";
            Document document = domImpl.createDocument(svgNS, "svg", null);
            SVGGeneratorContext context = SVGGeneratorContext.createDefault(document);
            context.setEmbeddedFontsOn(embeddedFonts);
            SVGGraphics2D graphics = new SVGGraphics2D(context, textAsShapes);
            return graphics;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSVGString(SVGGraphics2D graphics) {
        try {
            StringWriter writer = new StringWriter();
            graphics.stream(writer, true);
            return writer.toString();
        } catch (SVGGraphics2DIOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String canonicalize(String string) {
        try {
            org.apache.xml.security.Init.init();
            Canonicalizer canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
            System.setProperty("javax.xml.accessExternalDTD", "all");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            canon.canonicalize(string.getBytes(), baos, false);
            return new String(baos.toByteArray());
        } catch (InvalidCanonicalizerException | XMLParserException | CanonicalizationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
