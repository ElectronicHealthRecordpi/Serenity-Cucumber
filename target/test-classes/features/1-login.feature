@Login
Feature: CP01 - Validate login
  Background: Validate login with valid and invalid credentials
    Given the user navigates to the website

  @ValidCredentials
  Scenario: 1 - Validate with valid credentials
    When the user enters valid credentials
    Then the application should show the admin dashboard

  @InvalidCredentials
  Scenario: 2 - Validate with invalid credentials
    When the user enters invalid credentials
    Then the application should show an error message
