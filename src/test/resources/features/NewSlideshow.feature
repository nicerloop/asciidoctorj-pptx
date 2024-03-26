Feature: Create a new slideshow

  Background: 
    Given I have a temporary "input" folder
    And I have a temporary "output" folder

  Scenario: Title
    Given I have an asciidoc document named "input.adoc" in the "input" folder containing
      """
= Main Title: Subtitle
      """
    When I convert "input"."input.adoc" into "output"."output.pptx"
    Then a "output.pptx" slideshow is present in the "output" folder
    And the slideshow has 1 slide
    And the slides look like "expected"
