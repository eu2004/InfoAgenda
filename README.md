# InfoAgenda

Run with java 13
pi@raspberrypi:~/apps/InfoAgenda $ java -jar InfoAgenda-1.0-SNAPSHOT-jar-with-dependencies.jar

Run with java 15
pi@raspberrypi:~/apps/InfoAgenda $ java --module-path="dependencies-jars" --add-modules=javafx.controls,javafx.fxml -jar InfoAgenda-1.0-SNAPSHOT-jar-with-dependencies.jar

Run with swing (javafx hits CPU at 100%):
java -cp InfoAgenda-1.0-SNAPSHOT-jar-with-dependencies.jar ro.eu.infoagenda.ui.swing.InfoAgendaMain


/usr/lib/jvm/java-11-openjdk-armhf/bin/java -cp ./apps/InfoAgenda/InfoAgenda-1.4-SNAPSHOT-jar-with-dependencies.jar ro.eu.infoagenda.ui.swing.InfoAgendaMain
