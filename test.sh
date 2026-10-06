#!/bin/bash
# Shell script to compile and run the Automated Integration Test Suite
echo "================================================================"
echo " Running Automated Verification Suite - Case Study 12           "
echo "================================================================"

mkdir -p out
javac -d out $(find src -name "*.java")

if [ $? -eq 0 ]; then
    java -cp out com.vehiclerental.test.SystemIntegrationTest
else
    echo "[-] Compilation failed."
    exit 1
fi
