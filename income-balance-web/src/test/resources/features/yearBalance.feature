Feature: YearBalance lifecycle

  Scenario: Initial yearBalance list is empty
    Given there are 0 yearBalances

  Scenario: Add yearBalance
    When a yearBalance year 2021 is added
    Then yearBalance year 2021 has 0 bankBalances
    And yearBalance year 2021 has 0 monthBalances
    And yearBalance year 2021 has savings "NA", salary "0.00", result "0.00"
    And there are 1 yearBalances

  Scenario: Put yearBalance
    When Replace yearBalance year 2021 with yearBalance year 2020
    Then yearBalance year 2021 not exist
    Then yearBalance year 2020 exists
    And there are 1 yearBalances