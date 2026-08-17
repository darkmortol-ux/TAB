package com.miplugin.tabpersonalizado.modelos;

public class RangoTab {

    private final String tipo; // "rangosmc" o "personalizado"
    private final String nombre;
    private final String prefixManual; // solo se usa si tipo = personalizado

    public RangoTab(String tipo, String nombre, String prefixManual) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.prefixManual = prefixManual;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrefixManual() {
        return prefixManual;
    }
}
