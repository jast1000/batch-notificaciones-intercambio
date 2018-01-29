package com.serverhttp.janiserver.batch.notificaciones.jobs;

import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.emails.EnvioEmailInvitacionThread;
import com.serverhttp.janiserver.batch.notificaciones.model.SolicitudParticipante;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JAST
 */
public class NotificacionInvitacionJob implements Job, Serializable {

    private final Logger LOGGER = LoggerFactory.getLogger(NotificacionInvitacionJob.class);

    private IntercambioDAOImpl intercambioDAO;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Properties propertiesEmail = new Properties();
        List<SolicitudParticipante> invitaciones;

        intercambioDAO = new IntercambioDAOImpl();
        try {
            intercambioDAO.init();
//            propertiesEmail.load(getClass().getResourceAsStream("/envio-email.properties"));
            propertiesEmail.load(new FileInputStream(new File("envio-email.properties")));
            invitaciones = intercambioDAO.listarParticipantesPorInvitar();
            if (invitaciones != null && !invitaciones.isEmpty()) {
                for (SolicitudParticipante sp : invitaciones) {
                    EnvioEmailInvitacionThread inv = new EnvioEmailInvitacionThread();
                    inv.setImpl(intercambioDAO);
                    inv.setPropertiesEmail(propertiesEmail);
                    inv.setSolicitud(sp);
                    inv.run();
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
