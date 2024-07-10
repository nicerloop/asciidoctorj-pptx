Feature: Layouts

  Background: 
    Given I have an empty temporary asciidoc document

  Scenario: All layouts have a test
    Given the asciidoc document has a complete test case
    When I convert the asciidoc document to an Office Open XML slideshow
    Then the slideshow uses all layouts
    And each layout has all placeholders filled
