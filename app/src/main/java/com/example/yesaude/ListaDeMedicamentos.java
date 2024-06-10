package com.example.yesaude;

public class ListaDeMedicamentos{

    String meds;
    String horario;

    public ListaDeMedicamentos(String meds, String horario) {
        this.meds = meds;
        this.horario = horario;
    }

    public String getMeds() {
        return meds;
    }

    public void setMeds(String meds) {
        this.meds = meds;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}

