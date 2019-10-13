package com.aspirecsl.labs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;

import static javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;

/** @author anoopr */
public class Printer {
  private final PrintService printService;

  private final PrintJobFormatter printJobFormatter;

  private Printer(String printerName) {
    printService = PrintServiceProvider.from(printerName);
    printJobFormatter = new PrintJobFormatter();
  }

  public static Printer create() {
    return new Printer("default");
  }

  public static Printer from(String printerName) {
    return new Printer(printerName);
  }

  public void print(PrintJob printJob) throws IOException {
    print(printJob, true);
  }

  public void print(PrintJob printJob, boolean deleteRawPrintFile) throws IOException {
    final File rawPrintFile = printJobFormatter.format(printJob);
    try (InputStream is = new FileInputStream(rawPrintFile)) {
      final DocPrintJob job =
          Optional.ofNullable(printService)
              .orElseThrow(() -> new IOException("no printer available"))
              .createPrintJob();
      final Doc doc = new SimpleDoc(is, AUTOSENSE, null);
      job.print(doc, null);
      if (deleteRawPrintFile) {
        Files.delete(rawPrintFile.toPath());
      }
    } catch (PrintException e) {
      throw new IOException("printing failed. exception is:- ", e);
    }
  }
}
