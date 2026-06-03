package com.swag.pe.steps.login;

import com.swag.pe.pages.login.LoginPage;
import net.serenitybdd.annotations.Step;

/**
 * Step Library that exposes business-level actions for the login flow.
 * Each method is annotated with @Step so Serenity displays a readable
 * description in the generated report.
 */
public class LoginStep extends LoginPage {

    /** Types the given value into the username field. */
    @Step("Enter username")
    public void enterUsername(String username) {
        typeUsername(username);
    }

    /** Types the given value into the password field. */
    @Step("Enter password")
    public void enterPassword(String password) {
        typePassword(password);
    }

    /** Clicks the submit button to send the login form. */
    @Step("Click on the login button")
    public void clickLogin() {
        clickSubmit();
    }
}
