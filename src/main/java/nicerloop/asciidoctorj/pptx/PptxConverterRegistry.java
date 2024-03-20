package nicerloop.asciidoctorj.pptx;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.jruby.converter.spi.ConverterRegistry;

public class PptxConverterRegistry implements ConverterRegistry {

	@Override
	public void register(Asciidoctor asciidoctor) {
		asciidoctor.javaConverterRegistry().register(PptxConverter.class);
	}

}
