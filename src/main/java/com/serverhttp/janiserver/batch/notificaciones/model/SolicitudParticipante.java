package com.serverhttp.janiserver.batch.notificaciones.model;

import java.util.Date;

/**
 *
 * @author JAST
 */
public class SolicitudParticipante {

    private String idParticipante;
    private String usuario;
    private Integer idIntercambio;
    private String idAsistenciaParticipante;
    private String lugar;
    private Double monto;
    private Date fecha;

    public SolicitudParticipante() {
    }

    public String getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(String idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getIdIntercambio() {
        return idIntercambio;
    }

    public void setIdIntercambio(Integer idIntercambio) {
        this.idIntercambio = idIntercambio;
    }

    public String getIdAsistenciaParticipante() {
        return idAsistenciaParticipante;
    }

    public void setIdAsistenciaParticipante(String idAsistenciaParticipante) {
        this.idAsistenciaParticipante = idAsistenciaParticipante;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "SolicitudParticipante{" + "idParticipante=" + idParticipante + ", usuario=" + usuario + ", idIntercambio=" + idIntercambio + ", idAsistenciaParticipante=" + idAsistenciaParticipante + '}';
    }
}
