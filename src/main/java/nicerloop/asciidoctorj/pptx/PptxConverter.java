package nicerloop.asciidoctorj.pptx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.asciidoctor.ast.Column;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.StructuralNode;
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
		if (node instanceof Column) {
			System.out.println(Column.class.getCanonicalName());
			System.out.println(node.toString());
		} else if (node instanceof PhraseNode) {
			System.out.println(PhraseNode.class.getCanonicalName());
			System.out.println(node.toString());
		} else if (node instanceof StructuralNode) {
			System.out.println(StructuralNode.class.getCanonicalName());
			System.out.println(node.toString());
		} else {
			System.err.println("Unknown node type");
			System.out.println(node.toString());
		}
	}

}
