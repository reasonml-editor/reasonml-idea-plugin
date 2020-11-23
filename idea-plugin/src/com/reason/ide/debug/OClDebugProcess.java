package com.reason.ide.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OClDebugProcess extends XDebugProcess {

    private static final Logger LOG = Logger.getInstance("ReasonML.debug");
    private OclDebuggerEditorsProvider m_editorsProvider;

//    private final OClApplicationRunningState m_runningState;

    public static OClDebugProcess create(@NotNull XDebugSession session, @NotNull ExecutionEnvironment environment) {
        return new OClDebugProcess(session, environment);
    }

    private OClDebugProcess(@NotNull XDebugSession session, @NotNull ExecutionEnvironment environment) {
        super(session);
        m_editorsProvider = new OclDebuggerEditorsProvider();
    }

    @NotNull
    @Override
    public XDebuggerEditorsProvider getEditorsProvider() {
        return m_editorsProvider;
    }

    @NotNull
    @Override
    public ExecutionConsole createConsole() {
        ConsoleView console = (ConsoleView) super.createConsole();

        getProcessHandler().addProcessListener(new ProcessListener() {
            @Override
            public void startNotified(@NotNull ProcessEvent event) {
                console.print("Connected to the OCaml debugger\n", ConsoleViewContentType.SYSTEM_OUTPUT);
            }

            @Override
            public void processTerminated(@NotNull ProcessEvent event) {
            }

            @Override
            public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) {
            }

            @Override
            public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                console.print(event.getText(), ConsoleViewContentType.getConsoleViewType(outputType));
            }
        });

        return console;
    }

    @Nullable
    @Override
    protected ProcessHandler doGetProcessHandler() {
        try {
            System.out.println("OClDebugProcess.doGetProcessHandler");
//            GeneralCommandLine commandLine = m_runningState.getCommand();
            GeneralCommandLine commandLine = new GeneralCommandLine();
            // set exe/working dir/...
            String workDirectory = "/home/dev/rincewind"; //m_configuration.getWorkDirectory();
            commandLine.withWorkDirectory(workDirectory);
            commandLine.setExePath("ocamldebug");
            commandLine.addParameters("_build/default/bin/rincewind.bc");

            LOG.debug("Running debugger process: " + commandLine.getCommandLineString());

            Process process = commandLine.createProcess();
            return new OSProcessHandler(process, commandLine.getCommandLineString());
        } catch (ExecutionException e) {
            LOG.debug("Failed to run debug target.", e);
            throw new RuntimeException(e);
        }
    }

}