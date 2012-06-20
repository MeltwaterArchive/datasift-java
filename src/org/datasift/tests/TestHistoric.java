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
			Historic h = def.createHistoric(DataForTests.historic_start, DataForTests.historic_end, DataForTests.historic_feeds, DataForTests.historic_sample, DataForTests.historic_name);

			assertEquals("Definition ID is incorrect", DataForTests.definition_hash, h.getStreamHash());
			assertEquals("Name is incorrect", DataForTests.historic_name, h.getName());
			assertEquals("Start date is incorrect", DataForTests.historic_start, h.getStartDate());
			assertEquals("End date is incorrect", DataForTests.historic_end, h.getEndDate());
			assertEquals("Status is incorrect", "created", h.getStatus());
			assertEquals("Progress is incorrect", 0, h.getProgress());
			assertEquals("Sample is incorrect", DataForTests.historic_sample, h.getSample());
			
			String[] feeds_input = DataForTests.historic_feeds.split("/,/");
			ArrayList<String> feeds = h.getFeeds();
			assertEquals("Incorrect number of feeds", feeds_input.length, feeds.size());
			for (String feed : feeds_input) {
				assertTrue("Feed \"" + feed + "\" not found in feeds", feeds.contains(feed));
			}
			
			HashMap<String,Integer> volume_info = h.getVolumeInfo();
			assertEquals("We have volume info for a historic query that has only been created locally", 0, volume_info.size());
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
		int sample = 42;
		String feed1 = "twitter";
		String feed2 = "digg";
		String volume_info_type1 = "twitter";
		int volume_info_num1 = 123;
		String volume_info_type2 = "digg";
		int volume_info_num2 = 456;
		
		api_client.setResponse(
			"{\"id\":\"" + playback_id + "\",\"definition_id\":\"" + definition_id + "\",\"name\":\"" + name + "\"," +
			"\"start\":" + String.valueOf(start) + ",\"end\":" + String.valueOf(end) + ",\"created_at\":" + String.valueOf(created_at) +"," + 
			"\"status\":\"" + status + "\",\"progress\":" + String.valueOf(progress) + ",\"feed\":[\"" + feed1 + "\",\"" + feed2 + "\"]," +
			"\"sample\":" + String.valueOf(sample) + ",\"volume_info\":{\"" + volume_info_type1 + "\":" + String.valueOf(volume_info_num1) + "," +
			"\"" + volume_info_type2 + "\":" + String.valueOf(volume_info_num2) + "}}", 200);

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
			
			ArrayList<String> feeds = h.getFeeds();
			assertEquals("Incorrect number of feeds", 2, feeds.size());
			assertTrue("Type 1 not found in feeds", feeds.contains(feed1));
			assertTrue("Type 2 not found in feeds", feeds.contains(feed2));
			
			HashMap<String,Integer> volume_info = h.getVolumeInfo();
			assertEquals("Incorrect number of volume info records", 2, volume_info.size());
			assertTrue("Type 1 not found in volume info", volume_info.containsKey(volume_info_type1));
			assertTrue("Type 2 not found in volume info", volume_info.containsKey(volume_info_type2));
			assertEquals("Type 1 volume info value is incorrect", volume_info_num1, volume_info.get(volume_info_type1).intValue());
			assertEquals("Type 2 volume info value is incorrect", volume_info_num2, volume_info.get(volume_info_type2).intValue());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (EAPIError e) {
			fail("EAPIError: " + e.getMessage());
		}
	}
}

class TestHistoricEventCatcher implements IStreamConsumerEvents {

	public void onConnect(StreamConsumer c) {
	}
	
	public void onDisconnect(StreamConsumer c) {
	}

	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
	}

	public void onDeleted(StreamConsumer c, Interaction i)
			throws EInvalidData {
	}

	public void onStatus(StreamConsumer consumer, String type, JSONdn info) {
	}

	public void onWarning(StreamConsumer consumer, String message)
			throws EInvalidData {
	}

	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData {
	}

	public void onStopped(StreamConsumer consumer, String reason) {
	}

}
