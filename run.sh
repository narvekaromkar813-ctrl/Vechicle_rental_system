#!/bin/bash
# Shell script to compile and run the Vehicle Rental Management System
echo "================================================================"
echo " Starting Vehicle Rental Management System - Case Study 12     "
echo " ITM Skills University - School of Future Tech (B.Tech CSE)     "
echo "================================================================"

mkdir -p out
echo "[*] Compiling Java source files..."
javac -d out $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "[+] Compilation successful! Launching GUI application..."
    java -cp out com.vehiclerental.Main
else
    echo "[-] Compilation failed. Please check error output above."
    exit 1
fi
