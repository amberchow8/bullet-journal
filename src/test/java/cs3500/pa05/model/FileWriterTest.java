package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.ThemeJson;
import cs3500.pa05.model.json.WeekJson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests FileWriter
 */
public class FileWriterTest {
    private FileWriter fileWriter;
    private FileWriter badFileWriter;

    /**
     * Sets up file writer
     */
    @BeforeEach
    public void setUp() {
        WeekJson weekJson = new WeekJson("hello", 5, 5, new ArrayList<>(),
                "NEON", new ArrayList<>(
                List.of(new DayJson(DayType.SUNDAY, new ArrayList<>(), new ArrayList<>()))),
                new ArrayList<>(
                        List.of(new TaskJson(
                                "name", "desc", "Monday", "true"))), "note");
        fileWriter = new FileWriter(weekJson, "src/test/resources/fileWriteTest");
        badFileWriter = new FileWriter(weekJson, "bad/file");
    }

    /**
     * Tests writeBujo()
     */
    @Test
    public void testWriteBujo() {
        String jsonString = "";
        badFileWriter.writeBujo();
        fileWriter.writeBujo();
        try {
            jsonString = new String(Files.readAllBytes(Path.of("src/test/resources/fileWriteTest")));
            System.out.println(jsonString);
        } catch (IOException e) {
            System.err.println("File writer test not found");
        }
        assertEquals("{\n" +
                        "  \"name\" : \"hello\",\n" +
                        "  \"task-max\" : 5,\n" +
                        "  \"event-max\" : 5,\n" +
                        "  \"theme-options\" : [ ],\n" +
                        "  \"current-theme\" : \"NEON\",\n" +
                        "  \"days\" : [ {\n" +
                        "    \"day\" : \"SUNDAY\",\n" +
                        "    \"events\" : [ ],\n" +
                        "    \"tasks\" : [ ]\n" +
                        "  } ],\n" +
                        "  \"task-queue\" : [ {\n" +
                        "    \"name\" : \"name\",\n" +
                        "    \"description\" : \"desc\",\n" +
                        "    \"day\" : \"Monday\",\n" +
                        "    \"completed\" : \"true\"\n" +
                        "  } ],\n" +
                        "  \"note\" : \"note\"\n" +
                        "}",
                jsonString);

    }

}
