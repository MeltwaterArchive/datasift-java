/**
 * 
 */
package org.datasift.tests;

import junit.framework.TestCase;

import org.datasift.Config;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.MockApiClient;
import org.datasift.PushSubscription;
import org.datasift.User;
import org.junit.Before;

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

	public void testGetSubscription() {
		try {
			PushSubscription push = getTestSubscription();
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
			assertEquals("Last request time is incorrect", DataForTests.push_last_request, push.getLastRequest());
			assertEquals("Last success time is incorrect", DataForTests.push_last_success, push.getLastSuccess());
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, Integer.parseInt(push.getOutputParam("delivery_frequency")));
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, Integer.parseInt(push.getOutputParam("max_size")));
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getOutputParam("url"));
			assertEquals("Auth type is incorrect", DataForTests.push_output_params_auth_type, push.getOutputParam("auth.type"));
			assertEquals("Auth username is incorrect", DataForTests.push_output_params_auth_username, push.getOutputParam("auth.username"));
			assertEquals("Auth password is incorrect", DataForTests.push_output_params_auth_password, push.getOutputParam("auth.password"));
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
			PushSubscription push = getTestSubscription();
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
			assertEquals("Last request time is incorrect", DataForTests.push_last_request, push.getLastRequest());
			assertEquals("Last success time is incorrect", DataForTests.push_last_success, push.getLastSuccess());
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, Integer.parseInt(push.getOutputParam("delivery_frequency")));
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, Integer.parseInt(push.getOutputParam("max_size")));
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getOutputParam("url"));
			assertEquals("Auth type is incorrect", DataForTests.push_output_params_auth_type, push.getOutputParam("auth.type"));
			assertEquals("Auth username is incorrect", DataForTests.push_output_params_auth_username, push.getOutputParam("auth.username"));
			assertEquals("Auth password is incorrect", DataForTests.push_output_params_auth_password, push.getOutputParam("auth.password"));

			
			// Change the name and the auth type
			String new_name = "My new subscription name";
			String new_auth_type = "basic";
			String new_auth_user = "myusername";
			String new_auth_pass = "mypassword";
			
			// Set the data
			push.setName(new_name);
			push.setOutputParam("auth.type", new_auth_type);
			push.setOutputParam("auth.username", new_auth_user);
			push.setOutputParam("auth.password", new_auth_pass);
			
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
			assertEquals("Delivery frequency is incorrect", DataForTests.push_output_params_delivery_frequency, Integer.parseInt(push.getOutputParam("delivery_frequency")));
			assertEquals("Max size is incorrect", DataForTests.push_output_params_max_size, Integer.parseInt(push.getOutputParam("max_size")));
			assertEquals("URL is incorrect", DataForTests.push_output_params_url, push.getOutputParam("url"));
			assertEquals("Auth type is incorrect", new_auth_type, push.getOutputParam("auth.type"));
			assertEquals("Auth username is incorrect", new_auth_user, push.getOutputParam("auth.username"));
			assertEquals("Auth password is incorrect", new_auth_pass, push.getOutputParam("auth.password"));
		} catch (EAPIError e) {
			fail("EAPIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("EAccessDenied: " + e.getMessage());
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}
	
	private PushSubscription getTestSubscription() throws EAPIError, EAccessDenied, EInvalidData {
		return getTestSubscription("", "", "", "");
	}

	private PushSubscription getTestSubscription(String name, String auth_type, String auth_user, String auth_pass) throws EAPIError, EAccessDenied, EInvalidData {
		api_client.setResponse(DataForTests.getHttpSubscriptionJson(name, -1, -1, "", auth_type, auth_user, auth_pass), 200);
		return user.getPushSubscription(DataForTests.push_id);
	}
}
