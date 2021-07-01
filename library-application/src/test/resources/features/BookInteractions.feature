Feature: Interacting with library books
  Reading, borrowing and downloading books

  Scenario: User tries to borrow book with available copy
    Given user logged in
    And there is available copy
    When user tries to borrow book
    Then book should be handed to user

  Scenario: User tries to borrow book with no available copy
    Given user logged in
    And there is no available copy
    When user tries to borrow book
    Then borrow request status should be unsuccessful

  Scenario: User tries to borrow a book where a copy is reserved for him
    Given user logged in
    And there is a copy locked for the user
    When user tries to borrow book
    Then book should be handed to user

  Scenario: User tries to postpone book return date with allowed amount
    Given user logged in
    And user has borrowed book
    When user tries to postpone book
    Then postponement is successful

  Scenario: Postpone request with more than the maximum days allowed is made
    Given user logged in
    And user has borrowed book
    When user tries to postpone book for more than the days allowed
    Then postponement declined

  Scenario: read online book request is made
    Given user logged in
    When read online book request is made
    Then book should be added to the users used books

  Scenario: Download e-book request is made for book with download link present
    Given user logged in
    And e-book with download link is present
    When user tries to download book
    Then book should be added to the users downloaded books

  Scenario: Download e-book request i made for book without download link present
    Given user logged in
    And e-book without download link is present
    When user tries to download book with no download url
    Then download is unsuccessful