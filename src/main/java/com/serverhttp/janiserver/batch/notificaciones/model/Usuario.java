package com.serverhttp.janiserver.batch.notificaciones.model;

/**
 *
 * @author JAST
 */
public class Usuario {

    private String usuario;
    private String password;
    private Integer tipoUsuario;

    public Usuario() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuario=" + usuario + ", password=" + password + ", tipoUsuario=" + tipoUsuario + '}';
    }
}
