package se.metricspace.fileutils;

import junit.framework.TestCase;

public class RenameImageFilesTest extends TestCase {
  public RenameImageFilesTest() {
    super();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testSuffixBmp() {
    assertEquals("bmp", RenameImageFiles.getSuffix("donaldDuck.bmp"));
  }

  public void testSuffixExcel() {
    assertEquals(null, RenameImageFiles.getSuffix("donaldDuck.xls"));
  }
}
