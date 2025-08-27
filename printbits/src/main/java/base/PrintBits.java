package base;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PrintBits {
  private static final Options options;
  private static final OptionGroup bytesDerivationGroup;
  private static final Option bytesOption;
  private static final Option typeOption;
  private static final Option stdInOption;
  private static final Map<String, Integer> supportedTypes = Map.of(
      "byte", Byte.BYTES,
      "short", Short.BYTES,
      "int", Integer.BYTES,
      "long", Long.BYTES);

  static {
    options = new Options();
    bytesDerivationGroup = new OptionGroup();
    bytesOption = Option.builder("b")
        .longOpt("bytes")
        .hasArg()
        .argName("bytes-to-print")
        .desc("The number of bytes to print")
        .type(Integer.class)
        .get();
    typeOption = Option.builder("t")
        .longOpt("type")
        .hasArg()
        .argName("type-name")
        .desc("The Java primitive data type to use")
        .type(String.class)
        .get();
    stdInOption = Option.builder("i")
        .longOpt("stdin")
        .desc("Read from stdin")
        .get();
    options.addOption(stdInOption);

    bytesDerivationGroup.addOption(bytesOption);
    bytesDerivationGroup.addOption(typeOption);
    options.addOptionGroup(bytesDerivationGroup);
  }

  private static void printByte(final byte b) {
    IntStream.range(0, Byte.SIZE)
        // print most significant bits first
        .map(i -> Byte.SIZE - i - 1)
        .forEach(i -> {
          final byte bit = (byte) ((b >>> i) & 1);
          if (i == 3) {
            System.out.print(" ");
          }
          System.out.print(bit);
        });
  }

  static void printBytes(
      final long n,
      final int bytesToPrint) {
    IntStream.range(0, bytesToPrint)
        // print most significant byte first
        .map(i -> bytesToPrint - i - 1)
        .forEach(i -> {
          final byte b = (byte) (n >>> (i * Byte.SIZE));
          printByte(b);
          if (i != 0) {
            System.out.print(" ");
          }
        });
  }

  static int getMinBytesRequired(final long n) {
    if ((n < 0 && n >= Byte.MIN_VALUE) ||
        (n >= 0 && n <= Byte.MAX_VALUE)) {
      return Byte.BYTES;
    } else if (
        (n < 0 && n >= Short.MIN_VALUE) ||
        (n >= 0 && n <= Short.MAX_VALUE)) {
      return Short.BYTES;
    } else if (
        (n < 0 && n >= Integer.MIN_VALUE) ||
        (n >= 0 && n <= Integer.MAX_VALUE)) {
      return Integer.BYTES;
    } else {
      // if we successfully parsed n then it must fit into a long
      return Long.BYTES;
    }
  }

  private static void printHelp() {
    final HelpFormatter formatter = HelpFormatter.builder().get();
    try {
      formatter.printHelp(
          "printbits [options] num1 num2...",
          "Print bits in twos complement form",
          options,
          "",
          false);
    } catch (final IOException e) {
      System.out.println("Unabled to print help instructions: " + e.getMessage());
    }
  }

  private static int getBytesToPrint(
      final Integer bytes,
      final long n) throws ParseException {
    final int minBytes = getMinBytesRequired(n);
    if (bytes == null) {
      return minBytes;
    } else if (bytes < minBytes) {
      throw new ParseException(n + " requires more than " + bytes + " to print");
    } else {
      return bytes;
    }
  }

  private static void doPrint(
      final Integer bytes,
      final long n) throws ParseException {
    System.out.println(n);
    printBytes(n, getBytesToPrint(bytes, n));
    System.out.println();
  }

  private static void readFromPositionalArgs(
      final String[] positionalArgs,
      final Integer bytes) throws ParseException {
    for (final String positionalArg : positionalArgs) {
      long n = Long.parseLong(positionalArg);
      doPrint(bytes, n);
    }
  }

  private static void readFromStdin(final Integer bytes) throws ParseException {
    final Scanner scanner = new Scanner(System.in);
    while (scanner.hasNext()) {
      final long n = scanner.nextLong();
      doPrint(bytes, n);
    }
  }

  public static Integer typeNameToBytes(final String typeName) throws ParseException {
    final Integer bytes = supportedTypes.get(typeName);
    if (bytes == null) {
      throw new ParseException("Unsupported type: " + typeName);
    }
    return bytes;
  }

  private static Integer getBytesFromArgs(final CommandLine cmd) throws ParseException {
    final Option selectedOption = options.getOption(bytesDerivationGroup.getSelected());
    if (selectedOption == null) {
      return null;
    } else if (selectedOption.equals(bytesOption)) {
      return cmd.getParsedOptionValue(bytesOption);
    } else {
      return typeNameToBytes(cmd.getParsedOptionValue(typeOption));
    }
  }

  public static void main(final String[] args) {
    try {
      final CommandLineParser parser = new DefaultParser();
      final CommandLine cmd = parser.parse(options, args, true);

      final Integer bytes = getBytesFromArgs(cmd);
      final boolean fromStdin = cmd.hasOption(options.getOption("i"));
      final String[] positionalArgs = cmd.getArgs();

      if (fromStdin || positionalArgs.length == 0) {
        readFromStdin(bytes);
      } else {
        readFromPositionalArgs(positionalArgs, bytes);
      }
    } catch (final ParseException | NumberFormatException e) {
      System.out.println("Invalid input: " + e.getMessage());
      printHelp();
    }
  }
}
