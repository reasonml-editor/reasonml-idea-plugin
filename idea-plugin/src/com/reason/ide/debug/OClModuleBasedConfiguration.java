package com.reason.ide.debug;

import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class OClModuleBasedConfiguration extends RunConfigurationModule {
    public OClModuleBasedConfiguration(@NotNull Project project) {
        super(project);
    }
}
