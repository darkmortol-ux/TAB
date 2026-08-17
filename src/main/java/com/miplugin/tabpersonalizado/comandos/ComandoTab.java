package com.miplugin.tabpersonalizado.comandos;

import com.miplugin.tabpersonalizado.TabPersonalizado;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ComandoTab implements CommandExecutor {

    private final TabPersonalizado plugin;

    public ComandoTab(TabPersonalizado plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender remitente, Command comando, String etiqueta, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            remitente.sendMessage(ChatColor.YELLOW + "Uso: /tab reload");
            return true;
        }

        if (!remitente.hasPermission("tabpersonalizado.admin")) {
            remitente.sendMessage(ChatColor.RED + "No tienes permiso para usar este comando.");
            return true;
        }

        plugin.getGestorTab().cargarConfiguracion();
        plugin.getGestorTab().actualizarTodosLosJugadoresConectados();
        remitente.sendMessage(ChatColor.GREEN + "TabPersonalizado recargado correctamente.");
        return true;
    }
}
