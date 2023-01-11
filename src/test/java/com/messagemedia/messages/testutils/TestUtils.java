package com.messagemedia.messages.testutils;

import com.messagemedia.messages.MessageMediaMessagesClient;

import java.util.Random;

public class TestUtils {

    public static MessageMediaMessagesClient getConfiguredClientOrSkip() {
        return getConfiguredClientOrSkip(ApiCredentials.fromResources());
    }

    public static MessageMediaMessagesClient getConfiguredClientOrSkip(ApiCredentials creds) {
        creds.skipTestIfNotConfigured();
        return new MessageMediaMessagesClient(creds.getUsername(), creds.getPassword(), creds.useHmacAuth());
    }

    public static DestinationNumbers getDestPhoneNumbersOrSkip() {
        return getDestPhoneNumbersOrSkip(DestinationNumbers.fromResources());
    }

    public static DestinationNumbers getDestPhoneNumbersOrSkip(DestinationNumbers numbers) {
        numbers.skipTestIfNotConfigured();
        return numbers;
    }

    // NB: This is not cryptographically secure, but doesn't need to be
    public static String generateRandomUpperCharsString(int targetStringLength) {
        int leftLimit = 'A';
        int rightLimit = 'Z';
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
