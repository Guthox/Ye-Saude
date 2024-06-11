package com.example.yesaude;

public class ListaDeMedicamentos {

    private int id; // Novo campo para identificador único
    private String meds;
    private String horario;

    public ListaDeMedicamentos(int id, String meds, String horario) {
        this.id = id;
        this.meds = meds;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
