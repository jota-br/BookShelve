package ostrovski.joao.ui.helpers;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import ostrovski.joao.ui.ControllerEditDialog;

public class DialogResult {

    private final Dialog<ButtonType> dialog;
    private final ControllerEditDialog controller;

    public DialogResult(Dialog<ButtonType> dialog, ControllerEditDialog controller) {
        this.dialog = dialog;
        this.controller = controller;
    }

    public Dialog<ButtonType> getDialog() {
        return this.dialog;
    }

    public ControllerEditDialog getController() {
        return this.controller;
    }
}