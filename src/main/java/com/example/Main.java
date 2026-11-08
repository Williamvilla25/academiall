package com.example;

import javax.swing.SwingUtilities;

import com.uniajc.vista.VistaLogin;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin vistaLogin = new VistaLogin();
            vistaLogin.mostrarVentana();
        });
    }
}
