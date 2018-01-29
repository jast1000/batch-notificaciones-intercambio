package com.serverhttp.janiserver.batch.notificaciones.dao;

import com.serverhttp.janiserver.batch.notificaciones.model.InfoPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PerfilIntercambioPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PlantillaCorreo;
import com.serverhttp.janiserver.batch.notificaciones.model.SolicitudParticipante;
import com.serverhttp.janiserver.batch.notificaciones.model.Usuario;
import java.util.Date;
import java.util.List;

/**
 *
 * @author JAST
 */
public interface IntercambioDAOInterface {

    public void init() throws Exception;

    public List<Usuario> listarNuevosUsuarios() throws Exception;

    public void registrarPerfilUsuario(Usuario usuario) throws Exception;

    public List<SolicitudParticipante> listarParticipantesPorInvitar() throws Exception;

    public void registrarInvitacion(SolicitudParticipante sp) throws Exception;

    public List<InfoPareja> listarParejas() throws Exception;

    public void registrarEnvioParejas(InfoPareja ip) throws Exception;

    public Date consultarFechaIntercambio() throws Exception;

    public List<PerfilIntercambioPareja> listarParticipantesConPareja() throws Exception;

    public List<String> listarCorreosAdministradores() throws Exception;
    
    public PlantillaCorreo obtenerPlantillaPorId(Integer id) throws Exception;
}
