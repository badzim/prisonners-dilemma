package fr.uga.miage.m1.my_project.component;
import fr.uga.miage.m1.my_project.model.Rencontre;
import fr.uga.miage.m1.my_project.model.Server;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import fr.uga.miage.m1.my_project.service.RencontreService;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Component
@RequiredArgsConstructor
public class RencontreComponent implements Runnable {

    @Autowired
    private final RencontreService rencontreService;

    private Rencontre rencontre;

    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(Server.class.getName());
        try {
            rencontreService.initialiserRencontre(rencontre);

            for (int i = 1; i <= rencontre.getNombreTours(); i++) {
                rencontreService.gererTour(rencontre, i);
            }
            rencontreService.terminerRencontre(rencontre);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
