package ro.eu.infoagenda.ui.swing;

import javax.swing.*;

public class InfoAgendaMain {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new InfoAgendaController().show());
    }

}
