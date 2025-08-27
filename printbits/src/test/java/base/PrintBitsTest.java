package base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrintBitsTest {
  private static PrintStream originalStdOut;
  private static InputStream originalStdin;
  private static ByteArrayOutputStream testOut;

  @BeforeAll
  static void beforeAll() {
    originalStdOut = System.out;
    originalStdin = System.in;
    testOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOut));
  }

  @AfterAll
  static void afterAll() throws IOException {
    System.setOut(originalStdOut);
    System.setIn(originalStdin);
    testOut.close();
  }

  @BeforeEach
  void beforeEach() {
    testOut.reset();
  }

  @Test
  void testPrintBytesByte() {
    PrintBits.printBytes(-1, Byte.BYTES);
    assertEquals("1111 1111", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(1, Byte.BYTES);
    assertEquals("0000 0001", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(-44, Byte.BYTES);
    assertEquals("1101 0100", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Byte.MIN_VALUE, Byte.BYTES);
    assertEquals("1000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(0, Byte.BYTES);
    assertEquals("0000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(3, Byte.BYTES);
    assertEquals("0000 0011", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Byte.MAX_VALUE, Byte.BYTES);
    assertEquals("0111 1111", testOut.toString());
  }

  @Test
  void testPrintBytesShort() {
    PrintBits.printBytes(-1, Short.BYTES);
    assertEquals("1111 1111 1111 1111", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(1, Short.BYTES);
    assertEquals("0000 0000 0000 0001", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(-14444, Short.BYTES);
    assertEquals("1100 0111 1001 0100", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Short.MIN_VALUE, Short.BYTES);
    assertEquals("1000 0000 0000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(0, Short.BYTES);
    assertEquals("0000 0000 0000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(3, Short.BYTES);
    assertEquals("0000 0000 0000 0011", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Short.MAX_VALUE, Short.BYTES);
    assertEquals("0111 1111 1111 1111", testOut.toString());
  }

  @Test
  void testPrintBytesInteger() {
    PrintBits.printBytes(-1, Integer.BYTES);
    assertEquals("1111 1111 1111 1111 1111 1111 1111 1111", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(1, Integer.BYTES);
    assertEquals("0000 0000 0000 0000 0000 0000 0000 0001", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(-1444444444, Integer.BYTES);
    assertEquals("1010 1001 1110 0111 1000 0110 1110 0100", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Integer.MIN_VALUE, Integer.BYTES);
    assertEquals("1000 0000 0000 0000 0000 0000 0000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(0, Integer.BYTES);
    assertEquals("0000 0000 0000 0000 0000 0000 0000 0000", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(3, Integer.BYTES);
    assertEquals("0000 0000 0000 0000 0000 0000 0000 0011", testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Integer.MAX_VALUE, Integer.BYTES);
    assertEquals("0111 1111 1111 1111 1111 1111 1111 1111", testOut.toString());
  }

  @Test
  void testPrintBytesLong() {
    PrintBits.printBytes(-1, Long.BYTES);
    assertEquals(
        "1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(1, Long.BYTES);
    assertEquals(
        "0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0001",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(-1444444444444444444L, Long.BYTES);
    assertEquals(
        "1110 1011 1111 0100 0100 1101 0110 1110 0100 0111 0001 1010 0011 1000 1110 0100",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Long.MIN_VALUE, Long.BYTES);
    assertEquals(
        "1000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(0, Long.BYTES);
    assertEquals(
        "0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(3, Long.BYTES);
    assertEquals(
        "0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0011",
        testOut.toString());
    testOut.reset();
    PrintBits.printBytes(Long.MAX_VALUE, Long.BYTES);
    assertEquals(
        "0111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111",
        testOut.toString());
  }

  @Test
  void testMainPositionalArgParsing() {
    final String[] args = new String[] {"-44", "-14444", "-1444444444", "-1444444444444444444"};
    PrintBits.main(args);

    assertEquals(
        "-44" + System.lineSeparator() +
            "1101 0100" + System.lineSeparator() +
            "-14444" + System.lineSeparator() +
            "1100 0111 1001 0100" + System.lineSeparator() +
            "-1444444444" + System.lineSeparator() +
            "1010 1001 1110 0111 1000 0110 1110 0100" + System.lineSeparator() +
            "-1444444444444444444" + System.lineSeparator() +
            "1110 1011 1111 0100 0100 1101 0110 1110 0100 0111 0001 1010 0011 1000 1110 0100" + System.lineSeparator(),
        testOut.toString());
  }

  @Test
  void testMainBytesArg() {
    final String[] args = new String[] {"-b", "3", "-1"};
    PrintBits.main(args);

    assertEquals(
        "-1" + System.lineSeparator() +
            "1111 1111 1111 1111 1111 1111" + System.lineSeparator(),
        testOut.toString());
  }

  @Test
  void testMainTypeArg() {
    final String[] args = new String[] {"-t", "int", "-1"};
    PrintBits.main(args);

    assertEquals(
        "-1" + System.lineSeparator() +
            "1111 1111 1111 1111 1111 1111 1111 1111" + System.lineSeparator(),
        testOut.toString());
  }

  @Test
  void testMainStdin() {
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream("-1".getBytes(StandardCharsets.UTF_8));
    System.setIn(inputStream);
    final String[] args = new String[] {"-b", "3"};
    PrintBits.main(args);

    assertEquals(
        "-1" + System.lineSeparator() +
            "1111 1111 1111 1111 1111 1111" + System.lineSeparator(),
        testOut.toString());
  }

  @Test
  void testMainHelpText() {
    PrintBits.main(new String[] {"-h"});
    assertTrue(testOut.toString().startsWith(" usage:"));
    testOut.reset();
    PrintBits.main(new String[] {"--help"});
    assertTrue(testOut.toString().startsWith(" usage:"));
  }

  @Test
  void testMainOverflowInvalidInput() {
    PrintBits.main(new String[] {"-b", "1", "-129"});
    assertTrue(testOut.toString().startsWith("-129" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-b", "1", "128"});
    assertTrue(testOut.toString().startsWith("128" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-b", "2", "-32769"});
    assertTrue(testOut.toString().startsWith("-32769" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-b", "2", "32768"});
    assertTrue(testOut.toString().startsWith("32768" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-t", "int", "-2147483649"});
    assertTrue(testOut.toString().startsWith("-2147483649" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-t", "int", "2147483649"});
    assertTrue(testOut.toString().startsWith("2147483649" + System.lineSeparator() + "Invalid input:"));
    testOut.reset();
    // we use Long.parse() so it will fail to parse right away
    PrintBits.main(new String[] {"-t", "long", "-9223372036854775809"});
    assertTrue(testOut.toString().startsWith("Invalid input:"));
    testOut.reset();
    PrintBits.main(new String[] {"-t", "long", "9223372036854775808"});
    assertTrue(testOut.toString().startsWith("Invalid input:"));
    testOut.reset();
  }

  @Test
  void testMainTypeAndBytesArgsInvalidInput() {
    final String[] args = new String[] {"-b", "2", "-t", "int", "-1"};
    PrintBits.main(args);

    assertTrue(testOut.toString().startsWith("Invalid input:"));
  }

  @Test
  void testMainUnrecognizedTypeNameArg() {
    final String[] args = new String[] {"-t", "foo", "-1"};
    PrintBits.main(args);

    assertTrue(testOut.toString().startsWith("Invalid input:"));
  }
}
