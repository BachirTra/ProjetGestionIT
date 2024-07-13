Feature: API to manage Tag Items

  @REFEPTGITDIC12024-00020
  Scenario Outline: Find tag by id should return correct entity
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
      | 2  | tag name 2  | description 2   |
    When call find tag by id with id="<id>"
    Then the returned http status is 200
    And the returned tag has properties name="<name>" and description="<description>"
    Examples:
      | id | name       | description   |
      | 1  | tag name 1 | description 1 |

  @REFEPTGITDIC12024-00021
  Scenario: Find all tags should return correct list
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
      | 2  | tag name 2  | description 2   |
    When call find all tags with page = 0 and size = 10 and sort="name,asc"
    Then the returned http status is 200
    And the returned list has 2 elements
    And that list contains values:
      | name       | description   |
      | tag name 1 | description 1 |
      | tag name 2 | description 2 |

  @REFEPTGITDIC12024-00022
  Scenario Template: Add tag should return 201
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
    And name = "<name>"
    And description = "<description>"
    When call add tag
    Then the returned http status is 201
    And the created tag has properties name="<name>" and description="<description>"
    Examples:
      | name       | description   |
      | tag name 3 | description 3 |

  @REFEPTGITDIC12024-00023
  Scenario Outline: Update an existing tag should return 202
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
    And name = "<name>"
    And description = "<description>"
    When call update tag with id="<id>"
    Then the returned http status is 202
    And the updated tag has properties name="<name>" and description="<description>"
    Examples:
      | id | name           | description     |
      | 1  | updated name 1 | description 1.1 |

  @REFEPTGITDIC12024-00024
  Scenario: Delete an existing tag should return 204
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
      | 2  | tag name 2  | description 2   |
    When call delete tag with id="1"
    Then the returned http status is 204

  @REFEPTGITDIC12024-00025
  Scenario: Find tag by id with a non-existing id should return 404
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
    When call find tag by id with id="2"
    Then the returned http status is 404

  @REFEPTGITDIC12024-00026
  Scenario: Add tag with an existing name should return 409
    Given acicd_tags table contains data:
      | id | name        | description     |
      | 1  | tag name 1  | description 1   |
    When name = "tag name 1"
    And description = "new description"
    When call add tag
    Then the returned http status is 409