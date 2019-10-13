package com.aspirecsl.labs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aspirecsl.labs.PrintJob.DuplexBinding;
import com.aspirecsl.labs.PrintJob.Orientation;

/** @author anoopr */
class PrintJobFormatter {

  PrintJobFormatter() {}

  File format(PrintJob printJob) throws IOException {
    final File printFile = new File(printJob.file().getName() + ".tmp~");

    try (BufferedReader inputReader = new BufferedReader(new FileReader(printJob.file()))) {
      try (Writer printFileWriter = Files.newBufferedWriter(printFile.toPath())) {
        printFileWriter.write(new String(initPCLModeCmd()));
        printFileWriter.write(new String(cpiCmd(printJob.cpi())));
        printFileWriter.write(new String(numberOfCopiesCmd(printJob.numberOfCopies())));
        printFileWriter.write(new String(duplexBindingCmd(printJob.duplexBinding())));
        printFileWriter.write(new String(pageOrientationCmd(printJob.orientation())));
        String aLine;
        while ((aLine = inputReader.readLine()) != null) {
          printFileWriter.write(aLine);
          printFileWriter.write("\r\n");
        }
        printFileWriter.write(new String(endJobCmd()));
      }
    }
    return printFile;
  }

  private byte[] initPCLModeCmd() {
    final String initPCLModeCmd = "%-12345X@PJL ENTER LANGUAGE = PCL";
    return getPCLCmdBytes(initPCLModeCmd);
  }

  private byte[] endJobCmd() {
    final String clearPrinterDisplayCmd = "%-12345X@PJL RDYMSG DISPLAY = \"\"";
    final byte[] clearPrinterDisplayCmdBytes = getPCLCmdBytes(clearPrinterDisplayCmd);
    final String endJobCmd = "%-12345X";
    final byte[] endJobCmdBytes = getPCLCmdBytes(endJobCmd);

    final byte[] endJobAndClearPrinterDisplayCmdBytes =
        new byte[clearPrinterDisplayCmdBytes.length + endJobCmdBytes.length];

    System.arraycopy(
        clearPrinterDisplayCmdBytes,
        0,
        endJobAndClearPrinterDisplayCmdBytes,
        0,
        clearPrinterDisplayCmdBytes.length);
    System.arraycopy(
        endJobCmdBytes,
        0,
        endJobAndClearPrinterDisplayCmdBytes,
        clearPrinterDisplayCmdBytes.length,
        endJobCmdBytes.length);
    return endJobAndClearPrinterDisplayCmdBytes;
  }

  private byte[] cpiCmd(int cpi) {
    final List<Byte> pclCmd = new ArrayList<>(Arrays.asList((byte) '(', (byte) 's'));
    for (byte b : convert(cpi)) {
      pclCmd.add(b);
    }
    pclCmd.add((byte) 'H');
    return getPCLCmdBytes(pclCmd);
  }

  private byte[] numberOfCopiesCmd(int numberOfCopies) {
    final List<Byte> pclCmd = new ArrayList<>(Arrays.asList((byte) '&', (byte) 'l'));
    for (byte b : convert(numberOfCopies)) {
      pclCmd.add(b);
    }
    pclCmd.add((byte) 'X');
    return getPCLCmdBytes(pclCmd);
  }

  private byte[] duplexBindingCmd(DuplexBinding binding) {
    final List<Byte> pclCmd = new ArrayList<>(Arrays.asList((byte) '&', (byte) 'l'));
    switch (binding) {
      case NONE:
        pclCmd.add((byte) '0');
        break;
      case LONG_EDGE:
        pclCmd.add((byte) '1');
        break;
      case SHORT_EDGE:
        pclCmd.add((byte) '2');
        break;
    }
    pclCmd.add((byte) 'S');
    return getPCLCmdBytes(pclCmd);
  }

  private byte[] pageOrientationCmd(Orientation orientation) {
    final List<Byte> pclCmd = new ArrayList<>(Arrays.asList((byte) '&', (byte) 'l'));
    switch (orientation) {
      case PORTRAIT:
        pclCmd.add((byte) '0');
        break;
      case LANDSCAPE:
        pclCmd.add((byte) '1');
        break;
    }
    pclCmd.add((byte) 'O');
    return getPCLCmdBytes(pclCmd);
  }

  private byte[] getPCLCmdBytes(String pclCmdStr) {
    final byte[] pclCmdBytes = pclCmdStr.getBytes();
    final byte[] pclFullCmdBytes = new byte[pclCmdStr.length() + 1];
    pclFullCmdBytes[0] = (byte) 0x1b;
    System.arraycopy(pclCmdBytes, 0, pclFullCmdBytes, 1, pclCmdBytes.length);
    return pclFullCmdBytes;
  }

  private byte[] getPCLCmdBytes(List<Byte> pclCmd) {
    final byte[] pclCmdBytes = new byte[pclCmd.size() + 1];
    pclCmdBytes[0] = (byte) 0x1b;

    int idx = 1;
    for (byte pclCmdPart : pclCmd) {
      pclCmdBytes[idx++] = pclCmdPart;
    }
    return pclCmdBytes;
  }

  private byte[] convert(int number) {
    return String.valueOf(number).getBytes();
  }
}
