package fr.uga.miage.m1.my_project.core.port.input;

import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;

import java.util.List;

public interface RencontreServicePort {

    /* =====================================================
       Méthodes Publiques (Interfaces du Service)
       ===================================================== */

    /**
     * Récupère toutes les rencontres en attente.
     *
     * @return Une liste de {@link RencontreResponse} représentant les rencontres en attente.
     */
    List<RencontreResponse> getRencontresEnAttente();

    /**
     * Initialise une nouvelle rencontre pour un client donné.
     *
     * @param clientId    L'ID du client initiateur.
     * @param nombreTours Le nombre de tours pour la rencontre.
     * @return true si la rencontre a été initiée avec succès, sinon false.
     */
    boolean initierRencontre(String clientId, int nombreTours);

    /**
     * Permet à un client de rejoindre une rencontre existante.
     *
     * @param clientId     L'ID du client qui rejoint la rencontre.
     * @param idRencontre  L'ID de la rencontre à rejoindre.
     * @throws InvalidActionRestException Si le client ne peut pas rejoindre la rencontre.
     */
    void rejoindreRencontre(String clientId, String idRencontre);

    /**
     * Enregistre le choix d'un joueur pour un tour donné.
     *
     * @param clientId  L'ID du client.
     * @param action    L'action choisie par le joueur.
     * @param strategie La stratégie choisie (en cas d'abandon).
     * @param groupId   Le groupe du joueur (pour adapter la stratégie).
     */
    void enregistrerChoix(String clientId, TYPE_ACTION action, TYPE_STRATEGIE strategie, String groupId);

    /**
     * Valide si un client peut rejoindre une rencontre.
     *
     * @param clientId    L'ID du client.
     * @param idRencontre L'ID de la rencontre.
     * @return La rencontre validée.
     * @throws InvalidActionRestException Si le client ne peut pas rejoindre la rencontre.
     */
    Rencontre validateRejoindreRencontre(String clientId, String idRencontre);

    /**
     * Vérifie si un tour est prêt à être traité.
     *
     * @param rencontre La rencontre à vérifier.
     * @return true si le tour est prêt, sinon false.
     */
    boolean estTourPret(Rencontre rencontre);

    /**
     * Gère la déconnexion d'un joueur.
     *
     * @param clientId L'ID du client déconnecté.
     */
    void handleDesconnectedPlayer(String clientId);

    /**
     * Récupère une rencontre en cours par l'ID du client.
     *
     * @param clientId L'ID du client.
     * @return La rencontre en cours.
     * @throws RencontreNotFoundRestException Si la rencontre n'est pas trouvée.
     */
    Rencontre getRencontreEnCoursByClientId(String clientId);

    /**
     * Récupère un joueur à partir d'une rencontre.
     *
     * @param rencontre La rencontre.
     * @param clientId  L'ID du client.
     * @return Le joueur correspondant.
     * @throws InvalidActionRestException Si le joueur ne fait pas partie de la rencontre.
     */
    Joueur getJoueurFromRencontre(Rencontre rencontre, String clientId);

    /**
     * Récupère le joueur opposé dans une rencontre.
     *
     * @param rencontre La rencontre.
     * @param joueur    Le joueur actuel.
     * @return Le joueur opposé.
     */
    Joueur getJoueurOppose(Rencontre rencontre, Joueur joueur);

    /**
     * Récupère l'historique des actions d'un joueur dans une rencontre.
     *
     * @param rencontre La rencontre.
     * @param joueur    Le joueur.
     * @return La liste des actions du joueur.
     */
    List<TYPE_ACTION> getHistoriqueJoueur(Rencontre rencontre, Joueur joueur);

    /**
     * Récupère le dernier résultat d'un joueur dans une rencontre.
     *
     * @param rencontre La rencontre.
     * @param joueur    Le joueur.
     * @return Le score du dernier tour du joueur.
     */
    int getDernierResultatJoueur(Rencontre rencontre, Joueur joueur);

    /**
     * Crée une entity robot à partir d'un joueur humain.
     *
     * @param joueur    Le joueur.
     * @return Un joueur robot.
     */
    Joueur remplacerJoueurParRobot(Joueur joueur);
}