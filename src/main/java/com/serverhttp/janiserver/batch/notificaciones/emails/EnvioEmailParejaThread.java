package com.serverhttp.janiserver.batch.notificaciones.emails;

import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.model.InfoPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PlantillaCorreo;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JAST
 */
public class EnvioEmailParejaThread extends Thread {

    private final Logger LOGGER = LoggerFactory.getLogger(EnvioEmailParejaThread.class);
    private final Integer ID_PLANTILLA_PAREJA = 3;

    private IntercambioDAOImpl impl;

    private Properties propertiesEmail;
    private InfoPareja pareja;

    public EnvioEmailParejaThread() {
    }

    @Override
    public void run() {
        PlantillaCorreo pc;
        try {
            pc = impl.obtenerPlantillaPorId(ID_PLANTILLA_PAREJA);
            String asunto = pc.getAsunto();
            String mensajeTemplate = pc.getPlantilla();
            String mensaje = String.format(mensajeTemplate,
                    pareja.getNombres(),
                    pareja.getEdad(),
                    pareja.getSexo(),
                    pareja.getGustos(),
                    pareja.getOpcionesIntercambio());

            String correoRemitente = propertiesEmail.getProperty("email-remitente");
            String passwordRemitente = propertiesEmail.getProperty("password-remitente");
            String host = propertiesEmail.getProperty("mail.smtp.host");
            String puerto = propertiesEmail.getProperty("mail.smtp.port");
            String correoDestinatario = pareja.getUsuarioParticipante();

            Session session = Session.getDefaultInstance(propertiesEmail);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestinatario));
            message.setSubject(asunto, "UTF-8");
            message.setText(mensaje, "UTF-8");

            Transport t = session.getTransport("smtp");
            t.connect(host, Integer.parseInt(puerto), correoRemitente, passwordRemitente);

            t.sendMessage(message, message.getAllRecipients());
            LOGGER.info("Invitación de correo enviado!");

            impl.registrarEnvioParejas(pareja);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public Properties getPropertiesEmail() {
        return propertiesEmail;
    }

    public void setPropertiesEmail(Properties propertiesEmail) {
        this.propertiesEmail = propertiesEmail;
    }

    public IntercambioDAOImpl getImpl() {
        return impl;
    }

    public void setImpl(IntercambioDAOImpl impl) {
        this.impl = impl;
    }

    public InfoPareja getPareja() {
        return pareja;
    }

    public void setPareja(InfoPareja pareja) {
        this.pareja = pareja;
    }
}
