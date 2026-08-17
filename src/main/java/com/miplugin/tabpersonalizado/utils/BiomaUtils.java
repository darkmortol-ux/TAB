package com.miplugin.tabpersonalizado.utils;

import org.bukkit.block.Biome;

public class BiomaUtils {

    private BiomaUtils() {
    }

    /**
     * Convierte una clave de bioma tipo "frozen_ocean" en "Frozen Ocean".
     */
    public static String obtenerNombreBonito(Biome bioma) {
        if (bioma == null || bioma.getKey() == null) return "Desconocido";

        String clave = bioma.getKey().getKey();
        String[] palabras = clave.split("_");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (palabra.isEmpty()) continue;
            if (resultado.length() > 0) resultado.append(" ");
            resultado.append(Character.toUpperCase(palabra.charAt(0)))
                    .append(palabra.substring(1));
        }

        return resultado.toString();
    }
}
