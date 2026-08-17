package com.miplugin.tabpersonalizado.hooks;

import com.miplugin.tabpersonalizado.TabPersonalizado;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Enlace con RangosMC via reflexion, sin depender de su jar en
 * tiempo de compilacion (mismo patron que usas entre SistemaClases
 * y EventosPersonalizados).
 */
public class HookRangosMC {

    private Object gestorRangos;
    private Method metodoGetRango;
    private Method metodoGetPrefijo;
    private boolean disponible = false;

    public void inicializar(TabPersonalizado plugin) {
        disponible = false;

        Plugin pluginRangosMC = Bukkit.getPluginManager().getPlugin("RangosMC");
        if (pluginRangosMC == null || !pluginRangosMC.isEnabled()) {
            plugin.getLogger().warning("RangosMC no esta activo: los rangos de tipo 'rangosmc' se omitiran del TAB.");
            return;
        }

        try {
            Method metodoGetGestor = pluginRangosMC.getClass().getMethod("getGestorRangos");
            gestorRangos = metodoGetGestor.invoke(pluginRangosMC);

            metodoGetRango = gestorRangos.getClass().getMethod("getRango", String.class);
            Class<?> claseRango = metodoGetRango.getReturnType();
            metodoGetPrefijo = claseRango.getMethod("getPrefijo");

            disponible = true;
            plugin.getLogger().info("RangosMC detectado: se usara para el listado de rangos en el TAB.");
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().warning("No se pudo enlazar con RangosMC (¿cambio su API?): " + e.getMessage());
        }
    }

    public String obtenerPrefijo(String nombreRango) {
        if (!disponible) return null;
        try {
            Object rango = metodoGetRango.invoke(gestorRangos, nombreRango);
            if (rango == null) return null;
            return (String) metodoGetPrefijo.invoke(rango);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
