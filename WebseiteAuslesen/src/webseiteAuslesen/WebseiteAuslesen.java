/*
 * Copyright (c) 2018, Tim Normen Holzapfel. All rights reserved.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package webseiteAuslesen;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * WebseiteAuslesen beinhaltet verschiedene Methoden um
 * den Text einer Webseite zu analysieren.<br>
 * <p>Hierfuer werden Objekte der Klasse {@link Wort} benutzt.</p>
 * 
 * @author Tim Normen Holzapfel
 * @version 1.0
 * @see Wort
 * @see Jsoup
 */
public class WebseiteAuslesen {
	
	// Deklarationen der benoetigten Variablen.
	/** Enthaelt eine Liste von Wort-Objekten (Jedes Wort nur einmal).*/
    private List<Wort> woerter;
    /** Enthaelt alle Woerter (Strings) die gefiltert werden sollen.*/
    private String[] filterWoerter;
    
    
    
    /**
     * Erzeugt ein neues Objekt der Klasse WebseiteAuslesen<br>
     * <p>Initialisiert {@link #woerter} mit einer leeren <i>ArrayList</i> und {@link #filterWoerter} mit einem
     * neuen <i>String-Array</i>, welches alle Woerter enthaelt die gefiltert werden sollen.</p>
     * 
     */
    public WebseiteAuslesen() {
    	// Initilisierung
    	woerter = new ArrayList<Wort>();
		filterWoerter = new String[] { "der", "die", "das", "mit", "dem", "den", "und", "bei", "ab", "aus", "von", "an",
				"auf", "bis", "durch", "fuer", "am", "beim", "fuers", "zur", "in", "des", "ist", "im", "als", "auch",
				"sich", "zu", "sind", "etwa" };
    }
    
    /**
     * Bereinigt den eingegebenen String und gibt dieses zurueck.<br>
     * <p>Zuerst werden alle {@link Character <i>characters</i>} des Strings in Kleinbuchstaben formatiert.<br>
     * Anschliessend werden je nach booleschen <b>isDocument</b> verschiedene Zeichen<br>
     * rausgefiltert. Zum Schluss werden noch die Umlaute, sowie Zeilenumbrueche<br>
     * ersetzt.</p>
     * @param string Die Zeichenkette, die gefiltert werden soll
     * @param isDocument true, wenn es sich um ein gesamtes Document handelt<br><div style="text-indent:71px">false, wenn es nur ein Wort/Satz ist</div>
     * @return den gesaeuberten String
     */
    public String clearString(String string, boolean isDocument) {
        string = string.toLowerCase(Locale.ROOT);
        if (string.length() != 0) {
            String stringNeu = " ";
            for (char c : string.toCharArray()) {
                if ((((int) c >= 97 && (int) c <= 122) || (int) c == 228 || (int) c == 252 || (int) c == 246 || (int) c == 223) | (isDocument && ( (int)c == 10 || (int)c == 32 ))) {
                	if(isDocument) {
                		if(! (  ( (int)stringNeu.charAt(stringNeu.length()-1) == 10 || (int)stringNeu.charAt(stringNeu.length()-1) == 32) && ( (int) c == 10 || (int)c == 32 ) ))
                			stringNeu = stringNeu+c;
                			
                	}else
                		stringNeu = stringNeu + c;
                }
            }
            stringNeu = stringNeu.replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll("ü", "ue").replaceAll("ß", "ss").replaceAll("\n", " ");
            string = stringNeu;
        }
        return string.substring(1);
    }
    
