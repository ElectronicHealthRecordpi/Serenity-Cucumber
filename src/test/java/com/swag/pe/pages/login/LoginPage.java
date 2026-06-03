package com.swag.pe.pages.login;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object that maps the login form of the application under test.
 * Selectors match the real frontend (Login.tsx) where the username field
 * has id="username", the password field has id="password", and the submit
 * button has no id but is identified by type="submit".
 */
public class LoginPage extends PageObject {

    // Username text input (id="username" in Login.tsx).
    @FindBy(id = "username")
    protected WebElementFacade txtUsername;

    // Password text input (id="password" in Login.tsx).
    @FindBy(id = "password")
    protected WebElementFacade txtPassword;

    // Submit button: the login form has no id on the button,
    // so we locate it by its type attribute (type="submit").
    @FindBy(xpath = "//button[@type='submit']")
    protected WebElementFacade btnLogin;

    /** Types the given username into the username field. */
    public void typeUsername(String username) {
        txtUsername.sendKeys(username);
    }

    /** Types the given password into the password field. */
    public void typePassword(String password) {
        txtPassword.sendKeys(password);
    }

    /** Clicks the login submit button to submit the form. */
    public void clickSubmit() {
        btnLogin.click();
    }
}
