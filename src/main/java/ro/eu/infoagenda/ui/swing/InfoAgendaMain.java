package ro.eu.infoagenda.ui.swing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class InfoAgendaMain {
    private static final Logger logger = LogManager.getLogger(ro.eu.infoagenda.ui.javafx.InfoAgendaMain.class);
    private static final String VERSION = "1.4";
    static {
        logger.info(String.format("Info agenda %s", VERSION));
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new InfoAgendaController().show());
    }

}
