package com.reason.lang.core.psi.impl;

import com.intellij.lang.*;
import com.intellij.navigation.*;
import com.intellij.psi.*;
import com.intellij.psi.search.*;
import com.intellij.psi.stubs.*;
import com.intellij.psi.util.*;
import com.intellij.util.*;
import com.reason.ide.files.*;
import com.reason.ide.search.*;
import com.reason.lang.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.PsiType;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.stub.*;
import com.reason.lang.core.type.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.*;

import static com.reason.lang.core.ORFileType.*;
import static java.util.Collections.*;

public class PsiInnerModuleImpl extends PsiTokenStub<ORTypes, PsiModule, PsiModuleStub> implements PsiInnerModule {
    // region Constructors
    public PsiInnerModuleImpl(@NotNull ORTypes types, @NotNull ASTNode node) {
        super(types, node);
    }

    public PsiInnerModuleImpl(@NotNull ORTypes types, @NotNull PsiModuleStub stub, @NotNull IStubElementType nodeType) {
        super(types, stub, nodeType);
    }
    // endregion

    // region NamedElement
    private @Nullable PsiElement getNameIdentifier() {
        return findChildByClass(PsiUpperIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedPath
    @Override
    public @Nullable String[] getPath() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override public @Nullable String[] getQualifiedNameAsPath() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedNameAsPath();
        }

        return ORUtil.getQualifiedNameAsPath(this);
    }

    @Override
    public @NotNull String getModuleName() {
        String name = getName();
        return name == null ? "" : name;
    }

