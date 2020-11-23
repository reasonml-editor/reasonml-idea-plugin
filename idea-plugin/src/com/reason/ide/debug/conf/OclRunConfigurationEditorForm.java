package com.reason.ide.debug.conf;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OclRunConfigurationEditorForm extends SettingsEditor<OclRunConfiguration> {
    private JPanel c_mainPanel;
    private ModulesComboBox c_modulesComboBox;
    private TextFieldWithBrowseButton c_workingDir;

    OclRunConfigurationEditorForm() {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        c_workingDir.addBrowseFolderListener("Choose Working Directory", null, null, descriptor);
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return c_mainPanel;
    }

    @Override
    protected void resetEditorFrom(@NotNull OclRunConfiguration configuration) {
        c_modulesComboBox.fillModules(configuration.getProject(), JavaModuleType.getModuleType()/*todo*/);
        c_modulesComboBox.setSelectedModule(configuration.getConfigurationModule().getModule());
        c_workingDir.setText(configuration.getWorkDirectory());
    }

    @Override
    protected void applyEditorTo(@NotNull OclRunConfiguration configuration) {
        configuration.setWorkDirectory(c_workingDir.getText());
    }
}