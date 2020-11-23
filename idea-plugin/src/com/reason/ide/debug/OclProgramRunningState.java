package com.reason.ide.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.reason.ide.debug.conf.OclRunConfiguration;
import org.jetbrains.annotations.NotNull;

public class OclProgramRunningState extends CommandLineState {
  private final Module m_module;
  private final OclRunConfiguration m_configuration;

  public OclProgramRunningState(ExecutionEnvironment environment, Module module, OclRunConfiguration configuration) {
    super(environment);
    m_module = module;
    m_configuration = configuration;
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    GeneralCommandLine commandLine = getCommand();
    return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
  }

  @NotNull
  private GeneralCommandLine getCommand() {
    GeneralCommandLine commandLine = new GeneralCommandLine();
    // set exe/working dir/...
    String workDirectory = m_configuration.getWorkDirectory();
    commandLine.withWorkDirectory(StringUtil.isEmpty(workDirectory) ? m_module.getProject().getBasePath() : workDirectory);

    return commandLine;
  }
}