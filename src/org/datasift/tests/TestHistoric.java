/**
 * 
 */
package org.datasift.tests;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.*;

/**
 * @author MediaSift
 */
public class TestHistoric extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDefinition.class);
	}

	private User user = null;
	private MockApiClient api_client = null;
	private Definition def = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DataForTests.init();
		user = new User(Config.username, Config.api_key);
		api_client = new MockApiClient(user);
		user.setApiClient(api_client);
		def = user.createDefinition(DataForTests.definition);
	}

	public void testConstruction() {
		try {
			// createHistoric will compile the definition, so set up the response
			api_client.setResponse("{\"hash\":\"" + DataForTests.definition_hash + "\",\"created_at\":\"2011-05-16 17:20:02\",\"dpu\":\"10\"}", 200);
			Historic h = def.createHistoric(DataForTests.historic_start, DataForTests.historic_end, DataForTests.historic_sources, DataForTests.historic_sample, DataForTests.historic_name);

			assertEquals("Definition ID is incorrect", DataForTests.definition_hash, h.getStreamHash());
			assertEquals("Name is incorrect", DataForTests.historic_name, h.getName());
			assertEquals("Start date is incorrect", DataForTests.historic_start, h.getStartDate());
			assertEquals("End date is incorrect", DataForTests.historic_end, h.getEndDate());
			assertEquals("Status is incorrect", "created", h.getStatus());
			assertEquals("Progress is incorrect", 0, h.getProgress());
			assertEquals("Sample is incorrect", DataForTests.historic_sample, h.getSample());
			
			String[] sources_input = DataForTests.historic_sources.split("/,/");
			ArrayList<String> sources = h.getSources();
			assertEquals("Incorrect number of sources", sources_input.length, sources.size());
			for (String source : sources_input) {
				assertTrue("Source \"" + source + "\" not found in sources", sources.contains(source));
			}

		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("EAccessDenied: " + e.getMessage());
		}
	}
	
	public void testGetHistoric() {
		String playback_id = "6cc90a14f570c4acdd22";
		String definition_id = "93558e17de15072fa126370c37c5bd8f";
		String name = "historic1";
		long start = 1329217703L;
		long end = 1329221303L;
		long created_at = 1334790000L;
		String status = "queued";
		int progress = 45;
		double sample = 42.0;
		String source1 = "twitter";
		String source2 = "digg";

		
		api_client.setResponse(
			"{\"id\":\"" + playback_id + "\",\"definition_id\":\"" + definition_id + "\",\"name\":\"" + name + "\"," +
			"\"start\":" + String.valueOf(start) + ",\"end\":" + String.valueOf(end) + ",\"created_at\":" + String.valueOf(created_at) +"," + 
			"\"status\":\"" + status + "\",\"progress\":" + String.valueOf(progress) + ",\"sources\":[\"" + source1 + "\",\"" + source2 + "\"]," +
			"\"sample\":" + String.valueOf(sample) + "}", 200);

		try {
			Historic h = user.getHistoric(DataForTests.historic_playback_id);

			assertEquals("ID is incorrect", playback_id, h.getHash());
			assertEquals("Definition ID is incorrect", definition_id, h.getStreamHash());
			assertEquals("Name is incorrect", name, h.getName());
			assertEquals("Start date is incorrect", start, h.getStartDate().getTime() / 1000);
			assertEquals("End date is incorrect", end, h.getEndDate().getTime() / 1000);
			assertEquals("Status is incorrect", status, h.getStatus());
			assertEquals("Progress is incorrect", progress, h.getProgress());
			assertEquals("Sample is incorrect", sample, h.getSample());
			
			ArrayList<String> sources = h.getSources();
			assertEquals("Incorrect number of sources", 2, sources.size());
			assertTrue("Type 1 not found in sources", sources.contains(source1));
			assertTrue("Type 2 not found in sources", sources.contains(source2));
			
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (EAPIError e) {
			fail("EAPIError: " + e.getMessage());
		}
	}
}

