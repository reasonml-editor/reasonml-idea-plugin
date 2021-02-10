package com.reason.ide.search.index;

import com.intellij.psi.stubs.*;
import com.reason.lang.core.psi.*;
import org.jetbrains.annotations.*;

public class ParameterFqnIndex extends IntStubIndexExtension<PsiParameter> {
    private static final int VERSION = 3;
    private static final ParameterFqnIndex INSTANCE = new ParameterFqnIndex();

    public static @NotNull ParameterFqnIndex getInstance() {
        return INSTANCE;
    }

    @Override
    public int getVersion() {
        return super.getVersion() + VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiParameter> getKey() {
        return IndexKeys.PARAMETERS_FQN;
    }
}
