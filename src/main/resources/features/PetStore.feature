Feature: Pet Store API

Feature: Pet Store API

  Scenario Outline: Create a pet
    Given I have a pet with id "<id>", name "<name>", and status "<status>"
    When I create the pet
    Then the response status should be 200
    And the response should contain the pet details

    Examples:
      | id      | name    | status |
      | 210900  | sobaka  | alive  |
      | 210901  | cat     | alive  |
      | 210902  | squirrel   | alive  |

  Scenario Outline: Read a pet
    Given I have a pet with id "<id>"
    When I read the pet
    Then the response status should be 200
    And the response should contain the pet details

    Examples:
      | id     |
      | 210900 |
      | 210901 |
      | 210902 |

  Scenario Outline: Update a pet
    Given I have a pet with id "<id>", name "<name>", and status "<status>"
    When I update the pet with name "<updatedName>" and status "<updatedStatus>"
    Then the response status should be 200
    And the response should contain the updated pet details

    Examples:
      | id     | name   | status | updatedName   | updatedStatus |
      | 210900 | sobaka | alive  | updatedSobaka | barking |
      | 210901 | cat    | alive  | updatedCat    | sleeping |
      | 210902 | squirrel  | alive  | updatedSquirrel  | squeaking |

  Scenario Outline: Delete a pet
    Given I have a pet with id "<id>"
    When I delete the pet
    Then the response status should be 200
    And the pet should no longer exist

    Examples:
      | id     |
      | 210900 |
      | 210901 |
      | 210902 |