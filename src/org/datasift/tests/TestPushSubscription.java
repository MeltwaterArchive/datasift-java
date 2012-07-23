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
			api_client.setResponse(
					"{\"id\":" + String.valueOf(DataForTests.push_id)+ ",\"name\":\"" + DataForTests.push_name + "\"," +
					"\"created_at\":" + String.valueOf(DataForTests.push_created_at.getTime() / 1000) + "," +
					"\"status\":\""+ DataForTests.push_status + "\",\"hash\":\"" + DataForTests.definition_hash + "\"," +
					"\"hash_type\":\"" + DataForTests.push_hash_type + "\",\"output_type\":\"" + DataForTests.push_output_type + "\"," +
					"\"output_params\":{\"delivery_frequency\":" + String.valueOf(DataForTests.push_output_params_delivery_frequency) + "," +
					"\"max_size\":" + String.valueOf(DataForTests.push_output_params_max_size) + "," +
					"\"url\":\"" + DataForTests.push_output_params_url + "\",\"auth\":{\"type\":\""+ DataForTests.push_output_params_auth_type + "\"," +
					"\"username\":\""+ DataForTests.push_output_params_auth_username + "\",\"password\":\""+ DataForTests.push_output_params_auth_password + "\"}}}",
				200);

			HttpOutputType push = (HttpOutputType) PushSubscription.get(user, DataForTests.push_id);
			
			assertEquals("Output type is incorrect", DataForTests.push_output_type, push.getOutputType());
			assertEquals("Hash type is incorrect", DataForTests.push_hash_type, push.getHashType());
			assertEquals("Hash is incorrect", DataForTests.definition_hash, push.getHash());
			assertEquals("Name is incorrect", DataForTests.push_name, push.getName());
			assertEquals("Status is incorrect", DataForTests.push_status, push.getStatus());
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
}
