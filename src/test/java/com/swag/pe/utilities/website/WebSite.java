package com.swag.pe.utilities.website;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.annotations.Steps;
import net.thucydides.core.pages.PageObject;

/**
 * Utility class used to navigate the application under test.
 * The base URL is configured in serenity.properties (webdriver.base.url)
 * and the frontend is expected to run locally on the host machine
 * (reachable from Jenkins as host.docker.internal).
 */
public class WebSite {

    // Shared PageObject instance injected by Serenity to access the WebDriver.
    @Steps
    PageObject swag;

    /**
     * Opens the given relative path on the configured base URL.
     * Example: navigateTo("/auth/login") -> opens http://host.docker.internal:5173/auth/login
     *
     * @param path relative path that will be appended to the base URL
     */
    @Step("Navigate to the website")
    public void navigateTo(String path) {
        // open(path) resolves the path against the default base URL
        // configured in serenity.properties (webdriver.base.url).
        swag.open(path);
    }
}
