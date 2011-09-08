/**
 * 
 */
package org.datasift.tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.MockApiClient;
import org.datasift.Recording;
import org.datasift.RecordingExport;
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
		Definition def = user.createDefinition(DataForTests.definition);
		assertEquals("Definition is incorrect", def.get(), DataForTests.definition);
	}

	public void testRateLimits() {
		Definition def = user.createDefinition(DataForTests.definition);

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
	
	public void testGetRecordings() {
		String json = "{\"count\":\"1\",\"recordings\":[{\"id\":\"47ce46821c942ff42f8e\",\"start_time\":1313055762,\"finish_time\":null,\"name\":\"Inherit everything 123\",\"hash\":\"9e2e0ba334ee76aa06ef42d5565dbb70\"}]}";
		api_client.setResponse(json, 200, 150, 100);
		
		try {
			ArrayList<Recording> recordings = user.getRecordings();
			Recording recording = recordings.get(0);
			
			assertEquals("The recording ID is incorrect", "47ce46821c942ff42f8e", recording.getID());
			assertEquals("The recording start time is incorrect", 1313055762, (int)recording.getStartTime());
			assertEquals("The recording finish time is incorrect", null, recording.getEndTime());
			assertEquals("The recording name is incorrect", "Inherit everything 123", recording.getName());
			assertEquals("The recording hash is incorrect", "9e2e0ba334ee76aa06ef42d5565dbb70", recording.getHash());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
	
	public void testGetRecordingsInvalidParams() {
		// Try to get page 0
		try {
			user.getRecordings(0);
			fail("Expected EInvalidData exception was not thrown");
		} catch (EInvalidData e) {
			// Expected exception
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}

		// Try to get page 1 with a count of 0
		try {
			user.getRecordings(1, 0);
			fail("Expected EInvalidData exception was not thrown");
		} catch (EInvalidData e) {
			// Expected exception
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
	
	public void testGetRecordingsErrors() {
		// Bad request
		api_client.setResponse("{\"error\":\"Bad request from user supplied data\"}", 400, 150, 100);
		try {
			user.getRecordings();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Bad request from user supplied data");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}

		// Unauthorised or banned
		api_client.setResponse("{\"error\":\"User banned because they are a very bad person\"}", 401, 150, 100);
		try {
			user.getRecordings();
			fail("Expected EAccessDenied exception was not thrown");
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			// Expected exception
			assertEquals("The AccessDenied exception does not contain the correct error message", e.getMessage(), "User banned because they are a very bad person");
		}

		// Endpoint or data not found
		api_client.setResponse("{\"error\":\"Endpoint or data not found\"}", 404, 150, 100);
		try {
			user.getRecordings();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Endpoint or data not found");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}

		// Problem with an internal service
		api_client.setResponse("{\"error\":\"Problem with an internal service\"}", 500, 150, 100);
		try {
			user.getRecordings();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Problem with an internal service");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
	
	public void testGetRecording() {
		api_client.setResponse(DataForTests.recording, 200, 150, 100);
		
		try {
			Recording recording = user.getRecording(DataForTests.recording_id);
			
			assertEquals("The recording ID is incorrect", "47ce46821c942ff42f8e", recording.getID());
			assertEquals("The recording start time is incorrect", 1313055762, (int)recording.getStartTime());
			assertEquals("The recording finish time is incorrect", null, recording.getEndTime());
			assertEquals("The recording name is incorrect", "Inherit everything 123", recording.getName());
			assertEquals("The recording hash is incorrect", "9e2e0ba334ee76aa06ef42d5565dbb70", recording.getHash());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
	
	public void testScheduleRecording() {
		api_client.setResponse(DataForTests.recording, 200, 150, 100);
		
		try {
			Recording recording = user.scheduleRecording(DataForTests.recording_hash, DataForTests.recording_name, DataForTests.recording_start_time, DataForTests.recording_end_time);
			
			assertEquals("The recording ID is incorrect", recording.getID(), "47ce46821c942ff42f8e");
			assertEquals("The recording start time is incorrect", (int)recording.getStartTime(), 1313055762);
			assertEquals("The recording finish time is incorrect", recording.getEndTime(), null);
			assertEquals("The recording name is incorrect", recording.getName(), "Inherit everything 123");
			assertEquals("The recording hash is incorrect", recording.getHash(), "9e2e0ba334ee76aa06ef42d5565dbb70");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
	
	public void testGetExports() {
		api_client.setResponse("{\"count\":\"1\",\"exports\":[" + DataForTests.export + "]}", 200, 150, 100);
		
		try {
			ArrayList<RecordingExport> exports = user.getExports();
			
			assertEquals("Incorrect number of exports returned", 1, exports.size());
	
			RecordingExport export = exports.get(0);
	
			assertEquals("Export ID is incorrect", DataForTests.export_id, export.getID());
			assertEquals("Export recording ID is incorrect", DataForTests.export_recording_id, export.getRecordingID());
			assertEquals("Export name is incorrect", DataForTests.export_name, export.getName());
			assertEquals("Export start is incorrect", DataForTests.export_start, (int)export.getStart());
			assertEquals("Export end is incorrect", DataForTests.export_end, (int)export.getEnd());
			assertEquals("Export status is incorrect", DataForTests.export_status, export.getStatus());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		}
	}
}
