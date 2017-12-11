package com.reason.lang.core.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.reason.lang.core.stub.ModuleStub;

public interface Module extends NamedElement, StubBasedPsiElement<ModuleStub> {
    PsiScopedExpr getModuleBody();
}