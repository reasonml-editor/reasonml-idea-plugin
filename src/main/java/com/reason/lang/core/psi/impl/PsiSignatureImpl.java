package com.reason.lang.core.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.reason.ide.files.FileHelper;
import com.reason.lang.core.HMSignature;
import com.reason.lang.core.psi.PsiSignature;
import org.jetbrains.annotations.NotNull;

public class PsiSignatureImpl extends ASTWrapperPsiElement implements PsiSignature {

    public PsiSignatureImpl(ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public HMSignature asHMSignature() {
        PsiFile containingFile = getContainingFile();
        return new HMSignature(FileHelper.isOCaml(containingFile.getFileType()), getText());
    }

    @Override
    public String toString() {
        return "Signature";
    }
}
