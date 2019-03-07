# MessageMedia Messages Java SDK
[![Travis Build Status](https://api.travis-ci.org/messagemedia/messages-java-sdk.svg?branch=master)](https://travis-ci.org/messagemedia/messages-java-sdk)
[![Pull Requests Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat)](http://makeapullrequest.com)
[![maven](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/messagemedia/sdk/messages/maven-metadata.xml.svg)](https://mvnrepository.com/artifact/com.messagemedia.sdk/messages)

The MessageMedia Messages API provides a number of endpoints for building powerful two-way messaging applications.

![Isometric](https://i.imgur.com/jJeHwf5.png)

## Table of Contents
* [Authentication](#closed_lock_with_key-authentication)
* [Errors](#interrobang-errors)
* [Information](#newspaper-information)
  * [Slack and Mailing List](#slack-and-mailing-list)
  * [Bug Reports](#bug-reports)
  * [Contributing](#contributing)
* [Installation](#star-installation)
* [Get Started](#clapper-get-started)
* [API Documentation](#closed_book-api-documentation)
* [Need help?](#confused-need-help)
* [License](#page_with_curl-license)

## :closed_lock_with_key: Authentication

Authentication is done via API keys. Sign up at https://developers.messagemedia.com/register/ to get your API keys.

Requests are authenticated using HTTP Basic Auth or HMAC. Provide your API key as the auth_user_name and API secret as the auth_password.

## :interrobang: Errors

Our API returns standard HTTP success or error status codes. For errors, we will also include extra information about what went wrong encoded in the response as JSON. The most common status codes are listed below.

#### HTTP Status Codes

| Code      | Title       | Description |
|-----------|-------------|-------------|
| 400 | Invalid Request | The request was invalid |
| 401 | Unauthorized | Your API credentials are invalid |
| 403 | Disabled feature | Feature not enabled |
| 404 | Not Found |	The resource does not exist |
| 50X | Internal Server Error | An error occurred with our API |

## :newspaper: Information

#### Slack and Mailing List

If you have any questions, comments, or concerns, please join our Slack channel:
https://developers.messagemedia.com/collaborate/slack/

Alternatively you can email us at:
developers@messagemedia.com

#### Bug reports

If you discover a problem with the SDK, we would like to know about it. You can raise an [issue](https://github.com/messagemedia/signingkeys-nodejs-sdk/issues) or send an email to: developers@messagemedia.com

#### Contributing

We welcome your thoughts on how we could best provide you with SDKs that would simplify how you consume our services in your application. You can fork and create pull requests for any features you would like to see or raise an [issue](https://github.com/messagemedia/signingkeys-nodejs-sdk/issues)

## :star: Installation
At present the jars are available from a public maven repository.

Use the following dependency in your project to grab via Maven:
```
<dependency>
  <groupId>com.messagemedia.sdk</groupId>
  <artifactId>messages</artifactId>
  <version>1.3.0</version>
</dependency>

```

## :clapper: Get Started
It's easy to get started. Simply enter the API Key and secret you obtained from the [MessageMedia Developers Portal](https://developers.messagemedia.com) into the code snippet below and a mobile number you wish to send to.

### Send an SMS
Destination numbers (`destination_number`) should be in the [E.164](http://en.wikipedia.org/wiki/E.164) format. For example, `+61491570156`.
```java
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.MessagesController;
import com.messagemedia.messages.http.client.APICallBack;
import com.messagemedia.messages.http.client.HttpContext;
import com.messagemedia.messages.models.SendMessagesRequest;
import com.messagemedia.messages.models.SendMessagesResponse;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {

     // Configuration parameters and credentials
        String authUserName = "API KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        MessagesController messages = client.getMessages();

        String bodyValue = "{\"messages\":" +
                "[{\"content\":\"My first message\", " +
                "\"destination_number\":\"+614NUMBER\"" +
                "}]}";

        SendMessagesRequest request = new SendMessagesRequest();

        ObjectMapper mapper = new ObjectMapper();
        SendMessagesRequest body;
		try {
			body = mapper.readValue(bodyValue,new TypeReference<SendMessagesRequest> (){});

			APICallBack<SendMessagesResponse> callBack =  new APICallBack<SendMessagesResponse>() {
		        public void onSuccess(HttpContext context, SendMessagesResponse response) {
		            // TODO success callback handler
		        	System.out.println("success");
		        }
		        public void onFailure(HttpContext context, Throwable error) {
		            // TODO failure callback handler
		        	System.out.println("failure");
		        }
			};


			messages.createSendMessagesAsync(null, body, callBack);

		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
}

```

### Send an MMS
* Destination numbers (`destination_number`) should be in the [E.164](http://en.wikipedia.org/wiki/E.164) format. For example, `+61491570156`.
```java
package com.company;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.MessagesController;
import com.messagemedia.messages.models.SendMessagesRequest;
import com.fasterxml.jackson.core.type.TypeReference;

public class Main {

    public static void main(String[] args) throws Throwable {
        // Configuration parameters and credentials
        String authUserName = "API_KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API_SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        MessagesController messages = client.getMessages();

        String bodyValue = "{\"messages\":" +
                "[{\"content\":\"My first message\", " +
                "\"destination_number\":\"+61466412529\"," +
                "\"format\":\"MMS\"," +
                "\"subject\":\"This is an MMS message\"," +
                "\"media\":[\"https://upload.wikimedia.org/wikipedia/commons/6/6a/L80385-flash-superhero-logo-1544.png\"]" +
                "}]}";

        SendMessagesRequest request = new SendMessagesRequest();

        ObjectMapper mapper = new ObjectMapper();
        SendMessagesRequest body;
		 try {
			body = mapper.readValue(bodyValue, new TypeReference<SendMessagesRequest> (){});

			APICallBack<SendMessagesResponse> callBack =  new APICallBack<SendMessagesResponse>() {
		        public void onSuccess(HttpContext context, SendMessagesResponse response) {
		            // TODO success callback handler
		        	System.out.println("success");
		        }
		        public void onFailure(HttpContext context, Throwable error) {
		            // TODO failure callback handler
		        	System.out.println("failure" + error.getLocalizedMessage());
		        }
			};


			messages.createSendMessagesAsync(null, body, callBack);

		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

}
```

### Get Status of a Message
You can get a messsage ID from a sent message by looking at the `message_id` from the response of the above example.
```java
package com.company;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.MessagesController;

public class Main {

    public static void main(String[] args) throws Throwable {
        // Configuration parameters and credentials
        String authUserName = "API_KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API_SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        MessagesController messages = client.getMessages();

        String messageId = "YOUR_MESSAGE_ID";
        String result = messages.getMessageStatus(messageId).parseAsString();
        System.out.print(result);
    }

}
```

### Get replies to a message
You can check for replies that are sent to your messages
```java
package com.company;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.RepliesController;

public class Main {

    public static void main(String[] args) throws Throwable {
        // Configuration parameters and credentials
        String authUserName = "API_KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API_SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        MessagesController messages = client.getMessages();

        APICallBack<CheckRepliesResponse> callBack =  new APICallBack<CheckRepliesResponse>() {

	        public void onFailure(HttpContext context, Throwable error) {
	            // TODO failure callback handler
	        	System.out.println("failure" + error.getLocalizedMessage());
	        }

			      public void onSuccess(HttpContext context, CheckRepliesResponse response) {
				    // TODO Auto-generated method stub
				    System.out.println(response.getReplies().toString());
			    }
		};

		client.getReplies().getCheckRepliesAsync(null, callBack);
    }

}
```

### Check Delivery Reports
This endpoint allows you to check for delivery reports to inbound and outbound messages.
```java
package com.company;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.DeliveryReportsController;

public class Main {

    public static void main(String[] args) throws Throwable {
        // Configuration parameters and credentials
        String authUserName = "API_KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API_SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        DeliveryReportsController deliveryReports = client.getDeliveryReports();

        APICallBack<CheckDeliveryReportsResponse> callBack =  new APICallBack<CheckDeliveryReportsResponse>() {

	        public void onFailure(HttpContext context, Throwable error) {
	            // TODO failure callback handler
	        	System.out.println("failure" + error.getLocalizedMessage());
	        }

			     public void onSuccess(HttpContext context, CheckDeliveryReportsResponse response) {
				       // TODO Auto-generated method stub
				       System.out.println(response.getDeliveryReports().toString());
			    }
		};

		deliveryReports.getCheckDeliveryReportsAsync(null, callBack);
    }

}
```

### Confirm Delivery Reports
This endpoint allows you to mark delivery reports as confirmed so they're no longer returned by the Check Delivery Reports function.
```java
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messagemedia.messages.MessageMediaMessagesClient;
import com.messagemedia.messages.controllers.DeliveryReportsController;
import com.messagemedia.messages.http.client.APICallBack;
import com.messagemedia.messages.http.client.HttpContext;
import com.messagemedia.messages.models.ConfirmDeliveryReportsAsReceivedRequest;
import com.messagemedia.messages.models.DynamicResponse;


public class App
{
    public static void main( String[] args )
    {

     // Configuration parameters and credentials
        String authUserName = "API_KEY"; // The username to use with basic/HMAC authentication
        String authPassword = "API_SECRET"; // The password to use with basic/HMAC authentication
        boolean useHmacAuth = false; // Change to true if you are using HMAC keys

        MessageMediaMessagesClient client = new MessageMediaMessagesClient(authUserName, authPassword, useHmacAuth);
        DeliveryReportsController deliveryReports = client.getDeliveryReports();

        String bodyValue = "{ " +
        		"\"delivery_report_ids\":" +
        		"\"011dcead-6988-4ad6-a1c7-6b6c68ea628d\"," +
        		"\"3487b3fa-6586-4979-a233-2d1b095c7718\"," +
        		"\"ba28e94b-c83d-4759-98e7-ff9c7edb87a1\"" +
        		"]" +
        		"}";

        ObjectMapper mapper = new ObjectMapper();
        ConfirmDeliveryReportsAsReceivedRequest body;

        try {
			body = mapper.readValue(bodyValue, new TypeReference<ConfirmDeliveryReportsAsReceivedRequest> (){});

			APICallBack<DynamicResponse> callBack =  new APICallBack<DynamicResponse>() {

		        public void onFailure(HttpContext context, Throwable error) {
		            // TODO failure callback handler
		        	System.out.println("failure" + error.getLocalizedMessage());
		        }

				public void onSuccess(HttpContext context, DynamicResponse response) {
					// TODO Auto-generated method stub
					System.out.println(response.getRawBody().toString());
				}
			};

			deliveryReports.createConfirmDeliveryReportsAsReceivedAsync(null, body, callBack);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
```

## :closed_book: API Reference Documentation
Check out the [full API documentation](https://developers.messagemedia.com/code/messages-api-documentation/) for more detailed information.

## :confused: Need help?
Please contact developer support at developers@messagemedia.com or check out the developer portal at [developers.messagemedia.com](https://developers.messagemedia.com/)

## :page_with_curl: License
Apache License. See the [LICENSE](LICENSE) file.
