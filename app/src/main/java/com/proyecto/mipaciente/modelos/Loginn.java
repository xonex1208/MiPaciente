/**
 * @Loginn.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase modelo de Loginn para obtener los datos de Firebase
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.9
 */

package com.proyecto.mipaciente.modelos;

public class Loginn
{

    private String usuario;
    private String contrasena;

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    public String getContrasena()
    {
        return contrasena;
    }

    public void setContrasena(String contrasena)
    {
        this.contrasena = contrasena;
    }
}
