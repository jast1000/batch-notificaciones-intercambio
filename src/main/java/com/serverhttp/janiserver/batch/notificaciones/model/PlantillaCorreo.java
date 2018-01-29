package com.serverhttp.janiserver.batch.notificaciones.model;

/**
 *
 * @author JAST
 */
public class PlantillaCorreo {

    private Integer id;
    private String asunto;
    private String plantilla;

    public PlantillaCorreo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(String plantilla) {
        this.plantilla = plantilla;
    }
}
