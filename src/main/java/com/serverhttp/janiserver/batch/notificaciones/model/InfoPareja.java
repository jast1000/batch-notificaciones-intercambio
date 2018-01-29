package com.serverhttp.janiserver.batch.notificaciones.model;

/**
 *
 * @author JAST
 */
public class InfoPareja {
    
    private String usuarioParticipante;
    private String idParticipante;
    
    private String nombres;
    private Integer edad;
    private String sexo;
    private String gustos;
    private String opcionesIntercambio;

    public InfoPareja() {
    }

    public String getUsuarioParticipante() {
        return usuarioParticipante;
    }

    public void setUsuarioParticipante(String usuarioParticipante) {
        this.usuarioParticipante = usuarioParticipante;
    }

    public String getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(String idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGustos() {
        return gustos;
    }

    public void setGustos(String gustos) {
        this.gustos = gustos;
    }

    public String getOpcionesIntercambio() {
        return opcionesIntercambio;
    }

    public void setOpcionesIntercambio(String opcionesIntercambio) {
        this.opcionesIntercambio = opcionesIntercambio;
    }
}
