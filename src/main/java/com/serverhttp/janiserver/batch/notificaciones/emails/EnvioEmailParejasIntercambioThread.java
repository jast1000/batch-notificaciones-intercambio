package com.serverhttp.janiserver.batch.notificaciones.emails;

import com.serverhttp.janiserver.batch.notificaciones.dao.IntercambioDAOImpl;
import com.serverhttp.janiserver.batch.notificaciones.model.PerfilIntercambioPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PlantillaCorreo;
import java.util.ArrayList;
import java.util.List;
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
public class EnvioEmailParejasIntercambioThread extends Thread {

    private final Logger LOGGER = LoggerFactory.getLogger(EnvioEmailParejasIntercambioThread.class);

    private final Integer ID_PLANTILLA_INTERCAMBIO = 4;
    private final Integer ID_PLANTILLA_LISTA_INTERCAMBIO = 5;

    private IntercambioDAOImpl impl;
    private List<PerfilIntercambioPareja> parejas;
    private List<String> correos;
    private Properties propertiesEmail;

    @Override
    public void run() {
        PlantillaCorreo pcIntercambio, pcParejas;
        List<InternetAddress> destinatarios = new ArrayList<>();
        try {
            pcIntercambio = impl.obtenerPlantillaPorId(ID_PLANTILLA_INTERCAMBIO);
            pcParejas = impl.obtenerPlantillaPorId(ID_PLANTILLA_LISTA_INTERCAMBIO);
            for (String c : correos) {
                destinatarios.add(new InternetAddress(c));
            }
            String asunto = pcIntercambio.getAsunto();
            String plantillaMensaje = pcIntercambio.getPlantilla();
            String listaParejas = "";

            for (PerfilIntercambioPareja p : parejas) {
                listaParejas += String.format(pcParejas.getPlantilla(),
                        p.getNombresParticipante1(),
                        p.getNombresParticipante2());
            }

            String mensaje = String.format(plantillaMensaje, listaParejas);

            String correoRemitente = propertiesEmail.getProperty("email-remitente");
            String passwordRemitente = propertiesEmail.getProperty("password-remitente");
            String host = propertiesEmail.getProperty("mail.smtp.host");
            String puerto = propertiesEmail.getProperty("mail.smtp.port");

            Session session = Session.getDefaultInstance(propertiesEmail);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente));
            message.addRecipients(Message.RecipientType.TO,
                    destinatarios.toArray(new InternetAddress[destinatarios.size()]));
            message.setSubject(asunto, "UTF-8");
            message.setText(mensaje, "UTF-8");

            Transport t = session.getTransport("smtp");
            t.connect(host, Integer.parseInt(puerto), correoRemitente, passwordRemitente);

            t.sendMessage(message, message.getAllRecipients());
            LOGGER.info("Invitación de correo enviado!");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public IntercambioDAOImpl getImpl() {
        return impl;
    }

    public void setImpl(IntercambioDAOImpl impl) {
        this.impl = impl;
    }

    public Properties getPropertiesEmail() {
        return propertiesEmail;
    }

    public void setPropertiesEmail(Properties propertiesEmail) {
        this.propertiesEmail = propertiesEmail;
    }

    public List<String> getCorreos() {
        return correos;
    }

    public void setCorreos(List<String> correos) {
        this.correos = correos;
    }

    public List<PerfilIntercambioPareja> getParejas() {
        return parejas;
    }

    public void setParejas(List<PerfilIntercambioPareja> parejas) {
        this.parejas = parejas;
    }
}
