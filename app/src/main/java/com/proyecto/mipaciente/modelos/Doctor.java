/**
 * @Doctor.java 17/octubre/2019
 *
 * Copyright 2019 Helix, todos los derechos reservados.
 */

/**
 * Clase modelo del Doctor para guardar y obtener los datos de Firebase
 *
 * @author Cesar Alfredo Ramirez Orozco
 * @version 1.0.2 22-noviembre-2019

 * @since 0.0.8
 */

package com.proyecto.mipaciente.modelos;

public class Doctor
{
    private String nombre;
    private String apellidos;
    private String email;
    private String especialidad;
    private String contrasena;
    private double telefono;
    private int numeroDeCedula;
    private String fechaNacimiento;
    private String sexo;

    public Doctor(String nombre,
                  String apellidos,
                  String email,
                  String especialidad,
                  String contrasena,
                  double telefono,
                  int numeroDeCedula,
                  String fechaNacimiento,
                  String sexo)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.especialidad = especialidad;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.numeroDeCedula = numeroDeCedula;
        this.fechaNacimiento=fechaNacimiento;
        this.sexo=sexo;
    }

    public Doctor()
    {

    }

    public String getFechaNacimiento()
    {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento)
    {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo()
    {
        return sexo;
    }

    public void setSexo(String sexo)
    {
        this.sexo = sexo;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setEspecialidad(String especialidad)
    {
        this.especialidad = especialidad;
    }

    public void setContrasena(String contrasena)
    {
        this.contrasena = contrasena;
    }

    public void setTelefono(double telefono)
    {
        this.telefono = telefono;
    }

    public void setNumeroDeCedula(int numeroDeCedula)
    {
        this.numeroDeCedula = numeroDeCedula;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public String getEmail()
    {
        return email;
    }

    public String getEspecialidad()
    {
        return especialidad;
    }

    public String getContrasena()
    {
        return contrasena;
    }

    public double getTelefono()
    {
        return telefono;
    }

    public int getNumeroDeCedula()
    {
        return numeroDeCedula;
    }

}
