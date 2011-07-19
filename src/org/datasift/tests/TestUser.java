/**
 * 
 */
package org.datasift.tests;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.Definition;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.MockApiClient;
import org.datasift.User;

/**
 * @author MediaSift
 * @version 0.1
 */
public class TestUser extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDefinition.class);
	}

	private User user = null;
	private MockApiClient api_client = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		user = new User(Config.username, Config.api_key);
		api_client = new MockApiClient(user);
		user.setApiClient(api_client);
	}

	public void testConstruction() {
		assertEquals("Username is incorrect", Config.username,
				user.getUsername());
		assertEquals("API key is incorrect", Config.api_key, user.getAPIKey());
	}

	public void testCreateDefinition_Empty() {
		Definition def = user.createDefinition();
		assertEquals("Definition is not empty", def.get(), "");
	}

	public void testCreateDefinition_NonEmpty() {
		Definition def = user.createDefinition(Config.definition);
		assertEquals("Definition is incorrect", def.get(), Config.definition);
	}

	public void testRateLimits() {
		Definition def = user.createDefinition(Config.definition);

		api_client.setResponse("{\"hash\":\"947b690ec9dca525fb8724645e088d79\",\"created_at\":\"2011-05-16 17:20:02\",\"cost\":\"10\"}", 200, 150, 100);

		try {
			def.validate();

			assertEquals("Rate limit is incorrect after calling the API", 150, user.getRateLimit());
			assertEquals("Rate limit remaining is incorrect after calling the API", 100, user.getRateLimitRemaining());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
}
