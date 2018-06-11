/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.YearAndCount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<YearAndCount> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<?> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private Button btnSequenza, btnAnalizza, btnAvvistamenti;
    
    @FXML
    void handleAnalizza(ActionEvent event) {

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	this.txtResult.clear();
    	YearAndCount year = this.boxAnno.getValue();
    	if(year == null) {
    		this.txtResult.setText("Selezionare un anno dal box.");
    		return;
    	}
    	this.model.creaGrafo(year.getAnno());
    	this.txtResult.appendText("Grafo ultimato: è possibile eseguire le operazioni successive.\n");
    	this.txtResult.appendText(String.format("Il grafo ha: %d nodi e %d archi.\n", model.getStati().size(), model.getEdges().size()));
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert btnAnalizza != null: "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert btnSequenza != null: "fx:id=\"btnSequenza\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert btnAvvistamenti!= null: "fx:id=\"btnAvvistamenti\" was not injected: check your FXML file 'Ufo.fxml'.";
    }
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().setAll(model.getAnniAvvistamenti());
    }
}
