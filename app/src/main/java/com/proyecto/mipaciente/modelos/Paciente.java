package com.proyecto.mipaciente.modelos;

public class Paciente {
    private String nombre;
    private String apellidos;
    private String email;
    private int telefono;
    private String fechaNacimiento;
    private int edad;
    private String sexo;
    private String direccion;
    private String estadoCivil;
    private String parentesco;
    private String ocupacion;
    private String redSocial;

    public Paciente(String nombre,
                    String apellidos,
                    String email,
                    int telefono,
                    String fechaNacimiento,
                    int edad,
                    String sexo,
                    String direccion,
                    String estadoCivil,
                    String parentesco,
                    String ocupacion,
                    String redSocial) {
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
    }

    public Paciente()
    {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }


}
