package com.messagemedia.messages.testutils;

import org.testng.SkipException;

import java.util.Properties;

/*
    Represents a set of MessageMedia API Credentials.

    Optionally, it will load in any dev.local credentials configured in the test/resources/ folder,
    however can be configured with different credentials at runtime if required.
 */
public class ApiCredentials {

    static final String devLocalPropertiesResourceFilename = "testConfig/credentials.dev.local.properties";

    private Properties resourceProps = new Properties();

    private String username;
    private String password;
    private boolean useHmacAuth;

    public ApiCredentials() {
    }

    public static ApiCredentials fromResources() {
        return new ApiCredentials().loadFromResources();
    }

    private ApiCredentials loadFromResources() {
        try {
            this.resourceProps = Io.getResourceProperties(devLocalPropertiesResourceFilename);
        } catch (RuntimeException ignored) { }

        if (!resourceProps.isEmpty()) {
            this.username = this.resourceProps.getProperty("api.username");
            this.password = this.resourceProps.getProperty("api.password");
            this.useHmacAuth = Boolean.parseBoolean(this.resourceProps.getProperty("api.useHmacAuth"));
        }
        return this;
    }

    public boolean validate() {
        return this.username != null
            && this.password != null;
    }

    public boolean isLoadedFromResources() {
        return !resourceProps.isEmpty();
    }

    public String getUsername() {
        return username;
    }

    public ApiCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ApiCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean useHmacAuth() {
        return useHmacAuth;
    }

    public ApiCredentials setUseHmacAuth(boolean useHmacAuth) {
        this.useHmacAuth = useHmacAuth;
        return this;
    }

    public void skipTestIfNotConfigured() {
        if (this.username == null || "".equals(this.username)) {
            throw new SkipException("API Credentials not configured");
        }
    }
}
