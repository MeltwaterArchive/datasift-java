/**
 * 
 */
package org.datasift.tests;


import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.Definition;
import org.datasift.EAccessDenied;
import org.datasift.EInvalidData;
import org.datasift.Historic;
import org.datasift.IStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.JSONdn;
import org.datasift.MockApiClient;
import org.datasift.StreamConsumer;
import org.datasift.User;

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
			Historic hist = def.createHistoric(DataForTests.historic_start, DataForTests.historic_end, DataForTests.historic_feeds, DataForTests.historic_sample, DataForTests.historic_name);
			assertEquals("Historic start date not set correctly", hist.getStartDate(), DataForTests.historic_start);
			assertEquals("Historic end date not set correctly", hist.getEndDate(), DataForTests.historic_end);
			assertEquals("Historic name not set correctly", hist.getName(), DataForTests.historic_name);
		} catch (EInvalidData e) {
			fail("EInvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("EAccessDenied: " + e.getMessage());
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
