class TestCase {
  public String name;
  public int number;
  public String student;
  public Boolean didPass; //True if the test succeeded
  public String witness;

  public TestCase(String name, int number, String student, Boolean passed,
      String witness) {
    this.name = name;
    this.number = number;
    this.student = student;
    this.didPass = passed;
    this.witness = witness;
  }
}

