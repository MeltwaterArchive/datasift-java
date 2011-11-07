/**
 * 
 */
package org.datasift.tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.DPU;
import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.IStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.MockApiClient;
import org.datasift.StreamConsumer;
import org.datasift.User;

/**
 * @author MediaSift
 */
public class TestDefinition extends TestCase {
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
		Definition def = new Definition(user);
		assertEquals("Default definition string is not empty", def.get(), "");
	}

	public void testConstructionWithDefinition() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);
	}

	public void testSetAndGet() {
		Definition def = new Definition(user);

		def.set(Config.definition);

		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);
	}

	public void testValidate_Success() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);

		String created_at = "2011-05-16 17:20:02";
		double total_dpu = 10;
		api_client.setResponse("{\"created_at\":\"" + created_at + "\",\"dpu\":\"" + total_dpu + "\"}", 200);
		
		try {
			def.validate();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			assertEquals("Created at date is incorrect", df.parse(created_at).getTime(), def.getCreatedAt().getTime());
			assertEquals("Total DPU is incorrect", total_dpu, def.getTotalDPU());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (ParseException e) {
			fail("ParseException: " + e.getMessage());
		}
	}

	public void testValidate_Failure() {
		Definition def = new Definition(user, Config.invalid_definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.invalid_definition);

		String error = "The target interactin.content does not exist";
		api_client.setResponse("{\"error\":\"" + error + "\"}", 400);
		
		try {
			def.validate();
			fail("Expected ECompileFailed exception was not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get, check the error message is correct
			assertEquals("Compile failed as expected, but the error message is incorrect", error, e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testValidate_SuccessThenFailure() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);

		String created_at = "2011-05-16 17:20:02";
		double total_dpu = 10;
		api_client.setResponse("{\"created_at\":\"" + created_at + "\",\"dpu\":\"" + total_dpu + "\"}", 200);

		try {
			def.validate();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			assertEquals("Created at date is incorrect", df.parse(created_at).getTime(), def.getCreatedAt().getTime());
			assertEquals("Total DPU is incorrect", total_dpu, def.getTotalDPU());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (ParseException e) {
			fail("ParseException: " + e.getMessage());
		}

		// Now set the invalid definition in that same object
		def.set(Config.invalid_definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.invalid_definition);

		String error = "The target interactin.content does not exist";
		api_client.setResponse("{\"error\":\"" + error + "\"}", 400);

		try {
			def.compile();
			fail("CompileFailed exception expected, but not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testCompile_Success() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);

		String hash = "947b690ec9dca525fb8724645e088d79";
		String created_at = "2011-05-16 17:20:02";
		double total_dpu = 10;
		api_client.setResponse("{\"hash\":\"" + hash + "\",\"created_at\":\"" + created_at + "\",\"dpu\":\"" + total_dpu + "\"}", 200);

		try {
			def.compile();

			assertEquals("Incorrect hash", hash, def.getHash());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			assertEquals("Created at date is incorrect", df.parse(created_at).getTime(), def.getCreatedAt().getTime());
			assertEquals("Total DPU is incorrect", total_dpu, def.getTotalDPU());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (ParseException e) {
			fail("ParseException: " + e.getMessage());
		}
	}

	public void testCompile_Failure() {
		Definition def = new Definition(user, Config.invalid_definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.invalid_definition);

		String error = "The target interactin.content does not exist";
		api_client.setResponse("{\"error\":\"" + error + "\"}", 400);

		try {
			def.compile();
			fail("Expected ECompileFailed exception was not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get, check the error message is correct
			assertEquals("Compile failed as expected, but the error message is incorrect", error, e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testCompile_SuccessThenFailure() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);

		String hash = "947b690ec9dca525fb8724645e088d79";
		String created_at = "2011-05-16 17:20:02";
		double total_dpu = 10;
		api_client.setResponse("{\"hash\":\"" + hash + "\",\"created_at\":\"" + created_at + "\",\"dpu\":\"" + total_dpu + "\"}", 200);

		try {
			def.compile();

			assertEquals("Incorrect hash", hash, def.getHash());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			assertEquals("Created at date is incorrect", df.parse(created_at).getTime(), def.getCreatedAt().getTime());
			assertEquals("Total DPU is incorrect", total_dpu, def.getTotalDPU());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (ParseException e) {
			fail("ParseException: " + e.getMessage());
		}

		// Now set the invalid definition in that same object
		def.set(Config.invalid_definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.invalid_definition);

		String error = "The target interactin.content does not exist";
		api_client.setResponse("{\"error\":\"" + error + "\"}", 400);

		try {
			def.compile();
			fail("CompileFailed exception expected, but not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get, check the error message is correct
			assertEquals("Compile failed as expected, but the error message is incorrect", error, e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testGetDPUBreakdown() {
		Definition def = new Definition(user, Config.definition);
		assertEquals("Definition string not set correctly", def.get(),
				Config.definition);
		
		api_client.setResponse("{\"detail\":{\"contains\":{\"count\":1,\"dpu\":4,\"targets\":{\"interaction.content\":{\"count\":1,\"dpu\":4}}}},\"dpu\":4}", 200);
		
		try {
			DPU dpu = def.getDPUBreakdown();
			assertEquals("Total DPU is incorrect", 4.0, dpu.getTotal());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAPIError e) {
			fail("APIError: " + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void testGetConsumer() {
		Definition def = new Definition(user, Config.definition);
		assertEquals(def.get(), Config.definition);

		api_client.setResponse("{\"hash\":\"947b690ec9dca525fb8724645e088d79\",\"created_at\":\"2011-05-16 17:20:02\",\"dpu\":\"10\"}", 200);

		try {
			StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP,
					new eventCatcher());
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
}

class eventCatcher implements IStreamConsumerEvents {

	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
	}

	public void onStopped(StreamConsumer consumer, String reason) {
	}

}
