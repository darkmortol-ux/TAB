package com.miplugin.tabpersonalizado;

import com.miplugin.tabpersonalizado.comandos.ComandoTab;
import com.miplugin.tabpersonalizado.escuchadores.EscuchadorConexion;
import com.miplugin.tabpersonalizado.gestores.GestorTab;
import com.miplugin.tabpersonalizado.tareas.TareaActualizacionFooter;
import org.bukkit.plugin.java.JavaPlugin;

public class TabPersonalizado extends JavaPlugin {

    private GestorTab gestorTab;
    private TareaActualizacionFooter tareaFooter;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        gestorTab = new GestorTab(this);
        gestorTab.cargarConfiguracion();

        getCommand("tab").setExecutor(new ComandoTab(this));
        getServer().getPluginManager().registerEvents(new EscuchadorConexion(this), this);

        tareaFooter = new TareaActualizacionFooter(this);
        tareaFooter.runTaskTimer(this, 0L, gestorTab.getTicksActualizacion());

        gestorTab.actualizarTodosLosJugadoresConectados();

        getLogger().info("========================================");
        getLogger().info(" TabPersonalizado activado correctamente.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        if (tareaFooter != null) {
            tareaFooter.cancel();
        }
        getLogger().info("TabPersonalizado ha sido desactivado.");
    }

    public GestorTab getGestorTab() {
        return gestorTab;
    }
}
