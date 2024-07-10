Feature: Feature: API to manage Tag Items

  Scenario: Retrieving all tags
    Given There are existing tags in the database
    When I request to retrieve all tags
    Then I should get a list of tags

  Scenario: Retrieving a tag by ID
    Given There is a tag with ID "1" in the database
    When I request to retrieve the tag with ID "1"
    Then I should get the tag details


  Scenario: Adding a new tag
    Given I have a new tag to add
    When I request to add the tag
    Then the tag should be saved successfully


  Scenario: Updating an existing tag
    Given There is an existing tag with ID "1" 
    When I request to update the tag with ID "1"
    Then the tag details should be updated successfully


  Scenario: Deleting an existing tag
    Given There is an existing tag with ID "1" to delete
    When I request to delete the tag with ID "1"
    Then the tag should be deleted successfully
  