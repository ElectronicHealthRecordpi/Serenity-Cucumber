package com.swag.pe.steps.validation;

import com.swag.pe.pages.validations.ValidationPage;
import net.serenitybdd.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class ValidationStep extends ValidationPage {

    private static final Logger log = LoggerFactory.getLogger(ValidationStep.class);

    @Step("Validate that the admin dashboard title is visible")
    public Boolean dashboardTitleIsVisible() {
        try {
            lblDashboardTitle.waitUntilVisible().withTimeoutOf(Duration.ofSeconds(10));
            return lblDashboardTitle.isCurrentlyVisible();
        } catch (Exception e) {
            String url = getDriver().getCurrentUrl();
            log.warn("Dashboard NOT found. Current URL: {}", url);
            try {
                var h1s = getDriver().findElements(org.openqa.selenium.By.tagName("h1"));
                log.warn("Page has {} H1 elements:", h1s.size());
                for (var h1 : h1s) {
                    log.warn("  H1: '{}'", h1.getText());
                }
                log.warn("Page source: {}", getDriver().getPageSource().substring(0, Math.min(2000, getDriver().getPageSource().length())));
            } catch (Exception inner) {
                log.warn("Could not retrieve page info: {}", inner.getMessage());
            }
            return false;
        }
    }

    @Step("Validate that the login error message is displayed")
    public Boolean errorMessageIsDisplayed() {
        try {
            lblErrorMessage.waitUntilVisible().withTimeoutOf(Duration.ofSeconds(10));
            return lblErrorMessage.isCurrentlyVisible();
        } catch (Exception e) {
            log.warn("Error message NOT found. Current URL: {}", getDriver().getCurrentUrl());
            return false;
        }
    }
}
