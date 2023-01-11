package com.messagemedia.messages;

import com.messagemedia.messages.testutils.ApiCredentials;
import com.messagemedia.messages.testutils.TestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MessageMediaMessagesClientTest {

    @Test
    public void instantiateBasicClient() {
        // This test is super-basic, but let's make sure even this works
        MessageMediaMessagesClient clientHmac = new MessageMediaMessagesClient("username", "password", true);
        Assert.assertNotNull(clientHmac);

        MessageMediaMessagesClient client = new MessageMediaMessagesClient("username", "password", false);
        Assert.assertNotNull(client);
        Assert.assertNotNull(client.getSharedHttpClient());
        Assert.assertNotNull(client.getMessages());
        Assert.assertNotNull(client.getReplies());
        Assert.assertNotNull(client.getDeliveryReports());
    }

    @Test
    public void areCredentialsConfiguredForTests() {
        // This Test will be skipped if there are no local-Dev credentials configured
        ApiCredentials creds = ApiCredentials.fromResources();
        Assert.assertNotNull(TestUtils.getConfiguredClientOrSkip(creds));
    }

}