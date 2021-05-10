package com.reason.lang.core.psi;

import com.intellij.psi.*;
import com.reason.lang.core.stub.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PsiLet extends PsiVar, PsiSignatureElement, PsiInferredType, PsiQualifiedPathElement, NavigatablePsiElement, PsiStructuredElement, StubBasedPsiElement<PsiLetStub> {
    @Nullable
    PsiLetBinding getBinding();

    @Nullable
    PsiFunction getFunction();

    boolean isRecord();

    boolean isJsObject();

    @NotNull
    Collection<PsiRecordField> getRecordFields();

    boolean isScopeIdentifier();

    @Nullable
    String getAlias();

    @NotNull
    Collection<PsiElement> getScopeChildren();

    boolean isDeconstruction();

    @NotNull
    List<PsiElement> getDeconstructedElements();

    boolean isPrivate();
}