    @Override
    public boolean isInterface() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.isInterface();
        }

        return ((FileBase) getContainingFile()).isInterface();
    }

    @Override
    public @Nullable PsiFunctorCall getFunctorCall() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiFunctorCall.class);
    }

    public @Nullable PsiElement getBody() {
        return ORUtil.findImmediateFirstChildOfAnyClass(this, PsiScopedExpr.class, PsiStruct.class);
    }

    public @Nullable PsiModuleType getModuleType() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiModuleType.class);
    }

    @Override
    public @NotNull Collection<PsiModule> getModules() {
        PsiElement body = getBody();
        return body == null
                ? emptyList()
                : PsiTreeUtil.getStubChildrenOfTypeAsList(body, PsiInnerModule.class);
    }

    @Override
    public @Nullable PsiModule getModuleExpression(@Nullable String name) {
        if (name != null) {
            for (PsiModule module : getModules()) {
                if (name.equals(module.getName())) {
                    return module;
                }
            }
        }

        return null;
    }

    @Override
    public @NotNull Collection<PsiNamedElement> getExpressions(@NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter) {
        Collection<PsiNamedElement> result = emptyList();

        PsiFinder psiFinder = PsiFinder.getInstance(getProject());

        String alias = getAlias();
        if (alias != null) {
            // Resolve alias and get expressions on alias
            PsiElement lastChild = getLastChild();
            PsiReference reference = lastChild.getReference();
            PsiElement resolve = reference == null ? null : reference.resolve();
            PsiElement resolvedElement = (resolve instanceof PsiUpperIdentifier) ? resolve.getParent() : resolve;
            if (resolvedElement instanceof PsiModule) {
                result = ((PsiModule) resolvedElement).getExpressions(eScope, filter);
            }
        } else {
            PsiModuleType moduleType = getModuleType();
            if (moduleType == null) {
                PsiElement body = getBody();
                if (body == null) {
                    PsiFunctorCall functorCall = ORUtil.findImmediateFirstChildOfClass(this, PsiFunctorCall.class);
                    if (functorCall != null) {
                        result = new ArrayList<>();
                        // Include all expressions from functor
                        QNameFinder qnameFinder = PsiFinder.getQNameFinder(getLanguage());

                        Set<String> potentialPaths = qnameFinder.extractPotentialPaths(functorCall);
                        for (String potentialPath : potentialPaths) {
                            Set<PsiModule> modules =
                                    psiFinder.findModulesFromQn(
                                            potentialPath + "." + functorCall.getFunctorName(),
                                            true,
                                            interfaceOrImplementation,
                                            GlobalSearchScope.allScope(getProject()));
                            for (PsiModule module : modules) {
                                result.addAll(module.getExpressions(eScope, filter));
                            }
                        }

                        Set<PsiModule> modules =
                                psiFinder.findModulesFromQn(
                                        functorCall.getFunctorName(),
                                        true,
                                        interfaceOrImplementation,
                                        GlobalSearchScope.allScope(getProject()));
                        for (PsiModule module : modules) {
                            result.addAll(module.getExpressions(eScope, filter));
                        }
                    }
                } else {
                    result = new ArrayList<>();
                    PsiElement element = body.getFirstChild();
                    while (element != null) {
                        if (element instanceof PsiNamedElement
                                && (filter == null || filter.accept((PsiNamedElement) element))) {
                            result.add((PsiNamedElement) element);
                        }
                        element = element.getNextSibling();
                    }
                }
            } else {
                result = new ArrayList<>();
                PsiElement element = moduleType.getFirstChild();
                while (element != null) {
                    if (element instanceof PsiNamedElement
                            && (filter == null || filter.accept((PsiNamedElement) element))) {
                        result.add((PsiNamedElement) element);
                    }
                    element = element.getNextSibling();
                }
            }
        }

        return result;
    }

    @Override
    public @Nullable PsiType getTypeExpression(@Nullable String name) {
        PsiElement body = name == null ? null : getBody();
        if (body != null) {
            ExpressionFilter expressionFilter = element -> element instanceof PsiType && name.equals(element.getName());
            Collection<PsiNamedElement> expressions = getExpressions(ExpressionScope.all, expressionFilter);
            if (!expressions.isEmpty()) {
                return (PsiType) expressions.iterator().next();
            }
        }

        return null;
    }

    @Override
    public @Nullable PsiLet getLetExpression(@Nullable String name) {
        PsiElement body = name == null ? null : getBody();
        if (body != null) {
            ExpressionFilter expressionFilter = element -> element instanceof PsiLet && name.equals(element.getName());
            Collection<PsiNamedElement> expressions = getExpressions(ExpressionScope.all, expressionFilter);
            if (!expressions.isEmpty()) {
                return (PsiLet) expressions.iterator().next();
            }
        }

        return null;
    }

    @Override
    public @Nullable PsiVal getValExpression(@Nullable String name) {
        PsiElement body = name == null ? null : getBody();
        if (body != null) {
            ExpressionFilter expressionFilter = element -> element instanceof PsiVal && name.equals(element.getName());
            Collection<PsiNamedElement> expressions = getExpressions(ExpressionScope.all, expressionFilter);
            if (!expressions.isEmpty()) {
                return (PsiVal) expressions.iterator().next();
            }
        }

        return null;
    }

    private boolean isModuleTypeOf() {
        PsiElement nextSibling = ORUtil.nextSibling(getFirstChild());
        PsiElement nextNextSibling = ORUtil.nextSibling(nextSibling);
        return nextSibling != null
                && nextNextSibling != null
                && nextSibling.getNode().getElementType() == m_types.TYPE
                && nextNextSibling.getNode().getElementType() == m_types.OF;
    }

    private @Nullable PsiModule findReferencedModuleTypeOf() {
        PsiElement of = ORUtil.findImmediateFirstChildOfType(this, m_types.OF);

        if (of != null) {
            // find latest module name
            PsiElement module = ORUtil.nextSiblingWithTokenType(of, m_types.C_UPPER_SYMBOL);
            PsiElement moduleNextSibling = module == null ? null : module.getNextSibling();
            while (moduleNextSibling != null
                    && moduleNextSibling.getNode().getElementType() == m_types.DOT) {
                PsiElement element = moduleNextSibling.getNextSibling();
                if (element != null && element.getNode().getElementType() == m_types.C_UPPER_SYMBOL) {
                    module = element;
                    moduleNextSibling = module.getNextSibling();
                } else {
                    moduleNextSibling = null;
                }
            }

            if (module != null) {
                PsiReference reference = module.getReference();
                PsiElement resolvedElement = reference == null ? null : reference.resolve();
                if (resolvedElement instanceof PsiUpperIdentifier) {
                    PsiElement resolvedModule = resolvedElement.getParent();
                    if (resolvedModule instanceof PsiModule) {
                        return (PsiModule) resolvedModule;
                    }
                }
            }
        }

        return null;
    }

    public ItemPresentation getPresentation() {
        boolean isModuleTypeOf = isModuleTypeOf();
        PsiModule referencedModuleType = isModuleTypeOf ? findReferencedModuleTypeOf() : null;

        return new ItemPresentation() {
            @Override
            public @Nullable String getPresentableText() {
                if (isModuleTypeOf) {
                    if (referencedModuleType == null) {
                        PsiElement of =
                                ORUtil.findImmediateFirstChildOfType(PsiInnerModuleImpl.this, m_types.OF);
                        assert of != null;
                        return getText().substring(of.getStartOffsetInParent() + 3);
                    }
                    return referencedModuleType.getName();
                }
                return getName();
            }

            @Override
            public @NotNull String getLocationString() {
                return referencedModuleType == null
                        ? ""
                        : referencedModuleType.getContainingFile().getName();
            }

            @Override
            public @NotNull Icon getIcon(boolean unused) {
                return isModuleType()
                        ? ORIcons.MODULE_TYPE
                        : (isInterface() ? ORIcons.INNER_MODULE_INTF : ORIcons.INNER_MODULE);
            }
        };
    }

    @Override
    public boolean isComponent() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.isComponent();
        }

        return ModuleHelper.isComponent(getBody());
    }

    @Override
    public boolean isModuleType() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.isModuleType();
        }

        PsiElement psiElement = ORUtil.nextSibling(getFirstChild());
        return psiElement != null && psiElement.getNode().getElementType() == m_types.TYPE;
    }

    @Override
    public boolean isFunctorCall() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.isFunctorCall();
        }

        return ORUtil.findImmediateFirstChildOfType(this, m_types.C_FUNCTOR_CALL) != null;
    }

    @Override
    public @NotNull PsiElement getNavigationElement() {
        if (isComponent()) {
            PsiLet make = getLetExpression("make");
            if (make != null) {
                return make;
            }
        }
        return super.getNavigationElement();
    }

    @Override
    public @Nullable String getAlias() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getAlias();
        }

        PsiElement eq = findChildByType(m_types.EQ);
        if (eq != null) {
            return ORUtil.computeAlias(eq.getNextSibling(), getLanguage(), false);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Module " + getQualifiedName();
    }
}
