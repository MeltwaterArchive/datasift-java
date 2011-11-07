/**
 * 
 */
package org.datasift.tests;

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
		assertEquals("Definition is not empty", def.get(), "");
	}

	public void testCreateDefinition_NonEmpty() {
		Definition def = user.createDefinition(Config.definition);
		assertEquals("Definition is incorrect", def.get(), Config.definition);
	}

	public void testRateLimits() {
		Definition def = user.createDefinition(Config.definition);

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
	
	public void testGetUsageSummary() {
		api_client.setResponse("{\"processed\":9999,\"delivered\":10800,\"streams\":{\"a123ab20f37f333824159b8868ad3827\":{\"processed\":7505,\"delivered\":8100},\"c369ab20f37f333824159b8868ad3827\":{\"processed\":2494,\"delivered\":2700}}}", 200, 150, 100);

		Usage u;
		try {
			u = user.getUsage();

			assertEquals("Processed count is incorrect", 9999, u.getProcessed());
			assertEquals("Delivered count is incorrect", 10800, u.getDelivered());

			assertEquals("Processed count for hash a123ab20f37f333824159b8868ad3827 is incorrect", 7505, u.getProcessed("a123ab20f37f333824159b8868ad3827"));
			assertEquals("Delivered count for hash a123ab20f37f333824159b8868ad3827 is incorrect", 8100, u.getDelivered("a123ab20f37f333824159b8868ad3827"));

			assertEquals("Processed count for hash c369ab20f37f333824159b8868ad3827 is incorrect", 2494, u.getProcessed("c369ab20f37f333824159b8868ad3827"));
			assertEquals("Delivered count for hash c369ab20f37f333824159b8868ad3827 is incorrect", 2700, u.getDelivered("c369ab20f37f333824159b8868ad3827"));

			int totalProcessed = 0;
			int totalDelivered = 0;
			for (String hash : u.getItems()) {
				totalProcessed += u.getProcessed(hash);
				totalDelivered += u.getDelivered(hash);
			}
			assertEquals("Sum of processed for hashes does not equal total processed", u.getProcessed(), totalProcessed);
			assertEquals("Sum of delivered for hashes does not equal total delivered", u.getDelivered(), totalDelivered);
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		}
	}

	public void testGetUsageForStream() {
		api_client.setResponse("{\"processed\":2494,\"delivered\":2700,\"types\":{\"buzz\":{\"processed\":247,\"delivered\":350},\"twitter\":{\"processed\":2247,\"delivered\":2350}}}", 200, 150, 100);

		Usage u;
		try {
			u = user.getUsage("a123ab20f37f333824159b8868ad3827");

			assertEquals("Processed count is incorrect", 2494, u.getProcessed());
			assertEquals("Delivered count is incorrect", 2700, u.getDelivered());

			assertEquals("Processed count for type buzz is incorrect", 247, u.getProcessed("buzz"));
			assertEquals("Delivered count for type buzz is incorrect", 350, u.getDelivered("buzz"));

			assertEquals("Processed count for type twitter is incorrect", 2247, u.getProcessed("twitter"));
			assertEquals("Delivered count for type twitter is incorrect", 2350, u.getDelivered("twitter"));

			int totalProcessed = 0;
			int totalDelivered = 0;
			for (String hash : u.getItems()) {
				totalProcessed += u.getProcessed(hash);
				totalDelivered += u.getDelivered(hash);
			}
			assertEquals("Sum of processed for types does not equal total processed", u.getProcessed(), totalProcessed);
			assertEquals("Sum of delivered for types does not equal total delivered", u.getDelivered(), totalDelivered);
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		}
	}
}
