Feature: Create a new slideshow

  Background: 
    Given I have a temporary "input" folder
    And I have a temporary "output" folder

  Scenario: 
    Given I have an asciidoc document named "input.adoc" in the "input" folder containing
      """
      = Title Slide
      """
    When I convert "input"."input.adoc" into "output"."output.pptx"
    Then a "output.pptx" slideshow is present in the "output" folder
