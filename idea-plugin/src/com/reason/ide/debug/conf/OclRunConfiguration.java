package com.reason.ide.debug.conf;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.intellij.util.xmlb.*;
import com.reason.ide.debug.*;
import com.reason.lang.core.psi.*;
import com.reason.module.*;
import org.jdom.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static java.util.stream.Collectors.*;

public class OclRunConfiguration extends ModuleBasedConfiguration<OClModuleBasedConfiguration, PsiModule> {
  private String m_workDirectory;

  OclRunConfiguration(Project project, ConfigurationType configurationType, String name) {
    super(name, new OClModuleBasedConfiguration(project), configurationType.getConfigurationFactories()[0]);
  }

  @Nullable
  public String getWorkDirectory() {
    return m_workDirectory;
  }

  public void setWorkDirectory(@Nullable String workDirectory) {
    m_workDirectory = workDirectory;
  }

  @Override
  public Collection<Module> getValidModules() {
    Module[] modules = ModuleManager.getInstance(getProject()).getModules();
    return Arrays.stream(modules).filter(it -> OCamlModuleType.NAME.equals(it.getModuleTypeName())).collect(toList());
  }

  @Override
  public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new OclRunConfigurationEditorForm();
  }

  @Nullable
  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
    OClModuleBasedConfiguration configuration = getConfigurationModule();
    Module module = configuration.getModule();
    return new OclProgramRunningState(environment, module, this);
  }

  @Override
  public void writeExternal(@NotNull Element element) throws WriteExternalException {
    super.writeExternal(element);
    XmlSerializer.serializeInto(this, element);
  }

  @Override
  public void readExternal(@NotNull Element element) throws InvalidDataException {
    super.readExternal(element);
    XmlSerializer.deserializeInto(this, element);
  }
}