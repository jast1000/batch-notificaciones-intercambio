package com.serverhttp.janiserver.batch.notificaciones.jobs;

import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.emails.EnvioEmailParejaThread;
import com.serverhttp.janiserver.batch.notificaciones.model.InfoPareja;
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
public class NotificacionParejaJob implements Job, Serializable {

    private final Logger LOGGER = LoggerFactory.getLogger(NotificacionParejaJob.class);

    private IntercambioDAOImpl intercambioDAO;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Properties propertiesEmail = new Properties();
        List<InfoPareja> parejas;

        intercambioDAO = new IntercambioDAOImpl();
        try {
            intercambioDAO.init();
//            propertiesEmail.load(getClass().getResourceAsStream("/envio-email.properties"));
            propertiesEmail.load(new FileInputStream(new File("envio-email.properties")));
            parejas = intercambioDAO.listarParejas();
            if (parejas != null && !parejas.isEmpty()) {
                for (InfoPareja ip : parejas) {
                    EnvioEmailParejaThread ept = new EnvioEmailParejaThread();
                    ept.setPropertiesEmail(propertiesEmail);
                    ept.setImpl(intercambioDAO);
                    ept.setPareja(ip);
                    ept.run();
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
