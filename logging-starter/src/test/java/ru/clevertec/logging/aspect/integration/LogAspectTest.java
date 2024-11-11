package ru.clevertec.logging.aspect.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.logging.aspect.integration.configuration.TestLoggingStarterConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestLoggingStarterConfiguration.class)
@ActiveProfiles("test")
class LogAspectTest {
    @Autowired
    private TestClass testClass;

    @Test
    void logAround() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream originalSystemOut = System.out;
        System.setOut(new PrintStream(byteArrayOutputStream));

        testClass.sum(1, 2);

        System.setOut(originalSystemOut);
        String logOutput = byteArrayOutputStream.toString();

        assertTrue(logOutput.contains(">> sum() - [1, 2]"));
        assertTrue(logOutput.contains("<< sum() - 3"));

    }

}