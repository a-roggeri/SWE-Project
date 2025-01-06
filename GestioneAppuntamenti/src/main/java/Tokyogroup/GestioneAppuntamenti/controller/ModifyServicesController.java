package Tokyogroup.GestioneAppuntamenti.controller;

import Tokyogroup.GestioneAppuntamenti.model.Service;
import Tokyogroup.GestioneAppuntamenti.model.ServiceDAO;
import Tokyogroup.GestioneAppuntamenti.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Controller per la gestione dei servizi di un barbiere.
 */
public class ModifyServicesController {

    private static final Logger logger = LogManager.getLogger(ModifyServicesController.class);

    private final ServiceDAO serviceDAO;
    private final User hairdresser;

    /**
     * Costruttore della classe ModifyServicesController.
     *
     * @param hairdresser Il barbiere per cui gestire i servizi.
     */
    public ModifyServicesController(User hairdresser) {
        this.hairdresser = hairdresser;
        this.serviceDAO = new ServiceDAO();
    }

    /**
     * Ottiene la lista dei servizi per il barbiere.
     *
     * @return Lista dei servizi del barbiere.
     */
    public List<Service> getServicesForHairdresser() {
        logger.info("Recupero dei servizi per il barbiere con ID: {}", hairdresser.getId());
        List<Service> services = serviceDAO.getServicesByHairdresser(hairdresser.getId());
        logger.debug("Servizi recuperati: {}", services);
        return services;
    }

    /**
     * Rimuove un servizio dal barbiere.
     *
     * @param serviceId ID del servizio da rimuovere.
     * @return true se il servizio è stato rimosso con successo, false altrimenti.
     */
    public boolean removeServiceFromHairdresser(int serviceId) {
        logger.info("Rimozione del servizio con ID {} dal barbiere con ID {}", serviceId, hairdresser.getId());
        boolean result = serviceDAO.removeServiceFromHairdresser(hairdresser.getId(), serviceId);
        logger.debug("Risultato della rimozione del servizio: {}", result);
        return result;
    }

    /**
     * Ottiene la lista dei servizi disponibili per il barbiere.
     *
     * @return Lista dei servizi disponibili per il barbiere.
     */
    public List<Service> getAvailableServicesForHairdresser() {
        logger.info("Recupero dei servizi disponibili per il barbiere con ID: {}", hairdresser.getId());
        List<Service> availableServices = serviceDAO.getAvailableServicesForHairdresser(hairdresser.getId());
        logger.debug("Servizi disponibili recuperati: {}", availableServices);
        return availableServices;
    }

    /**
     * Aggiunge un servizio al barbiere.
     *
     * @param serviceId ID del servizio da aggiungere.
     * @return true se il servizio è stato aggiunto con successo, false altrimenti.
     */
    public boolean addServiceToHairdresser(int serviceId) {
        logger.info("Aggiunta del servizio con ID {} al barbiere con ID {}", serviceId, hairdresser.getId());
        boolean result = serviceDAO.addServiceToHairdresser(hairdresser.getId(), serviceId);
        logger.debug("Risultato dell'aggiunta del servizio: {}", result);
        return result;
    }

    /**
     * Aggiunge un nuovo servizio.
     *
     * @param name  Nome del nuovo servizio.
     * @param price Prezzo del nuovo servizio.
     * @return ID del nuovo servizio aggiunto.
     */
    public int addNewService(String name, double price) {
        logger.info("Aggiunta di un nuovo servizio: {} con prezzo {}", name, price);
        int newServiceId = serviceDAO.addNewService(name, price);
        logger.debug("Nuovo servizio aggiunto con ID: {}", newServiceId);
        return newServiceId;
    }

    /**
     * Rimuove un servizio e cancella gli appuntamenti correlati.
     *
     * @param serviceId ID del servizio da rimuovere.
     * @return true se il servizio e gli appuntamenti correlati sono stati rimossi
     *         con successo, false altrimenti.
     */
    public boolean removeServiceAndCancelAppointments(int serviceId) {
        logger.info(
                "Rimozione del servizio con ID {} e cancellazione degli appuntamenti correlati per il barbiere con ID {}",
                serviceId, hairdresser.getId());
        boolean serviceRemoved = serviceDAO.removeServiceFromHairdresser(hairdresser.getId(), serviceId);
        logger.debug("Risultato della rimozione del servizio: {}", serviceRemoved);
        if (serviceRemoved) {
            boolean appointmentsCanceled = serviceDAO.cancelAppointmentsWithService(serviceId, hairdresser.getId());
            logger.debug("Risultato della cancellazione degli appuntamenti: {}", appointmentsCanceled);
            if (appointmentsCanceled) {
                logger.info("Appuntamenti con il servizio ID {} sono stati cancellati.", serviceId);
            }
            return true;
        }
        return false;
    }
}