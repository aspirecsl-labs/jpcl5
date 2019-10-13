package com.aspirecsl.labs;

import javax.print.PrintService;

import static java.util.Objects.requireNonNull;
import static javax.print.DocFlavor.SERVICE_FORMATTED.PRINTABLE;
import static javax.print.PrintServiceLookup.lookupDefaultPrintService;
import static javax.print.PrintServiceLookup.lookupPrintServices;

/** @author anoopr */
class PrintServiceProvider {
  PrintServiceProvider() {}

  static PrintService from(String printerName) {
    requireNonNull(printerName);
    if ("default".equalsIgnoreCase(printerName)) {
      return lookupDefaultPrintService();
    } else {
      final PrintService[] allPrintServices = lookupPrintServices(PRINTABLE, null);
      for (PrintService onePrintService : allPrintServices) {
        if (printerName.equalsIgnoreCase(onePrintService.getName())) {
          return onePrintService;
        }
      }
    }
    return null;
  }
}
