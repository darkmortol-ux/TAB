package com.miplugin.tabpersonalizado.escuchadores;

import com.miplugin.tabpersonalizado.TabPersonalizado;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EscuchadorConexion implements Listener {

    private final TabPersonalizado plugin;

    public EscuchadorConexion(TabPersonalizado plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void alEntrar(PlayerJoinEvent evento) {
        Player jugador = evento.getPlayer();
        plugin.getGestorTab().actualizarHeaderJugador(jugador);
        jugador.setPlayerListHeaderFooter(
                plugin.getGestorTab().getHeaderCacheado(jugador),
                plugin.getGestorTab().construirFooter(jugador)
        );
    }

    @EventHandler
    public void alSalir(PlayerQuitEvent evento) {
        plugin.getGestorTab().quitarJugadorDeCache(evento.getPlayer());
    }
}
