package Tokyogroup.GestioneAppuntamenti.model;

/**
 * Classe che rappresenta un servizio offerto dal barbiere.
 */
public class Service {

    private int id;
    private String name;
    private double price;

    /**
     * Costruttore completo.
     *
     * @param id    l'identificativo del servizio
     * @param name  il nome del servizio
     * @param price il prezzo del servizio
     */
    public Service(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Costruttore vuoto.
     */
    public Service() {
    }

    /**
     * Restituisce l'identificativo del servizio.
     *
     * @return l'identificativo del servizio
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo del servizio.
     *
     * @param id l'identificativo del servizio
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il nome del servizio.
     *
     * @return il nome del servizio
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome del servizio.
     *
     * @param name il nome del servizio
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Restituisce il prezzo del servizio.
     *
     * @return il prezzo del servizio
     */
    public double getPrice() {
        return price;
    }

    /**
     * Imposta il prezzo del servizio.
     *
     * @param price il prezzo del servizio
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del servizio.
     *
     * @return una stringa che rappresenta il servizio
     */
    @Override
    public String toString() {
        String result = name + " " + price + "€";
        return result;
    }
}