package webseiteAuslesen;

/**
 * Wort dient zur Verwaltung von Woertern welche mit
 * einem String (Ihren "Namen") und ihrer Haeufigkeit
 * versehen werden.<br>
 * Ausserdem implementiert die Klasse das Interface
 * Comparable, damit eine Liste von Wort-Objekten
 * auch nach ihrer Haeufigkeit sortiert werden kann.
 * 
 * @author Tim Normen Holzapfel
 * @version 1.0
 * @see WebseiteAuslesen
 */
public class Wort implements Comparable<Wort>{
	
	// Deklarationen der benoetigten Variablen
	/** String (Wort) welcher gespeichert werden soll.*/
	private String wort;
	/** Haeufigkeit des gespeicherten Strings (Wortes).*/
    private int haeufigkeit;
    
    /**
     * Erzeugt eine neue Instanz der Klasse Wort.<br>
     * Initialisiert:<br>
     * &#160;&#160;wort mit worUebergebent<br>
     * &#160;&#160;haeufigkeit mit 1
     * @param wort String welcher gespeichert werden soll
     */
    public Wort(String wortUebergeben){
        wort = wortUebergeben;
        haeufigkeit = 1;
    }
    
    /**Inkrementiert die Variable <b>haeufigkeit</b> des Wortes.*/
    public void inkrementiereHaeufigkeit(){
        haeufigkeit++;
    }
    
    /**
     * Liefert die aktuelle Haeufigkeit des Wortes zurueck. 
     * @return aktuelle Haeufigkeit
     */
    public int getHaeufigkeit(){
        return haeufigkeit;
    }
    /**
     * Liefert den gespeicherten String zurueck.
     * @return String welcher gespeichert wurde
     */
    public String getWort(){
        return wort;
    }
    
    /**{@inheritDoc}
     * Ueberschriebene Methode des Interfaces - Comparable.
     * <br>Dient zum sortieren durch den int-Wert <b>haeufigkeit</b>.
     * @param wortObj Wort-Objekt womit verglichen werden soll
     */
    @Override
    public int compareTo(Wort wortObj){
        int x;
        if(this.getHaeufigkeit() < wortObj.getHaeufigkeit())
            x = -1;
        else if(this.getHaeufigkeit() > wortObj.getHaeufigkeit())
            x = 1;
        else 
            x = 0;
        return x;
    }
}
