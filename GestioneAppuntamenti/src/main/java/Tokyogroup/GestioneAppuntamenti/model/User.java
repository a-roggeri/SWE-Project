package Tokyogroup.GestioneAppuntamenti.model;

/**
 * Classe che rappresenta un utente del sistema.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String accountType;
    private boolean isActive;

    /**
     * Costruttore completo.
     *
     * @param id          l'ID dell'utente
     * @param username    il nome utente
     * @param password    la password
     * @param accountType il tipo di account (GESTORE o CLIENTE)
     * @param isActive    lo stato attivo dell'utente
     * @throws IllegalArgumentException se username o password sono nulli o vuoti, o
     *                                  se accountType non è valido
     */
    public User(int id, String username, String password, String accountType, boolean isActive) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome utente non può essere nullo o vuoto.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere nulla o vuota.");
        }
        if (!"GESTORE".equals(accountType) && !"CLIENTE".equals(accountType)) {
            throw new IllegalArgumentException("Il tipo di account deve essere 'GESTORE' o 'CLIENTE'.");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.isActive = isActive;
    }

    /**
     * Costruttore senza ID e isActive (per compatibilità con il vecchio codice).
     *
     * @param username    il nome utente
     * @param password    la password
     * @param accountType il tipo di account (GESTORE o CLIENTE)
     * @throws IllegalArgumentException se username o password sono nulli o vuoti, o
     *                                  se accountType non è valido
     */
    public User(String username, String password, String accountType) {
        this(0, username, password, accountType, true);
    }

    /**
     * Costruttore vuoto.
     */
    public User() {
        // Costruttore vuoto, nessuna validazione necessaria
    }

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return l'ID dell'utente
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param id l'ID dell'utente
     * @throws IllegalArgumentException se l'ID è negativo
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("L'ID non può essere negativo.");
        }
        this.id = id;
    }

    /**
     * Restituisce il nome utente.
     *
     * @return il nome utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta il nome utente.
     *
     * @param username il nome utente
     * @throws IllegalArgumentException se il nome utente è nullo o vuoto
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome utente non può essere nullo o vuoto.");
        }
        this.username = username;
    }

    /**
     * Restituisce la password.
     *
     * @return la password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password.
     *
     * @param password la password
     * @throws IllegalArgumentException se la password è nulla o vuota
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere nulla o vuota.");
        }
        this.password = password;
    }

    /**
     * Restituisce il tipo di account.
     *
     * @return il tipo di account
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Imposta il tipo di account.
     *
     * @param accountType il tipo di account (GESTORE o CLIENTE)
     * @throws IllegalArgumentException se il tipo di account non è valido
     */
    public void setAccountType(String accountType) {
        if (!"GESTORE".equals(accountType) && !"CLIENTE".equals(accountType)) {
            throw new IllegalArgumentException("Il tipo di account deve essere 'GESTORE' o 'CLIENTE'.");
        }
        this.accountType = accountType;
    }

    /**
     * Restituisce lo stato attivo dell'utente.
     *
     * @return true se l'utente è attivo, false altrimenti
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Imposta lo stato attivo dell'utente.
     *
     * @param isActive lo stato attivo dell'utente
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'utente.
     *
     * @return il nome utente
     */
    @Override
    public String toString() {
        return username;
    }
}