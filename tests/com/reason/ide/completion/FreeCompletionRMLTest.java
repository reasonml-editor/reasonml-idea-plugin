package com.reason.ide.completion;

import com.intellij.codeInsight.completion.*;
import com.reason.ide.*;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class FreeCompletionRMLTest extends ORBasePlatformTestCase {
    public void test_pervasives() {
        configureCode("pervasives.mli", "val int_of_string : str -> int");
        configureCode("belt_Array.mli", "val length: t -> int");
        configureCode("belt.ml", "module Array = Belt_Array");

        configureCode("Dummy.re", "let x = <caret>");

        myFixture.complete(CompletionType.BASIC, 1);
        List<String> elements = myFixture.getLookupElementStrings();

        assertSize(5, elements);
        assertContainsElements(elements, "int_of_string", "Belt", "Belt_Array", "Pervasives", "x");
    }

    public void test_deconstruction() {
        configureCode("Dummy.re", "let (first, second) = myVar; <caret>");

        myFixture.complete(CompletionType.BASIC, 1);
        List<String> elements = myFixture.getLookupElementStrings();

        assertContainsElements(elements, "first", "second");
    }

    public void test_let_private_from_outside() {
        configureCode("A.re", "let x%private = 1;");
        configureCode("B.re", "open A; <caret>");

        myFixture.complete(CompletionType.BASIC, 1);
        List<String> elements = myFixture.getLookupElementStrings();

        assertDoesntContain(elements, "x");
    }

    public void test_let_private_from_inside() {
        configureCode("A.re", "let x%private = 1; <caret>");

        myFixture.complete(CompletionType.BASIC, 1);
        List<String> elements = myFixture.getLookupElementStrings();

        assertContainsElements(elements, "x");
    }
}
