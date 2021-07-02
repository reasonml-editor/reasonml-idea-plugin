package com.reason.lang.rescript;

import com.intellij.psi.*;
import com.reason.ide.files.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.psi.PsiAnnotation;
import com.reason.lang.core.psi.PsiType;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class ModuleParsingTest extends ResParsingTestCase {
    public void test_empty() {
        Collection<PsiModule> modules = moduleExpressions(parseCode("module M = {}"));

        assertEquals(1, modules.size());
        PsiInnerModule e = (PsiInnerModule) first(modules);
        assertEquals("M", e.getName());
        assertEquals("{}", e.getBody().getText());
    }

    public void test_alias() {
        PsiModule module = firstOfType(parseCode("module M = Y\n M.x"), PsiInnerModule.class);

        assertEquals("M", module.getName());
        assertEquals("Y", module.getAlias());
    }

    public void test_module_type() {
        PsiInnerModule module = firstOfType(parseCode("module type RedFlagsSig = {}"), PsiInnerModule.class);

        assertEquals("RedFlagsSig", module.getName());
        assertTrue(module.isModuleType());
    }

    public void test_module() {
        PsiFile file = parseCode("module Styles = { open Css\n let y = 1 }");
        PsiInnerModule module = (PsiInnerModule) first(moduleExpressions(file));

        assertEquals(1, expressions(file).size());
        assertEquals("Styles", module.getName());
        assertEquals("{ open Css\n let y = 1 }", module.getBody().getText());
    }

    public void test_inline_interface() {
        PsiFile file = parseCode("module Router: { let watchUrl: (url => unit) => watcherID }");
        PsiInnerModule module = (PsiInnerModule) first(moduleExpressions(file));

        assertEquals(1, expressions(file).size());
        assertEquals("Router", module.getName());
        assertEquals("{ let watchUrl: (url => unit) => watcherID }", module.getModuleType().getText());
        assertNull(module.getBody());
    }

    public void test_inline_interface_body() {
        PsiInnerModule e =
                firstOfType(parseCode("module M: { type t } = { type t = int }"), PsiInnerModule.class);

        assertEquals("M", e.getName());
        assertEquals("{ type t }", e.getModuleType().getText());
        assertEquals("{ type t = int }", e.getBody().getText());
    }

    public void test_moduleOpenVariant() {
        FileBase file = parseCode("ModelActions.UserCapabilitiesLoaded.( UserCapabilitiesBuilder.( ) ),");
        assertEquals(6, childrenCount(file));
    }

    public void test_annotation_after() {
        FileBase e = parseCode("module M = {}\n@module(\"x\")");

        PsiModule m = ORUtil.findImmediateFirstChildOfClass(e, PsiModule.class);
        PsiAnnotation a = ORUtil.findImmediateFirstChildOfClass(e, PsiAnnotation.class);

        assertEquals("module M = {}", m.getText());
        assertEquals("@module", a.getName());
    }
}
