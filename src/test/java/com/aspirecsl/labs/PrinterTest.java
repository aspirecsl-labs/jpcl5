package com.aspirecsl.labs;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import static java.util.Objects.requireNonNull;

public class PrinterTest {

  private ClassLoader classLoader;

  @Before
  public void setup() {
    this.classLoader = getClass().getClassLoader();
  }

  @Test
  public void printsCorrectly() throws IOException {
    final String fileName = requireNonNull(classLoader.getResource("printer_test.txt")).getFile();
    final PrintJob printJob = PrintJob.build(new File(fileName)).build();
    Printer.create().print(printJob);
  }
}
