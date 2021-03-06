// This is a generated file. Not intended for manual editing.
package com.reason.lang.core.psi.ocamlyacc.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElementVisitor;
import com.reason.lang.core.psi.ocamlyacc.OclYaccHeader;
import com.reason.lang.core.psi.ocamlyacc.OclYaccVisitor;
import com.reason.lang.ocamlyacc.OclYaccPsiImplUtil;
import org.jetbrains.annotations.NotNull;

public class OclYaccHeaderImpl extends ASTWrapperPsiElement implements OclYaccHeader {

  public OclYaccHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OclYaccVisitor visitor) {
    visitor.visitHeader(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OclYaccVisitor) accept((OclYaccVisitor) visitor);
    else super.accept(visitor);
  }

  public ItemPresentation getPresentation() {
    return OclYaccPsiImplUtil.getPresentation(this);
  }
}
