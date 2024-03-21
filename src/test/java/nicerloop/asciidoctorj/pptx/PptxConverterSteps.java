package nicerloop.asciidoctorj.pptx;

import static nicerloop.asciidoctorj.pptx.LambdaExceptionUtil.rethrowFunction;
import static org.approvaltests.awt.AwtApprovals.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.approvaltests.Approvals;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.GetApprovalName;
import org.approvaltests.namer.GetSourceFilePath;
import org.approvaltests.namer.NamerWrapper;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PptxConverterSteps implements GetApprovalName, GetSourceFilePath {

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

	private XMLSlideShow pptx = null;

	@Then("a {string} slideshow is present in the {string} folder")
	public void a_slideshow_is_present_in_the_folder(String file, String folder) throws IOException {
		pptx = new XMLSlideShow(Files.newInputStream(tempFolders.get(folder).resolve(file)));
	}

	@After
	public void closeSlideShow() throws IOException {
		pptx.close();
		pptx = null;
	}

	@Then("the slideshow has {int} slide(s)")
	public void the_slideshow_has_slide(Integer count) {
		assertThat(pptx.getSlides().size()).isEqualTo(count);
	}

	@Then("the slides look like {string}")
	public void the_slides_look_like(String expected) throws IOException {
		Dimension size = pptx.getPageSize();
		for (XSLFSlide slide : pptx.getSlides()) {
			Approvals.namerCreater = () -> new NamerWrapper(this, this) {
				@Override
				public ApprovalNamer addAdditionalInformation(String info) {
					return this;
				}
			};
			verify(image(slide, size));
		}
	}

	@Before
	public void setupApprovalsNamer(Scenario scenario) {
		sourceFilePath = scenario.getUri().getPath();
		approvalName = "." + scenario.getName();
		if (approvalName == null || approvalName.isEmpty()) {
			approvalName = scenario.getLine().toString();
		}
		approvalName = sourceFilePath.substring(sourceFilePath.lastIndexOf('/') + 1) + approvalName;
		sourceFilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('/'));
	}

	private String sourceFilePath = null;
	private String approvalName = null;

	@Override
	public String getSourceFilePath() {
		return sourceFilePath;
	}

	@Override
	public String getApprovalName() {
		return approvalName;
	}

	private BufferedImage image(XSLFSlide slide, Dimension size) {
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		slide.draw(graphics);
		graphics.dispose();
		return image;
	}

}
