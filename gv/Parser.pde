//This is the format:
//given <testname> test <number>, <student> <outcome> -- <witness>

//I know that the data shouldn't just be in a public variable, but I'm not yet
//sure where to put it.

import java.util.regex.*;

class Parser {
  public TestCase testCases[];

  public Parser(String filename) {
    String lines[] = loadStrings(filename);
    testCases = new TestCase[lines.length];

    Pattern pattern = Pattern.compile("given (\\S+)\\s+test\\s+(\\d+),\\s+(\\w+)\\s+(\\w+)(.*)");

    for (int i=0; i<lines.length; i++) {
      Matcher m = pattern.matcher(lines[i]);
      if(m.find() && m.groupCount() == 5) {
        String name = m.group(1);
        int number = int(m.group(2));
        String student = m.group(3);
        String outcome = m.group(4);
        String witness = m.group(5);
        if(witness.length() == 0) {
          witness = null;
        }
        Boolean didPass;
        if(outcome.equals("passed")) {
          didPass = true;
        } else if(outcome.equals("failed")) {
          didPass = false;
        } else {
          invalidLine(lines[i]);
          continue;
        }
        testCases[i] = new TestCase(name, number, student, didPass, witness);
      } else {
        invalidLine(lines[i]);
      }
    }
  }

  private void invalidLine(String line) {
    println("Invalid line: " + line);
  }
}

