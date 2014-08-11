package a3;

import junit.framework.TestCase;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
public class MyParserTest {

	@Test
	public void TestExtractAuthorsName() throws Exception{
		MyParser parser = new MyParser();
		parser.getHTML("http://scholar.google.ca/citations?user=rr8pZoUAAAAJ&hl=en");
		assertEquals("Radford Neal",parser.getAuthorsName());
	}
}
