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
import org.datasift.RecordingExport;
import org.datasift.User;

/**
 * @author MediaSift
 * @version 0.1
 */
public class TestRecordingExport extends TestCase {
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
			RecordingExport export = new RecordingExport(user, new JSONdn(DataForTests.export));
			assertEquals("Export ID is incorrect", DataForTests.export_id, export.getID());
			assertEquals("Export recording ID is incorrect", DataForTests.export_recording_id, export.getRecordingID());
			assertEquals("Export name is incorrect", DataForTests.export_name, export.getName());
			assertEquals("Export start is incorrect", DataForTests.export_start, (int)export.getStart());
			assertEquals("Export end is incorrect", DataForTests.export_end, (int)export.getEnd());
			assertEquals("Export status is incorrect", DataForTests.export_status, export.getStatus());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		}
	}
	
	public void testConstructionViaAPI() {
		api_client.setResponse(DataForTests.export, 200, 150, 100);
		try {
			RecordingExport export = new RecordingExport(user, DataForTests.export_id);
			assertEquals("Export ID is incorrect", DataForTests.export_id, export.getID());
			assertEquals("Export recording ID is incorrect", DataForTests.export_recording_id, export.getRecordingID());
			assertEquals("Export name is incorrect", DataForTests.export_name, export.getName());
			assertEquals("Export start is incorrect", DataForTests.export_start, (int)export.getStart());
			assertEquals("Export end is incorrect", DataForTests.export_end, (int)export.getEnd());
			assertEquals("Export status is incorrect", DataForTests.export_status, export.getStatus());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAPIError e) {
			fail("APIError: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testDelete() {
		RecordingExport export = null;
		try {
			export = new RecordingExport(user, new JSONdn(DataForTests.export));

			api_client.setResponse("{\"success\":true}", 200, 150, 100);
			export.delete();

			try {
				export.getID();
				fail("Expected InvalidData exception was not thrown when retrieving the export ID");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				export.getRecordingID();
				fail("Expected InvalidData exception was not thrown when retrieving the export recording ID");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				export.getName();
				fail("Expected InvalidData exception was not thrown when retrieving the export name");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				export.getStart();
				fail("Expected InvalidData exception was not thrown when retrieving the export start");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				export.getEnd();
				fail("Expected InvalidData exception was not thrown when retrieving the export end");
			} catch (EInvalidData e) {
				// Expected exception
			}

			try {
				export.getStatus();
				fail("Expected InvalidData exception was not thrown when retrieving the export status");
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
		RecordingExport export = null;
		
		// Bad request
		api_client.setResponse("{\"error\":\"Bad request from user supplied data\"}", 400, 150, 100);
		try {
			export = new RecordingExport(user, new JSONdn(DataForTests.export));
			export.delete();
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
			export = new RecordingExport(user, new JSONdn(DataForTests.export));
			export.delete();
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
			export = new RecordingExport(user, new JSONdn(DataForTests.export));
			export.delete();
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
			export = new RecordingExport(user, new JSONdn(DataForTests.export));
			export.delete();
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
}
