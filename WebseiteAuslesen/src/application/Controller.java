package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import webseiteAuslesen.WebseiteAuslesen;
import webseiteAuslesen.Wort;

public class Controller {
	
	ObservableList<String> l = FXCollections.observableArrayList();
	
	@FXML
	private Button startBtn;
	@FXML
	private ToggleButton toggleArtikel;
	@FXML
	private TextField webseiteURL;
	@FXML
	private Label woerterGefundenLabel;
	@FXML
	private ListView<String> woerterListe;
	
	@FXML
	private NumberTextField top10WoerterAnzahl;
	@FXML
	private PieChart pieChart;
	private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	
	WebseiteAuslesen wa;
	
	@FXML
	protected void go(ActionEvent e) {
		
		if(e.getSource() == toggleArtikel) {
			if(toggleArtikel.isSelected())
				toggleArtikel.setText("Nein");
			else
				toggleArtikel.setText("Ja");
		}
		if(e.getSource() == startBtn) {
			String webURL;
			int topWoerterAnzahl;
			try {
				webURL = webseiteURL.getText();
				if(top10WoerterAnzahl.getText() != null && top10WoerterAnzahl.getText().length() != 0) {
					topWoerterAnzahl = Integer.parseInt(top10WoerterAnzahl.getText());
					if(topWoerterAnzahl > 200) 
						topWoerterAnzahl = 200;
					else if(topWoerterAnzahl <= 0) 
						topWoerterAnzahl = 10;
				}else
					topWoerterAnzahl = 10;
				top10WoerterAnzahl.setText(""+topWoerterAnzahl);
				if(webURL != null && webURL.length() != 0) {
					l.clear();
					pieChartData.clear();
					int platz = 1;
					wa = new WebseiteAuslesen();
					wa.webseiteAuslesen(webURL, toggleArtikel.isSelected(), topWoerterAnzahl);
					for(Wort w : wa.getTopWoerterListe(topWoerterAnzahl)) {
						l.add(platz+++". \t"+w.getWort()+" \t\t\t"+w.getHaeufigkeit()+"x");
						pieChartData.add(new PieChart.Data(w.getWort(), w.getHaeufigkeit()));
					}
					woerterListe.setItems(l);
					woerterGefundenLabel.setText("Wörter gefunden: "+wa.getAnzahlGefundeneWoerter());
					
					pieChart.setData(pieChartData);
				}else {
					openErrorDialog("Hinweis!", "Keine Webseite eingetragen");
				}
			}catch(Exception exc) {
				openErrorDialog("Fehler", exc.getMessage());
			}
		}
	}
	
	@FXML
	protected void enter(MouseEvent e) {
		if(e.getSource() == startBtn)
			Tooltip.install(startBtn, new Tooltip("Starte die Analyse"));
		else if(e.getSource() == webseiteURL)
			Tooltip.install(webseiteURL, new Tooltip("Trage die URL der Webseite ein"));
	}
	
	private void openErrorDialog(String title, String error) {
		//Stage init
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Frage - Label
        Label label = new Label("Fehler: \n"+error);
         
         
        // Antwort-Button JA
        Button okBtn = new Button("Verstanden");
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });

         
        // Layout fuer Button
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(okBtn);
         
                                 
        // Layout fuer Label und hBox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.getChildren().add(label);
        vbox.getChildren().add(hbox);
         
         
        // Stage befuellen
        Scene scene = new Scene(vbox);
        dialog.setResizable(false);
        dialog.setTitle(title);
        dialog.setScene(scene);
        dialog.show();
	}
	
}
