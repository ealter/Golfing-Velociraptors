#!/usr/bin/python

#Format:
# given <testname> test <number>, <student> <outcome> -- <witness>

#Attempts to parse witness data from the test cases provided on stdin

import sys
import re

def run():
  testcases = sys.stdin.readlines()

  regex = re.compile(r"given (\S+)\s+test\s+(\d+),\s+(\w+)\s+(\w+)\s*(.*)")

  tests = {}

  for t in testcases:
    data = regex.match(t)
    if data is None:
      print "Invalid test case: " + t
    elif data.group(4) == "failed":
      testname = data.group(1)
      if testname not in tests:
        tests[testname] = []
      witness = data.group(5)[3:] #Get rid of dashes
      tests[testname].append(witness)

  for key in tests.keys():
    print "Test is " + key
    makeUniqueWitnesses(tests[key])
    unique = set(tests[key])
    if len(unique) <= 3:
      print "not many results"
    else:
      print '\n'.join(set(tests[key]))
    print ""

def makeUniqueWitnesses(test):
  for i in range(len(test)):
    witness = test[i]
    index = witness.rfind("caused a run-time error: ")
    if index != -1:
      test[i] = witness[index:]

run()
