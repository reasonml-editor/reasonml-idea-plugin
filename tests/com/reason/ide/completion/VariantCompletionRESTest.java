package com.reason.ide.completion;

import com.reason.ide.*;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class VariantCompletionRESTest extends ORBasePlatformTestCase {
    public void test_basic() {
        configureCode("A.res", "type color = | Black | Red;");
        configureCode("B.res", "A.<caret>");

        myFixture.completeBasic();
        List<String> elements = myFixture.getLookupElementStrings();

        assertSize(3, elements);
        assertContainsElements(elements, "color", "Black", "Red");
    }
}
