package nicerloop.asciidoctorj.pptx;

import static nicerloop.asciidoctorj.pptx.LambdaExceptionUtil.rethrowFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PptxConverterSteps {

	private Path target = Paths.get("target");
	private Map<String, Path> tempFolders = new TreeMap<>();

	@Given("I have a temporary {string} folder")
	public void i_have_a_temporary_folder(String name) throws IOException {
		tempFolders.computeIfAbsent(name, rethrowFunction((String n) -> Files.createTempDirectory(target, n)));
	}

	@Given("I have an asciidoc document named {string} in the {string} folder containing")
	public void i_have_an_asciidoc_document_named_in_the_folder_containing(String name, String folder, String docString)
			throws IOException {
		Files.write(tempFolders.get(folder).resolve(name), Arrays.asList(docString.split("\n")));
	}

	@When("I convert {string}.{string} into {string}.{string}")
	public void i_convert_into(String input, String in_name, String output, String out_name) {
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		asciidoctor.convertFile(tempFolders.get(input).resolve(in_name).toFile(),
				Options.builder().backend("pptx").toFile(tempFolders.get(output).resolve(out_name).toFile()).build());
	}

	@Then("a {string} slideshow is present in the {string} folder")
	public void a_slideshow_is_present_in_the_folder(String file, String folder) throws IOException {
		try (XMLSlideShow pptx = new XMLSlideShow(Files.newInputStream(tempFolders.get(folder).resolve(file)))) {
			pptx.getSlides();
		}
		;
	}

}
