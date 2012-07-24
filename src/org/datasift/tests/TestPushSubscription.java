/**
 * 
 */
package org.datasift.tests;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.*;
import org.datasift.pushsubscription.HttpOutputType;

/**
 * @author MediaSift
 */
public class TestPushSubscription extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestPushSubscription.class);
	}

	private User user = null;
	private MockApiClient api_client = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataForTests.init();
		user = new User(Config.username, Config.api_key);
		api_client = new MockApiClient(user);
		user.setApiClient(api_client);
	}

	public void testConstruction() {
		try {
			HttpOutputType push = (HttpOutputType) PushSubscription.factory(user, DataForTests.push_output_type, DataForTests.push_hash_type, DataForTests.definition_hash, DataForTests.push_name);
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Default status is incorrect", "", push.getStatus());
			assertNull("Default last request time is not null", push.getLastRequest());
			assertNull("Default last success time is not null", push.getLastSuccess());
			assertEquals("Default delivery frequency is incorrect", 10, push.getDeliveryFrequency());
			assertEquals("Default max size is incorrect", 0, push.getMaxSize());
			assertEquals("Default url is incorrect", "", push.getUrl());
			assertEquals("Default auth type is incorrect", "none", push.getAuthType());
			assertEquals("Default auth username is incorrect", "", push.getAuthUsername());
			assertEquals("Default auth password is incorrect", "", push.getAuthPassword());
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}
	
	public void testGetSubscription() {
		try {
			HttpOutputType push = getHttpTestSubscription();
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
			assertEquals("Last request time is incorrect", DataForTests.push_last_request, push.getLastRequest());
			assertEquals("Last success time is incorrect", DataForTests.push_last_success, push.getLastSuccess());
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, push.getDeliveryFrequency());
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, push.getMaxSize());
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getUrl());
			assertEquals("Auth type is incorrect", DataForTests.push_output_params_auth_type, push.getAuthType());
			assertEquals("Auth username is incorrect", DataForTests.push_output_params_auth_username, push.getAuthUsername());
			assertEquals("Auth password is incorrect", DataForTests.push_output_params_auth_password, push.getAuthPassword());
		} catch (EAPIError e) {
			fail("EAPIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("EAccessDenied: " + e.getMessage());
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}
	
	public void testUpdateSubscription() {
		try {
			HttpOutputType push = getHttpTestSubscription();
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
			assertEquals("Last request time is incorrect", DataForTests.push_last_request, push.getLastRequest());
			assertEquals("Last success time is incorrect", DataForTests.push_last_success, push.getLastSuccess());
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, push.getDeliveryFrequency());
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, push.getMaxSize());
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getUrl());
			assertEquals("Auth type is incorrect", DataForTests.push_output_params_auth_type, push.getAuthType());
			assertEquals("Auth username is incorrect", DataForTests.push_output_params_auth_username, push.getAuthUsername());
			assertEquals("Auth password is incorrect", DataForTests.push_output_params_auth_password, push.getAuthPassword());
			
			// Change the name and the auth type
			String new_name = "My new subscription name";
			String new_auth_type = "basic";
			String new_auth_user = "myusername";
			String new_auth_pass = "mypassword";
			
			// Set the data
			push.setName(new_name);
			push.setAuthType(new_auth_type);
			push.setAuthUsername(new_auth_user);
			push.setAuthPassword(new_auth_pass);
			
			// Save it
			api_client.setResponse(DataForTests.getHttpSubscriptionJson(new_name, -1, -1, "", new_auth_type, new_auth_user, new_auth_pass), 200);
			push.save();
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", new_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
			assertEquals("Last request time is incorrect", DataForTests.push_last_request, push.getLastRequest());
			assertEquals("Last success time is incorrect", DataForTests.push_last_success, push.getLastSuccess());
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, push.getDeliveryFrequency());
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, push.getMaxSize());
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getUrl());
			assertEquals("Auth type is incorrect", new_auth_type, push.getAuthType());
			assertEquals("Auth username is incorrect", new_auth_user, push.getAuthUsername());
			assertEquals("Auth password is incorrect", new_auth_pass, push.getAuthPassword());
		} catch (EAPIError e) {
			fail("EAPIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("EAccessDenied: " + e.getMessage());
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}
	
	private HttpOutputType getHttpTestSubscription() throws EAPIError, EAccessDenied, EInvalidData {
		return getHttpTestSubscription("", "", "", "");
	}

	private HttpOutputType getHttpTestSubscription(String name, String auth_type, String auth_user, String auth_pass) throws EAPIError, EAccessDenied, EInvalidData {
		api_client.setResponse(DataForTests.getHttpSubscriptionJson(name, -1, -1, "", auth_type, auth_user, auth_pass), 200);
		return (HttpOutputType) PushSubscription.get(user, DataForTests.push_id);
	}
}
