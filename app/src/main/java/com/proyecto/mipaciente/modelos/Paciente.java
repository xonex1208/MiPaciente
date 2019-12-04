/**
 * @Paciente.java 13/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase modelo Paciente para obtener y guardar
 * los datos en Firebase
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 1.0.0
 */

package com.proyecto.mipaciente.modelos;

public class Paciente
{
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String fechaNacimiento;
    private int edad;
    private String sexo;
    private String direccion;
    private String estadoCivil;
    private String parentesco;
    private String ocupacion;
    private String redSocial;
    private String idDelDoctor;
    private String imagenPaciente;

    public Paciente(String nombre,
                    String apellidos,
                    String email,
                    String telefono,
                    String fechaNacimiento,
                    int edad,
                    String sexo,
                    String direccion,
                    String estadoCivil,
                    String parentesco,
                    String ocupacion,
                    String redSocial,
                    String imagenPaciente,
                    String idDelDoctor) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.sexo = sexo;
        this.direccion = direccion;
        this.estadoCivil = estadoCivil;
        this.parentesco = parentesco;
        this.ocupacion = ocupacion;
        this.redSocial = redSocial;
        this.imagenPaciente = imagenPaciente;
        this.idDelDoctor = idDelDoctor;
    }

    public Paciente()
    {
        //Constructor public, requerido para Firebase
    }

    public String getIdDelDoctor() {
        return idDelDoctor;
    }

    public void setIdDelDoctor(String idDelDoctor) {
        this.idDelDoctor = idDelDoctor;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getFechaNacimiento()
    {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento)
    {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad()
    {
        return edad;
    }

    public void setEdad(int edad)
    {
        this.edad = edad;
    }

    public String getSexo()
    {
        return sexo;
    }

    public void setSexo(String sexo)
    {
        this.sexo = sexo;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public String getEstadoCivil()
    {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil)
    {
        this.estadoCivil = estadoCivil;
    }

    public String getParentesco()
    {
        return parentesco;
    }

    public void setParentesco(String parentesco)
    {
        this.parentesco = parentesco;
    }

    public String getOcupacion()
    {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion)
    {
        this.ocupacion = ocupacion;
    }

    public String getRedSocial()
    {
        return redSocial;
    }

    public void setRedSocial(String redSocial)
    {
        this.redSocial = redSocial;
    }

    public String getImagenPaciente()
    {
        return imagenPaciente;
    }

    public void setImagenPaciente(String imagenPaciente)
    {
        this.imagenPaciente = imagenPaciente;
    }
}
