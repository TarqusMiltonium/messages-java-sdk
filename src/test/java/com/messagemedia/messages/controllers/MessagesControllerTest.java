package com.messagemedia.messages.controllers;

import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.models.GetMessageStatusResponse;
import com.messagemedia.messages.models.SendMessagesRequest;
import com.messagemedia.messages.testutils.DestinationNumbers;
import com.messagemedia.messages.testutils.TestUtils;
import org.testng.annotations.Test;

import com.messagemedia.messages.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesControllerTest {

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

}