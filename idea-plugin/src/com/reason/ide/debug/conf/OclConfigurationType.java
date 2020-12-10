package com.reason.ide.debug.conf;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.SimpleConfigurationType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyValue;
import icons.ORIcons;
import org.jetbrains.annotations.NotNull;

public class OclConfigurationType extends SimpleConfigurationType implements DumbAware {
    protected OclConfigurationType() {
        super("ocaml.build_tool", "Ocaml application", "Run a byte code ocaml application", NotNullLazyValue.createValue(() -> ORIcons.OCL_FILE_MODULE));
    }

    @Override
    public @NotNull String getTag() {
        return "ocaml";
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new OclRunConfiguration(project, this, "ocaml");
    }
}