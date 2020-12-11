package com.reason.ide.intentions;

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class IntroduceLocalVariableIntention extends BaseElementAtCaretIntentionAction {

  @Nls
  @NotNull
  @Override
  public String getText() {
    return "Introduce local variable";
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return "Introduce local variable";
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
//    new CSharpIntroduceLocalVariableHandler().invoke(project, editor, element.getContainingFile(), null);
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    return false;
  }
}
