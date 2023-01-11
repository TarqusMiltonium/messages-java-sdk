package com.messagemedia.messages.controllers;

import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.models.*;
import com.messagemedia.messages.testutils.DestinationNumbers;
import com.messagemedia.messages.testutils.TestUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesControllerTest {

    // Sample taken from https://www.acma.gov.au/phone-numbers-fictional-purposes#mobile-numbers
    final String samplePhoneNumber = "+61491571266";

    // This is the duration allowed for the server-side gateway to "schedule" a message
    final int maxDelayMillisAllowanceForMessageScheduling = 10000;

    @Test
    public void sendLiveMessages() throws Throwable {
        DestinationNumbers destNumbers = TestUtils.getDestPhoneNumbersOrSkip();
        MessageMediaMessagesClient client = TestUtils.getConfiguredClientOrSkip();
        MessagesController messages = client.getMessages();

        List<Message> messagesList = new ArrayList<>();
        for (String destNum : destNumbers.getValidPhoneNumbers()) {
            Message message = new Message();
            String randomRef = TestUtils.generateRandomUpperCharsString(5); // Avoids Server-side Duplicate-Message Detection
            message.setContent("This is a Test message.\nRef: " + randomRef);
            message.setDestinationNumber(destNum);
            messagesList.add(message);
            System.out.println("Sending Message to phone number: " + destNum);
        }

        SendMessagesRequest body = new SendMessagesRequest();
        body.setMessages(messagesList);

        SendMessagesResponse response = messages.sendMessages(body);

        System.out.println("All Messages successfully sent:");
        int i = 1;
        for (Message msg : response.getMessages()) {
            System.out.println("  Message " + i++  + ":");
            System.out.println("    ID:        " + msg.getMessageId());
            System.out.println("    To Number: " + msg.getDestinationNumber());
            System.out.println("    From Num:  " + msg.getSourceNumber());
            System.out.println("    Status:    " + msg.getStatus());
            System.out.println("    Scheduled: " + msg.getScheduled());
        }

        System.out.println("All Messages successfully sent");
    }

    @Test
    public void sendAndCancelScheduledMessage() throws Throwable {
        MessageMediaMessagesClient client = TestUtils.getConfiguredClientOrSkip();
        MessagesController messages = client.getMessages();

        Message message = new Message();
        String randomRef = TestUtils.generateRandomUpperCharsString(5); // Avoids Server-side Duplicate-Message Detection
        message.setContent("This is a Test message.\nRef: " + randomRef);
        message.setDestinationNumber(samplePhoneNumber);
        message.setScheduled(DateTime.now().plus(Duration.standardHours(2L)));
        message.setMessageExpiryTimestamp(DateTime.now().plus(Duration.standardHours(1L))); // Ensure msg does not get sent

        List<Message> messagesList = Collections.singletonList(message);
        System.out.println("Sending Delayed Message to phone number: " + samplePhoneNumber);

        SendMessagesRequest body = new SendMessagesRequest();
        body.setMessages(messagesList);

        SendMessagesResponse responseSend = messages.sendMessages(body);

        System.out.println("Message successfully sent:");
        Message msg = responseSend.getMessages().get(0); // Only 1 message in response
        String messageId = msg.getMessageId().toString();
        System.out.println("    ID:        " + messageId);
        System.out.println("    To Number: " + msg.getDestinationNumber());
        System.out.println("    From Num:  " + msg.getSourceNumber());
        System.out.println("    Status:    " + msg.getStatus());
        System.out.println("    Scheduled: " + msg.getScheduled());

        System.out.println("\nNow waiting for Status to be set to \"Scheduled\" (max Wait timeout: " + maxDelayMillisAllowanceForMessageScheduling/1000 + "s)");
        GetMessageStatusResponse responseStatus = getMsgStatusAsScheduled(messageId, messages, maxDelayMillisAllowanceForMessageScheduling);

        if (StatusEnum.SCHEDULED.equals(responseStatus.getStatus())) {
            System.out.println("Message Status is correctly set as \"Scheduled\"; Cancelling.");
            DynamicResponse responseCancel = messages.cancelScheduledMessage(messageId, new CancelScheduledMessageRequest());
            System.out.println("Message successfully Cancelled");
        } else {
            System.out.println("Message is not correctly set to Status \"Scheduled\"; Something else has gone wrong");
            Assert.fail();
        }
    }

    private static GetMessageStatusResponse getMsgStatusAsScheduled(String messageId, MessagesController messages, int maxTimeout)
            throws Throwable {
        int elapsedMs = 0;
        int repeatMs = 1000;

        GetMessageStatusResponse responseStatus = new GetMessageStatusResponse();
        while (elapsedMs <= maxTimeout) {
            Thread.sleep(repeatMs);
            elapsedMs += repeatMs;
            System.out.println("  Checking Updated Status (Elapsed: " + elapsedMs/1000 + "s)");
            responseStatus = messages.getMessageStatus(messageId);
            if (StatusEnum.SCHEDULED.equals(responseStatus.getStatus())) {
                return responseStatus;
            } else {
                System.out.println("\nMessage not yet Scheduled (will check again in " + repeatMs/1000 + "s)");
            }
        }
        return responseStatus;
    }

}