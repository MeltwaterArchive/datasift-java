/**
 * 
 */
package org.datasift.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;

import org.datasift.Config;
import org.datasift.DPU;
import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.User;

/**
 * @author MediaSift
 */
public class TestLiveApi extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestLiveApi.class);
	}

	private User user = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		user = new User(Config.username, Config.api_key);
	}

	public void testValidate_Success() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.validate();

			// We should now have a hash
			assertEquals("Incorrect hash", def.getHash(),
					DataForTests.definition_hash);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testValidate_Failure() {
		Definition def = new Definition(user, DataForTests.invalid_definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.invalid_definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.validate();
			fail("Expected ECompileFailed exception was not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testValidate_SuccessThenFailure() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.validate();

			// We should now have a hash
			assertEquals("Hash is not correct", def.getHash(),
					DataForTests.definition_hash);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}

		// Now set the invalid definition in that same object
		def.set(DataForTests.invalid_definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.invalid_definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

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
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.compile();

			// We should now have a hash
			assertEquals("Incorrect hash", def.getHash(),
					DataForTests.definition_hash);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testCompile_Failure() {
		Definition def = new Definition(user, DataForTests.invalid_definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.invalid_definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.compile();
			fail("Expected ECompileFailed exception was not thrown");
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			// This is what we should get
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}

	public void testCompile_SuccessThenFailure() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

		try {
			def.compile();

			// We should now have a hash
			assertEquals("Hash is not correct", def.getHash(),
					DataForTests.definition_hash);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (ECompileFailed e) {
			fail("CompileFailed: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}

		// Now set the invalid definition in that same object
		def.set(DataForTests.invalid_definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.invalid_definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}

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
	
	public void testGetCreatedAt() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}
		
		try {
			Date d = def.getCreatedAt();
			assertNotNull(d);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testGetTotalDPU() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}
		
		try {
			double dpu = def.getTotalDPU();
			assertTrue(dpu > 0);
		} catch (EInvalidData e) {
			fail("InvalidData: " + e.getMessage());
		} catch (EAccessDenied e) {
			fail("AccessDenied: " + e.getMessage());
		}
	}
	
	public void testGetDPUBreakdown() {
		Definition def = new Definition(user, DataForTests.definition);
		try {
			assertEquals("Definition string not set correctly", def.get(),
					DataForTests.definition);
		} catch (EInvalidData e1) {
			fail("EInvalidData: " + e1.getMessage());
		}
		
		try {
			DPU dpu = def.getDPUBreakdown();
			assertEquals(DataForTests.definition_dpu, dpu.getTotal());
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
}
