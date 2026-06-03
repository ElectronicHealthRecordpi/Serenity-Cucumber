package com.swag.pe.steps.validation;

import com.swag.pe.pages.validations.ValidationPage;
import net.serenitybdd.annotations.Step;

/**
 * Step Library that wraps the assertions executed after a login attempt.
 * Each method is annotated with @Step so Serenity renders a readable
 * description in the generated report.
 */
public class ValidationStep extends ValidationPage {

    /** Returns true when the admin dashboard title is visible. */
    @Step("Validate that the admin dashboard title is visible")
    public Boolean dashboardTitleIsVisible() {
        lblDashboardTitle.waitUntilVisible();
        return lblDashboardTitle.isCurrentlyVisible();
    }

    /** Returns true when the login error banner is visible. */
    @Step("Validate that the login error message is displayed")
    public Boolean errorMessageIsDisplayed() {
        lblErrorMessage.waitUntilVisible();
        return lblErrorMessage.isCurrentlyVisible();
    }
}
