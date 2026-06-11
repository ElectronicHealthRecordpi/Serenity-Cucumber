package com.swag.pe.steps.validation;

import com.swag.pe.pages.validations.ValidationPage;
import net.serenitybdd.annotations.Step;

/**
 * Step Library that wraps the assertions executed after a login attempt.
 * Each method is annotated with @Step so Serenity renders a readable
 * description in the generated report.
 */
public class ValidationStep extends ValidationPage {
    @Step("Validate that the admin dashboard title is visible")
    public Boolean dashboardTitleIsVisible() {
        try {
            lblDashboardTitle.waitUntilVisible().withTimeoutOf(Duration.ofSeconds(10));
            return lblDashboardTitle.isCurrentlyVisible();
        } catch (Exception e) {
            System.out.println("Dashboard NOT found. Current URL: "
                    + getDriver().getCurrentUrl());
            System.out.println("Page source snippet: "
                    + getDriver().getPageSource().substring(0, 500));
            return false;
        }
    }

    @Step("Validate that the login error message is displayed")
    public Boolean errorMessageIsDisplayed() {
        try {
            lblErrorMessage.waitUntilVisible().withTimeoutOf(Duration.ofSeconds(10));
            return lblErrorMessage.isCurrentlyVisible();
        } catch (Exception e) {
            System.out.println("Error message NOT found. Current URL: "
                    + getDriver().getCurrentUrl());
            return false;
        }
    }
}
