package com.serverhttp.janiserver.batch.notificaciones.jobs;

import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.emails.EnvioEmailParejasIntercambioThread;
import com.serverhttp.janiserver.batch.notificaciones.model.PerfilIntercambioPareja;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JAST
 */
public class NotificacionParticipantesJob implements Job, Serializable {

    private final Logger LOGGER = LoggerFactory.getLogger(NotificacionParticipantesJob.class);

    private final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private IntercambioDAOImpl intercambioDAO;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Properties propertiesEmail = new Properties();
        Date fechaEvento;
        List<PerfilIntercambioPareja> parejas;
        intercambioDAO = new IntercambioDAOImpl();
        try {
            intercambioDAO.init();
//            propertiesEmail.load(getClass().getResourceAsStream("/envio-email.properties"));
            propertiesEmail.load(new FileInputStream(new File("envio-email.properties")));
            fechaEvento = intercambioDAO.consultarFechaIntercambio();
            if (fechaEvento != null) {
                DateTime intercambio = new DateTime(fechaEvento.getTime()).withMillisOfSecond(0);
                DateTime ahora = DateTime.now().withMillisOfSecond(0);
                if (intercambio.toDate().getTime() == ahora.toDate().getTime()) {
                    List<String> correos = intercambioDAO.listarCorreosAdministradores();
                    if (correos != null && !correos.isEmpty()) {
                        parejas = intercambioDAO.listarParticipantesConPareja();
                        if (parejas != null && !parejas.isEmpty()) {
                            EnvioEmailParejasIntercambioThread env = new EnvioEmailParejasIntercambioThread();
                            env.setImpl(intercambioDAO);
                            env.setCorreos(correos);
                            env.setParejas(parejas);
                            env.setPropertiesEmail(propertiesEmail);
                            env.run();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
