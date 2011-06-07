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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		user = new User(Config.username, Config.api_key);
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

		try {
			def.compile();
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// Ignore this, irrelevant to this test
		} catch (EAccessDenied e) {
			// Ignore this, irrelevant to this test
		}

		assertFalse("Rate limit is -1 after calling the API",
				user.getRateLimit() == -1);
		assertFalse("Rate limit remaining is -1 after calling the API",
				user.getRateLimitRemaining() == -1);
	}
}
