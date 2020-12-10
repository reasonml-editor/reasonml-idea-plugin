package com.reason.module;

import com.intellij.openapi.module.ModuleType;
import icons.ORIcons;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

/** Type of OCaml native project. */
public class OCamlModuleType extends ModuleType<OCamlModuleBuilder> {
  public static final String NAME = "OCaml module";
  private static final OCamlModuleType INSTANCE = new OCamlModuleType();

  private OCamlModuleType() {
    super("ocamlModuleType");
  }

  @NotNull
  public static OCamlModuleType getInstance() {
    return INSTANCE;
  }

  @NotNull
  @Override
  public OCamlModuleBuilder createModuleBuilder() {
    return new OCamlModuleBuilder();
  }

  @NotNull
  @Override
  public String getName() {
    //noinspection DialogTitleCapitalization
    return NAME;
  }

  @NotNull
  @Override
  public String getDescription() {
    return "OCaml modules are used for native development";
  }

  @NotNull
  @Override
  public Icon getNodeIcon(boolean isOpened) {
    return ORIcons.OCL_MODULE;
  }
}
