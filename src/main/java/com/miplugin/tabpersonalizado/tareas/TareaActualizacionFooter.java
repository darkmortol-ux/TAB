package com.miplugin.tabpersonalizado.tareas;

import com.miplugin.tabpersonalizado.TabPersonalizado;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TareaActualizacionFooter extends BukkitRunnable {

    private final TabPersonalizado plugin;

    public TareaActualizacionFooter(TabPersonalizado plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player jugador : plugin.getServer().getOnlinePlayers()) {
            String header = plugin.getGestorTab().getHeaderCacheado(jugador);
            String footer = plugin.getGestorTab().construirFooter(jugador);
            jugador.setPlayerListHeaderFooter(header, footer);
        }
    }
}
