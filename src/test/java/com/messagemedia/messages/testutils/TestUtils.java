package com.messagemedia.messages.testutils;

import com.messagemedia.messages.MessageMediaMessagesClient;

public class TestUtils {

    public static MessageMediaMessagesClient getConfiguredClientOrSkip() {
        return getConfiguredClientOrSkip(ApiCredentials.fromResources());
    }

    public static MessageMediaMessagesClient getConfiguredClientOrSkip(ApiCredentials creds) {
        creds.skipTestIfNotConfigured();
        return new MessageMediaMessagesClient(creds.getUsername(), creds.getPassword(), creds.useHmacAuth());
    }

}
