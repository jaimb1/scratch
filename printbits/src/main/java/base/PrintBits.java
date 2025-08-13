package base;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PrintBits {

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
          PrintBits.printByte(b);
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
      return Long.BYTES;
    }
  }

  private static void printHelp(final Options options) {
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

  private static Options buildOptions() {
    final Options options = new Options();
    options.addOption(Option.builder("b")
        .longOpt("bytes")
        .hasArg()
        .argName("bytes-to-print")
        .desc("The number of bytes to print")
        .type(Integer.class)
        .get());
    options.addOption(Option.builder("i")
        .longOpt("stdin")
        .desc("Read from stdin")
        .get());
    return options;
  }

  private static int getBytesToPrint(final Integer bytes, final long n) {
    return (bytes == null) ? getMinBytesRequired(n) : bytes;
  }

  private static void doPrint(final Integer bytes, final long n) {
    System.out.println(n);
    PrintBits.printBytes(n, getBytesToPrint(bytes, n));
    System.out.println();
  }

  private static void readFromPositionalArgs(
      final String[] positionalArgs,
      final Integer bytes) {
    Arrays.stream(positionalArgs)
        .mapToLong(Long::parseLong)
        .forEach(n -> doPrint(bytes, n));
  }

  private static void readFromStdin(final Integer bytes) {
    final Scanner scanner = new Scanner(System.in);
    while (scanner.hasNext()) {
      final long n = scanner.nextLong();
      doPrint(bytes, n);
    }
  }

  public static void main(final String[] args) {
    final Options options = buildOptions();
    try {
      final CommandLineParser parser = new DefaultParser();
      final CommandLine cmd = parser.parse(options, args, true);

      final Integer bytes = cmd.getParsedOptionValue(options.getOption("b"));
      final boolean fromStdin = cmd.hasOption(options.getOption("i"));
      final String[] positionalArgs = cmd.getArgs();

      if (fromStdin || positionalArgs.length == 0) {
        readFromStdin(bytes);
      } else {
        readFromPositionalArgs(positionalArgs, bytes);
      }
    } catch (final ParseException | NumberFormatException e) {
      printHelp(options);
    }
  }
}
