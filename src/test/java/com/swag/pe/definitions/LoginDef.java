package com.swag.pe.definitions;

import com.swag.pe.steps.login.LoginStep;
import com.swag.pe.steps.validation.ValidationStep;
import com.swag.pe.utilities.website.WebSite;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

public class LoginDef {
    @Steps
    WebSite url;
    @Steps
    LoginStep login;
    @Steps
    ValidationStep validate;

    @Given("The user navigate to the login page")
    public void userNavigateTo() {
        // url to mi website
        // http://localhost:5173/auth/login
        url.navigateTo("// http://localhost:5173//auth/login");
    }

    @When("the user (admin) should log in with valid credentials")
    public void userLoginWthValidCredentials() {
        login.typeUsername("admin");
        login.typePassword("Admin1234!");
        login.clickLogin();
    }

    @Then("The application should show the dashboard page ")
    public void systemShowProductModule() {
        Assert.assertTrue(validate.titleIsVisible());
    }

    @When("the user (admin) should log in with invalid credentials")
    public void userLoginWthInvalidCredentials() {
        login.typeUsername("admin");
        login.typePassword("InvalidPassword!");
        login.clickLogin();
    }

    @Then("The application should show an error message")
    public void systemShowErrorMessage() {
        Assert.assertTrue(validate.errorMessageIsDisplayed());
    }

}
