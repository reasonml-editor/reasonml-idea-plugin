package com.reason.ide.debug.conf;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class OclRunConfigurationProducer extends LazyRunConfigurationProducer<OclRunConfiguration> {

    @Override
    protected boolean setupConfigurationFromContext(@NotNull OclRunConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull OclRunConfiguration configuration, @NotNull ConfigurationContext context) {
        return false;
    }

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return ConfigurationTypeUtil.findConfigurationType(OclConfigurationType.class);
    }
}