/**
 *
 */
package org.datasift.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.MockApiClient;
import org.datasift.Usage;
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
		try {
			assertEquals("Definition is not empty", def.get(), "");
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}

	public void testCreateDefinition_NonEmpty() {
		Definition def = user.createDefinition(DataForTests.definition);
		try {
			assertEquals("Definition is incorrect", def.get(), DataForTests.definition);
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		}
	}

	public void testRateLimits() {
		Definition def = user.createDefinition(DataForTests.definition);

		api_client.setResponse("{\"hash\":\"947b690ec9dca525fb8724645e088d79\",\"created_at\":\"2011-05-16 17:20:02\",\"dpu\":\"10\"}", 200, 150, 100);

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

	public void testGetUsage() {
		api_client.setResponse("{\"start\":\"Mon, 07 Nov 2011 14:20:00 +0000\",\"streams\":{\"5e82aa9ac3dcf4dec1cce08a0cec914a\":{\"seconds\":313,\"licenses\":{\"twitter\":17,\"facebook\":5}}},\"end\":\"Mon, 07 Nov 2011 15:20:00 +0000\"}", 200, 150, 100);

		try {
			Usage u = user.getUsage();

			SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));

			assertEquals("Start date is incorrect", "Mon Nov 07 14:20:00 GMT 2011", df.format(u.getStartDate()));
			assertEquals("End date is incorrect", "Mon Nov 07 15:20:00 GMT 2011", df.format(u.getEndDate()));
			assertEquals("Seconds consumed is incorrect", 313, u.getSeconds("5e82aa9ac3dcf4dec1cce08a0cec914a"));
			assertEquals("Twitter licenses used is incorrect", 17, u.getLicenseUsage("5e82aa9ac3dcf4dec1cce08a0cec914a", "twitter"));
			assertEquals("Facebook licenses used is incorrect", 5, u.getLicenseUsage("5e82aa9ac3dcf4dec1cce08a0cec914a", "facebook"));
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (ParseException e) {
			fail("Caught ParseException: " + e.toString());
		}
	}

	public void testGetUsageTZ() {
		api_client.setResponse("{\"start\":\"Mon, 07 Nov 2011 14:20:00 +0000\",\"streams\":{\"5e82aa9ac3dcf4dec1cce08a0cec914a\":{\"seconds\":313,\"licenses\":{\"twitter\":17,\"facebook\":5}}},\"end\":\"Mon, 07 Nov 2011 15:20:00 +0000\"}", 200, 150, 100);

		try {
			Usage u = user.getUsage();

			SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
			df.setTimeZone(TimeZone.getTimeZone("EST"));

			assertEquals("Start date is incorrect", "Mon Nov 07 09:20:00 EST 2011", df.format(u.getStartDate()));
			assertEquals("End date is incorrect", "Mon Nov 07 10:20:00 EST 2011", df.format(u.getEndDate()));
			assertEquals("Seconds consumed is incorrect", 313, u.getSeconds("5e82aa9ac3dcf4dec1cce08a0cec914a"));
			assertEquals("Twitter licenses used is incorrect", 17, u.getLicenseUsage("5e82aa9ac3dcf4dec1cce08a0cec914a", "twitter"));
			assertEquals("Facebook licenses used is incorrect", 5, u.getLicenseUsage("5e82aa9ac3dcf4dec1cce08a0cec914a", "facebook"));
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (ParseException e) {
			fail("Caught ParseException: " + e.toString());
		}
	}
}
