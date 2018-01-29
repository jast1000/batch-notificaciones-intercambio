package com.serverhttp.janiserver.batch.notificaciones.dao;

import com.serverhttp.janiserver.batch.notificaciones.hibernate.HibernateUtil;
import com.serverhttp.janiserver.batch.notificaciones.model.InfoPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PerfilIntercambioPareja;
import com.serverhttp.janiserver.batch.notificaciones.model.PlantillaCorreo;
import com.serverhttp.janiserver.batch.notificaciones.model.SolicitudParticipante;
import com.serverhttp.janiserver.batch.notificaciones.model.Usuario;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

/**
 *
 * @author JAST
 */
public class IntercambioDAOImpl implements IntercambioDAOInterface {

    private SessionFactory sf;

    @Override
    public void init() throws Exception {
        this.sf = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<Usuario> listarNuevosUsuarios() throws Exception {
        List<Usuario> usuarios;
        String sql = "select u.\"IdUsuario\" as usuario, u.\"Password\" as password, u.\"TipoUsuario\" as \"tipoUsuario\""
                + "        from \"Usuarios\" as u left join \"Participantes\" as p on(p.\"IdUsuario\" = u.\"IdUsuario\")"
                + "        where p.\"IdPart\" is null";
        Session s = sf.openSession();
        s.beginTransaction();
        usuarios = (List<Usuario>) s.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Usuario.class))
                .list();
        s.getTransaction().commit();
        s.close();
        return usuarios;
    }

    @Override
    public void registrarPerfilUsuario(Usuario usuario) throws Exception {
        String sql = "insert into \"Participantes\"(\"IdPart\", \"IdUsuario\") values(?, ?)";
        Session s = sf.openSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setString(0, UUID.randomUUID().toString())
                .setString(1, usuario.getUsuario())
                .executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public List<SolicitudParticipante> listarParticipantesPorInvitar() throws Exception {
        String sql = "select p.\"IdPart\" as \"idParticipante\", p.\"IdUsuario\" as usuario, r.\"IdRegla\" as \"idIntercambio\", "
                + " r.\"Lugar\" as lugar, r.\"Fecha\" as fecha, r.\"Monto\" as monto, a.\"IdPart\" as \"idAsistenciaParticipante\" "
                + " from \"Participantes\" as p left join \"Reglas\" as r on 1 = 1 left join \"Asistencia\" as a on(a.\"IdPart\" = p.\"IdPart\") "
                + " where r.\"IdRegla\" is not null and a.\"IdPart\" is null";
        List<SolicitudParticipante> solicitudes;
        Session s = sf.openSession();
        s.beginTransaction();
        solicitudes = (List<SolicitudParticipante>) s.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(SolicitudParticipante.class))
                .list();
        s.getTransaction().commit();
        s.close();
        return solicitudes;
    }

    @Override
    public void registrarInvitacion(SolicitudParticipante sp) throws Exception {
        String sql = "insert into \"Asistencia\"(\"IdPart\", \"Afirmacion\") values(?, null)";
        Session s = sf.openSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setString(0, sp.getIdParticipante())
                .executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public List<InfoPareja> listarParejas() throws Exception {
        List<InfoPareja> parejas;
        String sql = "select pr.\"IdUsuario\" as \"usuarioParticipante\", so.\"IdPart1\" as \"idParticipante\", pd.\"Nombre\" as nombres, pd.\"Edad\" as edad, pd.\"Sexo\" as sexo,"
                + " pd.\"Gustos\" as gustos, pd.\"OpcionesIntercambio\" as \"opcionesIntercambio\" from \"Sorteo\" as so"
                + " inner join \"Participantes\" as pr on so.\"IdPart1\" = pr.\"IdPart\""
                + " inner join \"Participantes\" as pd on so.\"IdPartInter\" = pd.\"IdPart\""
                + " where so.\"Fecha\" is null";
        Session s = sf.openSession();
        s.beginTransaction();
        parejas = (List<InfoPareja>) s.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(InfoPareja.class))
                .list();
        s.getTransaction().commit();
        s.close();
        return parejas;
    }

    @Override
    public void registrarEnvioParejas(InfoPareja ip) throws Exception {
        String sql = "update \"Sorteo\" set \"Fecha\" = ? where \"IdPart1\" = ?";
        Session s = sf.openSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setTimestamp(0, new Date())
                .setString(1, ip.getIdParticipante())
                .executeUpdate();
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public Date consultarFechaIntercambio() throws Exception {
        String sql = "select top 1 \"Fecha\" from \"Reglas\"";
        Date fechaEvento;
        Session s = sf.openSession();
        s.beginTransaction();
        fechaEvento = (Date) s.createSQLQuery(sql)
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return fechaEvento;
    }

    @Override
    public List<PerfilIntercambioPareja> listarParticipantesConPareja() throws Exception {
        String sql = "select pr.\"Nombre\" as \"nombresParticipante1\", pd.\"Nombre\" as \"nombresParticipante2\" from \"Sorteo\" as so"
                + " inner join \"Participantes\" as pr on so.\"IdPart1\" = pr.\"IdPart\""
                + " inner join \"Participantes\" as pd on so.\"IdPartInter\" = pd.\"IdPart\"";
        List<PerfilIntercambioPareja> parejas;
        Session s = sf.openSession();
        s.beginTransaction();
        parejas = (List<PerfilIntercambioPareja>) s.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PerfilIntercambioPareja.class))
                .list();
        s.getTransaction().commit();
        s.close();
        return parejas;
    }

    @Override
    public List<String> listarCorreosAdministradores() throws Exception {
        List<String> correos;
        String sql = "select us.\"IdUsuario\" from \"Usuarios\" as us"
                + " inner join \"Participantes\" as p on(p.\"IdUsuario\" = us.\"IdUsuario\")"
                + " where us.\"TipoUsuario\" = 1 and p.\"Nombre\" is not null";
        Session s = sf.openSession();
        s.beginTransaction();
        correos = (List<String>) s.createSQLQuery(sql)
                .list();
        s.getTransaction().commit();
        s.close();
        return correos;
    }

    @Override
    public PlantillaCorreo obtenerPlantillaPorId(Integer id) throws Exception {
        String sql = "select id, plantilla, asunto from plantilla_correo where id = ?";
        PlantillaCorreo plantilla;
        Session s = sf.openSession();
        s.beginTransaction();
        plantilla = (PlantillaCorreo) s.createSQLQuery(sql)
                .setInteger(0, id)
                .setResultTransformer(Transformers.aliasToBean(PlantillaCorreo.class))
                .uniqueResult();
        s.getTransaction().commit();
        s.close();
        return plantilla;
    }
}
