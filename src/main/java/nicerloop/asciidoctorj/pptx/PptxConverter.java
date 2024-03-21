package nicerloop.asciidoctorj.pptx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.ast.Title;
import org.asciidoctor.converter.AbstractConverter;
import org.asciidoctor.converter.ConverterFor;

@ConverterFor(PptxConverter.ID)
public class PptxConverter extends AbstractConverter<XMLSlideShow> {

	public static final String ID = "pptx";

	public PptxConverter(String backend, Map<String, Object> opts) {
		super(backend, opts);
		setOutfileSuffix(".pptx");
	}

	@Override
	public XMLSlideShow convert(ContentNode node, String transform, Map<Object, Object> opts) {
		XMLSlideShow pptx = new XMLSlideShow();
		convertNode(node, pptx);
		return pptx;
	}

	@Override
	public void write(XMLSlideShow output, OutputStream out) throws IOException {
		output.write(out);
	}

	private void convertNode(ContentNode node, XMLSlideShow pptx) {
		System.out.println("node " + node);
		if (node instanceof Document) {
			convertDocument((Document) node, pptx);
		} else {
			System.err.println("Ignored");
		}
	}

	private void convertDocument(Document document, XMLSlideShow pptx) {
		System.out.println("document " + document);
		Title title = document.getStructuredDoctitle();
		String mainTitle = title.getMain();
		String subtitle = title.getSubtitle();
		XSLFSlide slide = pptx.createSlide(pptx.findLayout("Title Slide"));
		fillPlaceholder(slide, Placeholder.CENTERED_TITLE, mainTitle);
		fillPlaceholder(slide, Placeholder.SUBTITLE, subtitle);
		for (StructuralNode block : document.getBlocks()) {
			convertStructuralNode((StructuralNode) block, pptx);
		}
	}
	
	private void fillPlaceholder(XSLFSlide slide, Placeholder placeholder, String text) {
		XSLFSimpleShape shape = slide.getPlaceholder(placeholder);
		if (shape != null && shape instanceof XSLFTextShape) {
			XSLFTextShape textShape = (XSLFTextShape) shape;
			textShape.clearText();
			textShape.appendText(text, false);
		}
	}

	private void convertStructuralNode(StructuralNode structuralNode, XMLSlideShow pptx) {
		System.out.println("structuralNode " + structuralNode);
		// TODO
		convertNode(structuralNode, pptx);
	}

}
