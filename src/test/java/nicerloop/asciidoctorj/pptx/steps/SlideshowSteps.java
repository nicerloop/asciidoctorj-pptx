package nicerloop.asciidoctorj.pptx.steps;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.approvaltests.Approvals;
import org.approvaltests.image.ImageApprovals;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.assertj.core.api.Assertions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SlideshowSteps {

    private static final String ASCIIDOC_PPTX_TESTS = "asciidoc-pptx-tests-";
    private Path tempFolder;
    private File asciidocFile;
    private File pptxFile;
    private XMLSlideShow pptx;

    @Before
    public void createTempFolder() throws IOException {
        tempFolder = Files.createTempDirectory(Paths.get("target"), ASCIIDOC_PPTX_TESTS);
    }

    @Given("I have an empty temporary asciidoc document")
    public void i_have_an_empty_temporary_asciidoc_document() throws IOException {
        asciidocFile = createTempFile(tempFolder, ASCIIDOC_PPTX_TESTS, ".adoc").toFile();
    }

    @Given("the asciidoc document has a title")
    public void the_asciidoc_document_has_a_title() {
        appendToAsciidoc("= Title");
    }

    private Path appendToAsciidoc(CharSequence... lines) {
        try {
            return write(asciidocFile.toPath(), asList(lines), APPEND, CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Given("the asciidoc document has a title and a subtitle")
    public void the_asciidoc_document_has_a_title_and_a_subtitle() {
        appendToAsciidoc("= Title: Subtitle");
    }

    @When("I convert the asciidoc document to an Office Open XML slideshow")
    public void i_convert_the_asciidoc_document_to_an_office_open_xml_slideshow()
            throws FileNotFoundException, IOException {
        pptxFile = createTempFile(tempFolder, ASCIIDOC_PPTX_TESTS, ".pptx").toFile();
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convertFile(asciidocFile, Options.builder().backend("pptx").toFile(pptxFile).build());
        pptx = new XMLSlideShow(new FileInputStream(pptxFile));
    }

    @Then("the slideshow has {int} slide")
    public void the_slideshow_has_slide(Integer expectedSlideCount) {
        assertThat(pptx.getSlides()).hasSize(expectedSlideCount);
    }

    @Then("the slide looks like expected")
    public void the_slide_looks_like_expected() {
        pptx.getSlides().stream().map(PptxSupport::image).forEach(ImageApprovals::verify);
    }

    @Given("the asciidoc document has a complete test case")
    public void the_asciidoc_document_has_a_complete_test_case() {
        appendToAsciidoc("= Title");
    }

    @Then("the slideshow uses all layouts")
    public void the_slideshow_uses_all_layouts() {
        List<XSLFSlideLayout> masterLayouts = pptx.getSlideMasters().stream() //
                .flatMap(master -> stream(master.getSlideLayouts())).collect(toList());
        List<XSLFSlideLayout> slideLayouts = pptx.getSlides().stream() //
                .map(slide -> slide.getSlideLayout()).collect(toList());
        Assertions.assertThat(slideLayouts).containsAll(masterLayouts);
    }

    @Then("each layout has all placeholders filled")
    public void each_layout_has_all_placeholders_filled() {
        pptx.getSlides().stream().forEach(slide -> stream(slide.getPlaceholders())
                .forEach(placeholder -> assertThat(placeholder.getText()).isNotEmpty()));

    }

}
