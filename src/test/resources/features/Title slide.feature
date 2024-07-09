Feature: Title slide

    Background:
        Given I have an empty temporary asciidoc document

    Scenario: Title
        Given the asciidoc document has a title
        When I convert the asciidoc document to an Office Open XML slideshow
        Then the slideshow has 1 slide
        And the slide looks like expected

    Scenario: Title and subtitle
        Given the asciidoc document has a title and a subtitle
        When I convert the asciidoc document to an Office Open XML slideshow
        Then the slideshow has 1 slide
        And the slide looks like expected

    # Scenario: Title and author
    #     Given the asciidoc document has a title
    #     And the asciidoc document has an author
    #     When I convert the asciidoc document to an Office Open XML slideshow
    #     Then the slideshow has 1 slide
    #     And the slide looks like expected

    # Scenario: Title and date
    #     Given the asciidoc document has a title
    #     And the asciidoc document has a version and a date
    #     When I convert the asciidoc document to an Office Open XML slideshow
    #     Then the slideshow has 1 slide
    #     And the slide looks like expected

    # Scenario: Title, subtitle, author and date
    #     Given the asciidoc document has a title and a subtitle
    #     And the asciidoc document has an author and a date
    #     When I convert the asciidoc document to an Office Open XML slideshow
    #     Then the slideshow has 1 slide
    #     And the slide looks like expected
