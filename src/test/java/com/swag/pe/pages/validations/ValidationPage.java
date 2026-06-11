package com.swag.pe.pages.validations;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object that maps the elements used to assert the result of a login
 * attempt.
 *
 * - On a successful admin login, the application redirects to /admin/home
 * which renders an
 * <h1>with the text "Panel de administracion".
 * - On a failed login, the form shows an error message inside a
 * <div role="alert"> element.
 */
public class ValidationPage extends PageObject {

    // H1 title rendered on the admin dashboard after a successful admin login.

    // @FindBy(xpath = "//h1[contains(text(),'Panel de administracion')]")
    // protected WebElementFacade lblDashboardTitle;
    // @FindBy(xpath = "//h1[normalize-space()='Panel de administracion']")
    @FindBy(xpath = "//h1[text()='Panel de administracion']")
    protected WebElementFacade lblDashboardTitle;

    // Error banner displayed by the login form when credentials are invalid.
    @FindBy(css = "div[role='alert']")
    protected WebElementFacade lblErrorMessage;
}
