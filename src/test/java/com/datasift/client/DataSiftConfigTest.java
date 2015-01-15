package com.datasift.client;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DataSiftConfigTest {
    @Test
    public void testThatWeGetAVersion() throws IOException {
        DataSiftConfig config = new DataSiftConfig();
        File pom = new File("./pom.xml");
        if (!pom.exists()) {
            fail("Unable to find pom.xml");
        }
        List<String> lines = Files.readAllLines(pom.toPath(), Charset.defaultCharset());
        boolean foundParentVersion = false;
        String version = "3.x";
        for (String line : lines) {
            if (line.trim().startsWith("<version>")) {
                if (!foundParentVersion) {
                    foundParentVersion = true;
                    continue;
                }
                version = line.trim().substring(9, line.trim().indexOf("</version>"));
                break;
            }
        }
        assertEquals(version, config.getClientVersion());
    }
}
