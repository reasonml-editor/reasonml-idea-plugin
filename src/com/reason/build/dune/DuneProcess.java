package com.reason.build.dune;

import java.util.concurrent.atomic.*;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.Platform;
import com.reason.build.CompilerProcessLifecycle;
import com.reason.build.bs.ModuleConfiguration;
import com.reason.ide.ORNotification;
import com.reason.ide.sdk.OCamlSDK;

import static com.intellij.notification.NotificationListener.URL_OPENING_LISTENER;
import static com.intellij.notification.NotificationType.ERROR;

public final class DuneProcess implements ProjectComponent, CompilerProcessLifecycle {

    private final Project m_project;
    private final ProcessListener m_outputListener;
    private final AtomicBoolean m_started = new AtomicBoolean(false);
    private final AtomicBoolean m_restartNeeded = new AtomicBoolean(false);
    private KillableColoredProcessHandler m_processHandler;

    public static DuneProcess getInstance(Project project) {
        return project.getComponent(DuneProcess.class);
    }

    DuneProcess(Project project) {
        m_project = project;
        m_outputListener = new DuneOutputListener(m_project, this);
        recreate();
    }

    // Wait for the tool window to be ready before starting the process
    void startNotify() {
        if (m_processHandler != null && !m_processHandler.isStartNotified()) {
            try {
                m_processHandler.startNotify();
            } catch (Throwable e) {
                // already done ?
            }
        }
    }

    @Nullable
    ProcessHandler recreate() {
        try {
            killIt();
            GeneralCommandLine cli = getGeneralCommandLine();
            if (cli != null) {
                //cli.setRedirectErrorStream(true);
                m_processHandler = new KillableColoredProcessHandler(cli);
                if (m_outputListener != null) {
                    m_processHandler.addProcessListener(m_outputListener);
                }
            }
            return m_processHandler;
        } catch (ExecutionException e) {
            Notifications.Bus.notify(new ORNotification("Dune", "Can't run sdk\n" + e.getMessage(), ERROR));
        }

        return null;
    }

    private void killIt() {
        if (m_processHandler != null) {
            m_processHandler.killProcess();
            m_processHandler = null;
        }
    }

    @Nullable
    private GeneralCommandLine getGeneralCommandLine() {
        Sdk sdk = OCamlSDK.getSDK(m_project);
        if (sdk == null) {
            Notifications.Bus.notify(new ORNotification("Dune",
                    "<html>Can't find sdk.\n"
                            + "When using a dune config file, you need to create an OCaml SDK and associate it to the project.\n"
                            + "see <a href=\"https://github.com/reasonml-editor/reasonml-idea-plugin#ocaml\">github</a>.</html>",
                    ERROR, URL_OPENING_LISTENER));
            return null;
        }

        GeneralCommandLine cli = new GeneralCommandLine(sdk.getHomePath() + "/bin/jbuilder", "build", "rincewind.exe");
        //cli.withEnvironment("PATH", sdk.getHomePath() + "/bin" + ";" + sdk.getHomePath() + "/lib");
        VirtualFile baseRoot = Platform.findBaseRoot(m_project);
        cli.withWorkDirectory(ModuleConfiguration.getWorkingDir(m_project, baseRoot));

        return cli;
    }

    public boolean start() {
        boolean success = m_started.compareAndSet(false, true);
        if (!success) {
            m_restartNeeded.compareAndSet(false, true);
        }
        return success;
    }

    public void terminated() {
        m_started.set(false);
    }

}