    /**
     * Liest eine Webseite aus und fuellt {@link #topWoerter}, sowie {@link #woerter}.<br>
     * 
     * <p>Mittels der {@link Jsoup}-Libary und eines {@link Document}-Objekts wird die Webseite aufgerufen.<br>
     * Anschliessend wird die Methode {@link WebseiteAuslesen#clearString(String, boolean) clearString()} aufgerufen und bekommt den gesamten Text<br>
     * der im <i>body</i> steht uebergeben. Der von der Methode zurueckgegebene String wird danach mit<br>
     * der {@link String#split(String) split()}-Methode gespalten und in dem temporaeren String[] {@link #stringAlleWoerter} gespeichert.</p>
     * 
     * 
     * @param webseite String der die auszulesende Webseite enthalten soll
     * @param woerterFiltern true wenn alle <i>{@link #filterWoerter}</i> rausgefiltert werden sollen
     * @param topAnzahl <i>int</i> - wie viele TopWoerter es geben soll
     * @throws IOException wenn keine Verbindung zur Webseite hergestellt werden kann
     * 
     * @see Wort
     * @see Jsoup
     */
    public void webseiteAuslesen(String webseite, boolean woerterFiltern, int topAnzahl) throws IOException {
        Document document = new Document("");
        document = Jsoup.connect(webseite).get();
        
        /**Temporaere Variable die alle Woerter als Strings zwischenspeichert.*/
        String[] stringAlleWoerter = clearString(document.wholeText(), true).split(" ");
        
        /*
         * for-each - Schleife die jedes Element des String[] stringAlleWoerter durchgeht.
         *            Hier wird geprueft, ob das Wort bereits vorhanden ist und dessen Haufigkeit
         *            inkrementiert wird oder das Wort in die Liste -woerter- mit 
         *            aufgenommen werden muss.
         */
        for (String x : stringAlleWoerter) {
                boolean vorhanden = false;
                for (Wort w : woerter) {
                    if (x.equals(w.getWort())) {
                        vorhanden = true;
                        w.inkrementiereHaeufigkeit();
                        break;
                    }
                }
                if (!vorhanden) {
                	Wort w = new Wort(x);
                	if(woerterFiltern) {
                	for(String artikelS : filterWoerter) {
                		if(w.getWort().equals(artikelS)) {
                			vorhanden = true;
                			break;
                		}
                	}}
                    if(!vorhanden)woerter.add(w);
                }
        }
        
        //Sortiert die woerter-Liste nach ihrer jeweiligen Haeufigkeit
        Collections.sort(woerter);
    }

    /**
     * Liefert einen umgekehrten int-{@code Array} des uebergebenen zurueck.
     * @param intArray int[] welcher umgekehrt werden soll
     * @return einen umgekehrten int[]
     */
    public int[] reverseIntArray(int[] intArray) {
        int m = 0;
        for (int x = 0; x < intArray.length / 2; x++) {
            m = intArray[x];
            intArray[x] = intArray[intArray.length - x - 1];
            intArray[intArray.length - x - 1] = m;
        }
        return intArray;
    }

    /**
     * Sucht das gesuchte Wort in der eingegebnen Liste und gibt es als Objekt der<br>
     * Klasse Wort zurueck oder null, wenn das Wort nicht gefunden wurde.
     * @param woerter die Liste in der gesucht werden soll
     * @param wortString Wort welches gesucht werden soll als String
     * @return das zu suchende Wort, oder null
     */
    public Wort findeWort(List<Wort> woerter, String wortString) {
        for (Wort w : woerter) 
            if (w.getWort().equals(wortString)) 
                return w;
        return null;
    }

    /**
     * Gibt die Prozente der beiden uebergebenen Werte als String zurueck.
     * @param grundwert Grundwert (100 Prozent)
     * @param prozentZahl Prozentzahl
     * @return Prozentzahl als String
     */
    public String getProzent(double grundwert, double prozentZahl) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumIntegerDigits(2);
        return nf.format((grundwert / prozentZahl) * 100);
    }
    
    /**
     * Gibt eine Liste aller TopWoerter zurueck.
     * 
     * @param anzahlTopWoerter Anzahl, wie viele TopWoerter zurueckgegeben werden sollen
     * @return Liste der TopWoerter
     */
    public List<Wort> getTopWoerterListe(int anzahlTopWoerter){
    	List<Wort> topWoerterList = new ArrayList<Wort>();
    	for(int i = woerter.size()-1; i >= woerter.size()-anzahlTopWoerter; i--)
    		topWoerterList.add(woerter.get(i));
    	return topWoerterList;
    }
    
    /**
     * Gibt die komplette {@link #woerter} Liste zurueck.
     * @return {@link #woerter}
     */
    public List<Wort> getWoerterListeKomplett(){
    	return woerter;
    }
    
    /**
     * Liefert die Anzahl aller gefundenen Woerter zurueck.
     * 
     * @return die Anzahl aller gefundenen Woerter
     */
    public int getAnzahlGefundeneWoerter() {
    	int z=0;
        for(int x = 0; x < woerter.size(); x++) {
        	z += woerter.get(x).getHaeufigkeit();
        }
    	return z;
    }
}
