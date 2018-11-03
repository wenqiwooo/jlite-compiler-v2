#!/bin/bash
# Test script  
# Author: Wu Wenqi    A0124278A

idx=1
syntax_tests_count=6
semantic_tests_count=5
class_path=$1

# Syntax tests

# while [ $idx -le $syntax_tests_count ]
# do
#     java -cp $class_path wwu.compiler.Jlc tests/syntax/test$idx > output.txt
#     res=$(diff --ignore-blank-lines --ignore-space-change output.txt tests/syntax/test"$idx"_output)
#     if [ "$res" == "" ]
#     then
#         echo "Test $idx passed."
#     else
#         echo "Test $idx failed."
#     fi
#     ((idx++))
# done
# rm output.txt

# Semantic tests

while [ $idx -le $semantic_tests_count ]
do
    java -cp $class_path wwu.compiler.Jlc tests/semantic/test$idx > output.txt
    res=$(diff --ignore-blank-lines --ignore-space-change output.txt tests/semantic/test"$idx"_output)
    if [ "$res" == "" ]
    then
        echo "Test $idx passed."
    else
        echo "Test $idx failed."
    fi
    ((idx++))
done

rm output.txt

