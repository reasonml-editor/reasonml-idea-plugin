package com.reason.ide.debug;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.facet.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.text.StringUtil;
import com.reason.*;
import com.reason.ide.debug.conf.*;
import com.reason.ide.facet.*;
import org.jetbrains.annotations.*;

public class OclProgramRunningState extends CommandLineState {
  private final Module m_module;
  private final OclRunConfiguration m_configuration;

  public OclProgramRunningState(ExecutionEnvironment environment, Module module, OclRunConfiguration configuration) {
    super(environment);
    m_module = module;
    m_configuration = configuration;
  }

  @Override
  protected @NotNull ProcessHandler startProcess() throws ExecutionException {
    DuneFacet duneFacet = FacetManager.getInstance(m_module).getFacetByType(DuneFacet.ID);
    Sdk odk = duneFacet == null ? null : duneFacet.getODK();

    OCamlExecutable exeEnv = OCamlExecutable.getExecutable(odk);

    String workDirectory = m_configuration.getWorkDirectory();
    String path = StringUtil.isEmpty(workDirectory) ? m_module.getProject().getBasePath() : workDirectory;

    GeneralCommandLine command = new GeneralCommandLine()
                                     .withExePath(path + "/main")
                                     .withWorkDirectory(path);

    GeneralCommandLine cli = exeEnv.patchCommandLine(command, null, false, m_module.getProject());

    return new KillableProcessHandler(cli);
  }
}