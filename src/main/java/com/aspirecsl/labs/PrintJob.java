package com.aspirecsl.labs;

import java.io.File;

/**
 * Represents a print job.
 *
 * @author anoopr
 */
public class PrintJob {
  private final int cpi;
  private final int numberOfCopies;

  private final File file;

  private final boolean retainPrintFile;
  private final boolean printToFileOnly;

  private final DuplexBinding duplexBinding;
  private final Orientation orientation;

  private PrintJob(Builder builder) {
    cpi = builder.cpi;
    file = builder.file;
    duplexBinding = builder.duplexBinding;
    numberOfCopies = builder.numberOfCopies;
    retainPrintFile = builder.retainPrintFile;
    printToFileOnly = builder.printToFileOnly;
    orientation = builder.orientation;
  }

  /**
   * Creates a new PrintJob builder instance for the given <tt>file</tt>.
   *
   * <p>The builder can be used to set other print attributes, like duplex, cpi etc, set to defaults
   *
   * @param file the file to print
   * @return PrintJob builder instance
   */
  public static Builder build(File file) {
    return new Builder(file);
  }

  File file() {
    return file;
  }

  int numberOfCopies() {
    return numberOfCopies;
  }

  DuplexBinding duplexBinding() {
    return duplexBinding;
  }

  Orientation orientation() {
    return orientation;
  }

  int cpi() {
    return cpi;
  }

  public boolean retainPrintFile() {
    return retainPrintFile;
  }

  public boolean printToFileOnly() {
    return printToFileOnly;
  }

  public enum DuplexBinding {
    NONE,
    LONG_EDGE,
    SHORT_EDGE
  }

  public enum Orientation {
    PORTRAIT,
    LANDSCAPE
  }

  public static class Builder {
    private final File file;

    private int cpi;
    private int numberOfCopies;

    private boolean retainPrintFile;

    private boolean printToFileOnly;

    private DuplexBinding duplexBinding;

    private Orientation orientation;

    private Builder(File file) {
      this.file = file;

      cpi = 12;
      numberOfCopies = 1;
      duplexBinding = DuplexBinding.NONE;
      retainPrintFile = false;
      printToFileOnly = false;
      orientation = Orientation.LANDSCAPE;
    }

    public Builder duplex(DuplexBinding duplexBinding) {
      this.duplexBinding = duplexBinding;
      return this;
    }

    public Builder copies(int copies) {
      this.numberOfCopies = copies;
      return this;
    }

    public Builder retainPrintFile() {
      this.retainPrintFile = true;
      return this;
    }

    public Builder printToFileOnly() {
      this.printToFileOnly = true;
      return this;
    }

    public Builder orientation(Orientation orientation) {
      this.orientation = orientation;
      return this;
    }

    public Builder cpi(int cpi) {
      this.cpi = cpi;
      return this;
    }

    public PrintJob build() {
      return new PrintJob(this);
    }
  }
}
