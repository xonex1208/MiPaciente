package com.proyecto.mipaciente.modelos;

public class ExpedientePaciente
{
    private boolean alergias;
    private boolean embarazo;
    private boolean extraccionDental;
    private boolean medicamento;
    private boolean hospitalizado;
    private String enfermedades;
    private String idPaciente;

    public void setAlergias(boolean alergias) {
        this.alergias = alergias;
    }

    public void setEmbarazo(boolean embarazo) {
        this.embarazo = embarazo;
    }

    public void setExtraccionDental(boolean extraccionDental) {
        this.extraccionDental = extraccionDental;
    }

    public void setMedicamento(boolean medicamento) {
        this.medicamento = medicamento;
    }

    public void setHospitalizado(boolean hospitalizado) {
        this.hospitalizado = hospitalizado;
    }

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public boolean isAlergias() {
        return alergias;
    }

    public boolean isEmbarazo() {
        return embarazo;
    }

    public boolean isExtraccionDental() {
        return extraccionDental;
    }

    public boolean isMedicamento() {
        return medicamento;
    }

    public boolean isHospitalizado() {
        return hospitalizado;
    }

    public String getEnfermedades() {
        return enfermedades;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public ExpedientePaciente() {
    }

    public ExpedientePaciente(boolean alergias,
                              boolean embarazo,
                              boolean extraccionDental,
                              boolean medicamento,
                              boolean hospitalizado,
                              String enfermedades,
                              String idPaciente) {
        this.alergias = alergias;
        this.embarazo = embarazo;
        this.extraccionDental = extraccionDental;
        this.medicamento = medicamento;
        this.hospitalizado = hospitalizado;
        this.enfermedades = enfermedades;
        this.idPaciente = idPaciente;
    }


}
