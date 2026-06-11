package com.swag.pe.definitions;

import com.swag.pe.steps.login.LoginStep;
import com.swag.pe.steps.validation.ValidationStep;
import com.swag.pe.utilities.website.WebSite;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

/**
 * Glue code that binds the Gherkin steps in 1-login.feature to the
 * underlying Serenity Step Libraries (LoginStep, ValidationStep) and
 * navigation utility (WebSite).
 *
 * The credentials used here match the "admin" account that is created
 * in the auth-db (MongoDB) when the stack is started.
 */
public class LoginDef {

    @Steps
    WebSite url;

    @Steps
    LoginStep login;

    @Steps
    ValidationStep validate;

    /** Opens the login page of the application under test. */
    @Given("the user navigates to the website")
    public void userNavigatesTo() {
        // http://localhost:5173/auth/login
        url.navigateTo("http://localhost:5173/auth/login");
    }

    /** Logs in with the seeded admin credentials. */
    @When("the user enters valid credentials")
    public void userEntersValidCredentials() {
        // debug("Using credentials: admin / Admin1234!");
        System.out.println("Using credentials: admin / Admin1234!");
        login.enterUsername("admin");
        login.enterPassword("Admin1234!");
        System.out.println("Credentials entered and login button clicked");
        login.clickLogin();
    }

    /** Asserts that the admin dashboard is visible after a successful login. */
    @Then("the application should show the admin dashboard")
    public void systemShowsAdminDashboard() {
        System.out.println("Validating that the admin dashboard title is visible");
        System.out.println("Dashboard title visible: " + validate.dashboardTitleIsVisible());
        Assert.assertTrue("Admin dashboard title was not visible after login",
                validate.dashboardTitleIsVisible());
    }

    /** Logs in with a valid username but an incorrect password. */
    @When("the user enters invalid credentials")
    public void userEntersInvalidCredentials() {
        login.enterUsername("admin");
        login.enterPassword("InvalidPassword!");
        login.clickLogin();
    }

    /** Asserts that the login error banner is displayed after a failed attempt. */
    @Then("the application should show an error message")
    public void systemShowsErrorMessage() {
        Assert.assertTrue("Login error message was not displayed",
                validate.errorMessageIsDisplayed());
    }
}
