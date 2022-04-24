package me;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class ApplicationTest {

    private InputStream sysInBackup;
    private PrintStream sysOutBackup;

    @Before
    public void setUp() throws Exception {
        sysInBackup = System.in;
        sysOutBackup = System.out;
    }

    @After
    public void tearDown() throws Exception {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    @Test
    public void takesAddInputThroughScannerAndAcknowledgesItInOutput() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1\nfirstkey\nfirstcontent\n4\n".getBytes());
        System.setIn(in);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        Application.main(new String[0]);

        String fullOutput = byteArrayOutputStream.toString();
        String resultHeader = "association added: ";
        String resultOnly = fullOutput.substring(fullOutput.indexOf(resultHeader) + resultHeader.length());
        assertThat(resultOnly, Matchers.startsWith("firstkey : firstcontent\n"));
    }

    @Test
    public void takesAddInputThroughScannerAndCanThenRetrieveIt() {
        ByteArrayInputStream in = new ByteArrayInputStream((
                "1\n1\nfirstkey\nfirstcontent\n" +
                        "2\nfirstkey\n4\n").getBytes());
        System.setIn(in);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        Application.main(new String[0]);

        String fullOutput = byteArrayOutputStream.toString();
        String resultHeader = "found content associated with key: ";
        String resultOnly = fullOutput.substring(fullOutput.indexOf(resultHeader) + resultHeader.length());
        assertThat(resultOnly, Matchers.startsWith("firstcontent\n"));
    }

    @Test
    public void uponAddingAndFindingAssociationsWillEjectLeastFoundAssociation() {
        ByteArrayInputStream in = new ByteArrayInputStream((
                "2\n1\nfirstkey\nfirstcontent\n" + //set association limit and add first key-content
                        "1\nsecondkey\nsecondcontent\n" + //add second key-content
                        "2\nfirstkey\n" + //find first key-content
                        "1\nthirdkey\nthirdcontent\n" + //add third key-content
                        "2\nfirstkey\n" + //can still find first key-content
                        "2\nthirdkey\n" + //can find third key-content
                        "2\nsecondkey\n" + //cannot find second key-content anymore
                        "4\n").getBytes());
        System.setIn(in);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        Application.main(new String[0]);

        String fullOutput = byteArrayOutputStream.toString();
        String resultHeader = "cannot find content for key ";
        String resultOnly = fullOutput.substring(fullOutput.indexOf(resultHeader) + resultHeader.length());
        assertThat(resultOnly, Matchers.startsWith("secondkey\n"));
    }
}