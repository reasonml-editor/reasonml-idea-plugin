package com.reason.ocaml;

import com.reason.BaseParsingTestCase;
import com.reason.lang.core.psi.PsiInclude;
import com.reason.lang.ocaml.OclParserDefinition;

public class IncludeParsingTest extends BaseParsingTestCase {
    public IncludeParsingTest() {
        super("", "ml", new OclParserDefinition());
    }

    public void testInclude() {
        PsiInclude e = first(parseCode("include Belt").getIncludeExpressions());

        assertNotNull(e);
        assertEquals("Belt", e.getName());
    }
}
