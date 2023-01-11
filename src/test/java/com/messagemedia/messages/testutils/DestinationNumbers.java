package com.messagemedia.messages.testutils;

import org.testng.SkipException;

import java.util.Properties;

/*
    Represents a set of Phone Numbers to deliver Messages to.

    Optionally, it will load in any dev.local numbers configured in the test/resources/ folder,
    however can be configured with different numbers at runtime if required.
 */
public class DestinationNumbers {

    static final String devLocalPropertiesResourceFilename = "testConfig/phone-numbers.dev.local.properties";

    private Properties resourceProps = new Properties();

    private String[] validPhoneNumbers = new String[] {};

    public DestinationNumbers() {
    }

    public static DestinationNumbers fromResources() {
        return new DestinationNumbers().loadFromResources();
    }

    private DestinationNumbers loadFromResources() {
        try {
            this.resourceProps = Io.getResourceProperties(devLocalPropertiesResourceFilename);
        } catch (RuntimeException ignored) { }

        if (!resourceProps.isEmpty()) {
            String resourceNumbersString = this.resourceProps.getProperty("destNumbers.valid");
            if (resourceNumbersString != null) {
                this.validPhoneNumbers = resourceNumbersString.split("\\|");
            }
        }
        return this;
    }

    public boolean validate() {
        return this.validPhoneNumbers.length != 0;
    }

    public boolean isLoadedFromResources() {
        return !resourceProps.isEmpty();
    }


    public void skipTestIfNotConfigured() {
        if (!this.validate()) {
            throw new SkipException("destination Phone Numbers not configured");
        }
    }

    public String[] getValidPhoneNumbers() {
        return validPhoneNumbers;
    }

    public DestinationNumbers setValidPhoneNumbers(String[] validPhoneNumbers) {
        this.validPhoneNumbers = validPhoneNumbers;
        return this;
    }
}
