package com.swag.pe.steps.validation;

import com.swag.pe.pages.validations.ValidationPage;
import net.serenitybdd.annotations.Step;
import java.time.Duration;

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
            System.out.println("Dashboard NOT found. URL: " + getDriver().getCurrentUrl());
            System.out.println("H1s en página: ");
            getDriver().findElements(org.openqa.selenium.By.tagName("h1"))
                    .forEach(h -> System.out.println("  H1: '" + h.getText() + "'"));
            return false;
        }
    }

    @Step("Validate that the login error message is displayed")
    public Boolean errorMessageIsDisplayed() {
        try {
            lblErrorMessage.waitUntilVisible().withTimeoutOf(Duration.ofSeconds(10));
            return lblErrorMessage.isCurrentlyVisible();
        } catch (Exception e) {
            System.out.println("Error message NOT found. URL: " + getDriver().getCurrentUrl());
            return false;
        }
    }
}
