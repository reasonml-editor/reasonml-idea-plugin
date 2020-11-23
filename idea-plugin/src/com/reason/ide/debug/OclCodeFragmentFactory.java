package com.reason.ide.debug;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OclCodeFragmentFactory {
    private final Project m_project;

    public OclCodeFragmentFactory(Project project) {
        m_project = project;
    }

    public PsiFile createExpressionCodeFragment(@NotNull String text, @Nullable PsiElement context, @Nullable PsiType expectedType, boolean isPhysical) {
        return null; //return Factory.createFileFromText(m_project, OclLanguage.INSTANCE, text);
    }
}