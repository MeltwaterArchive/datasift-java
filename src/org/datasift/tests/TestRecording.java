/**
 * 
 */
package org.datasift.tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.junit.Before;

import org.datasift.Config;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.JSONdn;
import org.datasift.MockApiClient;
import org.datasift.Recording;
import org.datasift.RecordingExport;
import org.datasift.User;

/**
 * @author MediaSift
 * @version 0.1
 */
public class TestRecording extends TestCase {
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
		try {
			Recording recording = new Recording(user, new JSONdn(DataForTests.recording));
			assertEquals("Recording ID is incorrect", DataForTests.recording_id, recording.getID());
			assertEquals("Recording name is incorrect", DataForTests.recording_name, recording.getName());
			assertEquals("Recording start time is incorrect", DataForTests.recording_start_time, recording.getStartTime());
			assertEquals("Recording end time is incorrect", DataForTests.recording_end_time, recording.getEndTime());
			assertEquals("Recording hash is incorrect", DataForTests.recording_hash, recording.getHash());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		}
	}
	
	public void testConstructionViaAPI() {
		api_client.setResponse(DataForTests.recording, 200, 150, 100);
		try {
			Recording recording = new Recording(user, DataForTests.recording_id);
			assertEquals("Recording ID is incorrect", DataForTests.recording_id, recording.getID());
			assertEquals("Recording name is incorrect", DataForTests.recording_name, recording.getName());
			assertEquals("Recording start time is incorrect", DataForTests.recording_start_time, recording.getStartTime());
			assertEquals("Recording end time is incorrect", DataForTests.recording_end_time, recording.getEndTime());
			assertEquals("Recording hash is incorrect", DataForTests.recording_hash, recording.getHash());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAPIError e) {
			fail("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testUpdate() {
		try {
			Recording recording = new Recording(user, new JSONdn(DataForTests.recording));
			
			api_client.setResponse(DataForTests.recording, 200, 150, 100);
			recording.update(1234, 5678, "New name");

			assertEquals("Recording ID is incorrect", DataForTests.recording_id, recording.getID());
			assertEquals("Recording name is incorrect", DataForTests.recording_name, recording.getName());
			assertEquals("Recording start time is incorrect", DataForTests.recording_start_time, recording.getStartTime());
			assertEquals("Recording end time is incorrect", DataForTests.recording_end_time, recording.getEndTime());
			assertEquals("Recording hash is incorrect", DataForTests.recording_hash, recording.getHash());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (EAPIError e) {
			fail("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testDelete() {
		Recording recording = null;
		try {
			recording = new Recording(user, new JSONdn(DataForTests.recording));

			api_client.setResponse("{\"success\":true}", 200, 150, 100);
			recording.delete();

			try {
				recording.getID();
				fail("Expected InvalidData exception was not thrown when retrieving the export ID");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				recording.getName();
				fail("Expected InvalidData exception was not thrown when retrieving the export name");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				recording.getStartTime();
				fail("Expected InvalidData exception was not thrown when retrieving the export start time");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				recording.getEndTime();
				fail("Expected InvalidData exception was not thrown when retrieving the export end time");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				recording.getHash();
				fail("Expected InvalidData exception was not thrown when retrieving the export hash");
			} catch (EInvalidData e) {
				// Expected exception
			}
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (EAPIError e) {
			fail("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testDeleteApiErrors() {
		Recording recording = null;
		
		// Bad request
		api_client.setResponse("{\"error\":\"Bad request from user supplied data\"}", 400, 150, 100);
		try {
			recording = new Recording(user, new JSONdn(DataForTests.recording));
			recording.delete();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Bad request from user supplied data");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (JSONException e) {
			fail("Caught JSONException: " + e.toString());
		}

		// Unauthorised or banned
		api_client.setResponse("{\"error\":\"User banned because they are a very bad person\"}", 401, 150, 100);
		try {
			recording = new Recording(user, new JSONdn(DataForTests.recording));
			recording.delete();
			fail("Expected EAccessDenied exception was not thrown");
		} catch (EAPIError e) {
			fail("Caught EAPIError: " + e.toString());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			// Expected exception
			assertEquals("The AccessDenied exception does not contain the correct error message", e.getMessage(), "User banned because they are a very bad person");
		} catch (JSONException e) {
			fail("Caught JSONException: " + e.toString());
		}

		// Endpoint or data not found
		api_client.setResponse("{\"error\":\"Endpoint or data not found\"}", 404, 150, 100);
		try {
			recording = new Recording(user, new JSONdn(DataForTests.recording));
			recording.delete();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Endpoint or data not found");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (JSONException e) {
			fail("Caught JSONException: " + e.toString());
		}

		// Problem with an internal service
		api_client.setResponse("{\"error\":\"Problem with an internal service\"}", 500, 150, 100);
		try {
			recording = new Recording(user, new JSONdn(DataForTests.recording));
			recording.delete();
			fail("Expected EAPIError exception was not thrown");
		} catch (EAPIError e) {
			// Expected exception
			assertEquals("The APIError exception does not contain the correct error message", e.getMessage(), "Problem with an internal service");
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught EAccessDenied: " + e.toString());
		} catch (JSONException e) {
			fail("Caught JSONException: " + e.toString());
		}
	}
	
	public void testStartExport() {
		try {
			Recording recording = new Recording(user, new JSONdn(DataForTests.recording));
			
			api_client.setResponse(DataForTests.export, 200, 150, 100);
			RecordingExport export = recording.startExport("json");
			
			assertEquals("Export ID is incorrect", DataForTests.export_id, export.getID());
			assertEquals("Export recording ID is incorrect", DataForTests.export_recording_id, export.getRecordingID());
			assertEquals("Export name is incorrect", DataForTests.export_name, export.getName());
			assertEquals("Export start is incorrect", DataForTests.export_start, (int)export.getStart());
			assertEquals("Export end is incorrect", DataForTests.export_end, (int)export.getEnd());
			assertEquals("Export status is incorrect", DataForTests.export_status, export.getStatus());
		} catch (EInvalidData e) {
			fail("Caught EInvalidData: " + e.toString());
		} catch (JSONException e) {
			fail("Caught JSONException: " + e.toString());
		} catch (EAPIError e) {
			fail("Caught APIError: " + e.toString());
		} catch (EAccessDenied e) {
			fail("Caught AccessDenied: " + e.toString());
		}
	}
}
