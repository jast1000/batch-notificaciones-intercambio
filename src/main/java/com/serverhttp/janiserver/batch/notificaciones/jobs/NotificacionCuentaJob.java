package com.serverhttp.janiserver.batch.notificaciones.jobs;

import com.serverhttp.janiserver.batch.notificaciones.emails.EnvioEmailNuevoUsuarioThread;
import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.model.Usuario;
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
public class NotificacionCuentaJob implements Job, Serializable {

    private final Logger LOGGER = LoggerFactory.getLogger(NotificacionCuentaJob.class);

    private IntercambioDAOImpl intercambioDAO;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        intercambioDAO = new IntercambioDAOImpl();
        Properties propertiesEmail = new Properties();
        List<Usuario> usuariosNuevos;
        try {
            intercambioDAO.init();
//            propertiesEmail.load(getClass().getResourceAsStream("/envio-email.properties"));
            propertiesEmail.load(new FileInputStream(new File("envio-email.properties")));
            usuariosNuevos = intercambioDAO.listarNuevosUsuarios();
            if (usuariosNuevos != null && !usuariosNuevos.isEmpty()) {
                for (Usuario u : usuariosNuevos) {
                    LOGGER.info("{}", u.toString());
                    EnvioEmailNuevoUsuarioThread envioCorreo = new EnvioEmailNuevoUsuarioThread();
                    envioCorreo.setImpl(intercambioDAO);
                    envioCorreo.setPropertiesEmail(propertiesEmail);
                    envioCorreo.setUsuario(u);
                    envioCorreo.run();
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
