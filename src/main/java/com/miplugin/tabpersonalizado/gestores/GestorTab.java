package com.miplugin.tabpersonalizado.gestores;

import com.miplugin.tabpersonalizado.TabPersonalizado;
import com.miplugin.tabpersonalizado.hooks.HookRangosMC;
import com.miplugin.tabpersonalizado.modelos.RangoTab;
import com.miplugin.tabpersonalizado.utils.BiomaUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GestorTab {

    private final TabPersonalizado plugin;
    private final HookRangosMC hookRangosMC = new HookRangosMC();

    private final Map<UUID, String> headerCache = new ConcurrentHashMap<>();

    private List<String> lineasHeader;
    private List<RangoTab> listaRangos;
    private String nombreOverworld;
    private String nombreNether;
    private String nombreEnd;
    private String footerFormato;
    private long ticksActualizacion;

    public GestorTab(TabPersonalizado plugin) {
        this.plugin = plugin;
    }

    public void cargarConfiguracion() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        hookRangosMC.inicializar(plugin);

        lineasHeader = config.getStringList("header-formato");

        listaRangos = new ArrayList<>();
        List<Map<?, ?>> listaCruda = config.getMapList("tab-rangos");
        for (Map<?, ?> entrada : listaCruda) {
            String tipo = String.valueOf(entrada.get("tipo"));
            String nombre = String.valueOf(entrada.get("nombre"));

            if ("personalizado".equalsIgnoreCase(tipo)) {
                Object prefixObj = entrada.get("prefix");
                String prefix = prefixObj != null ? String.valueOf(prefixObj) : "&f[" + nombre + "]";
                listaRangos.add(new RangoTab("personalizado", nombre, prefix));
            } else {
                listaRangos.add(new RangoTab("rangosmc", nombre, null));
            }
        }

        nombreOverworld = config.getString("mundos.overworld", "&aOverworld");
        nombreNether = config.getString("mundos.nether", "&cNether");
        nombreEnd = config.getString("mundos.end", "&5End");

        footerFormato = config.getString("footer-formato", "&7X: {x} Y: {y} Z: {z} &8| &fBioma: &7{bioma}");
        ticksActualizacion = config.getLong("actualizacion-footer-ticks", 20L);
    }

    public long getTicksActualizacion() {
        return ticksActualizacion;
    }

    /**
     * Arma el encabezado (fijo) para un jugador y lo guarda en cache.
     * Solo se recalcula al entrar o con /tab reload.
     */
    public void actualizarHeaderJugador(Player jugador) {
        FileConfiguration config = plugin.getConfig();
        String servidorNombre = config.getString("servidor.nombre", "&6Mi Servidor");
        String tienda = config.getString("servidor.tienda", "");
        String discord = config.getString("servidor.discord", "");
        String rangos = construirListaRangos();

        StringBuilder header = new StringBuilder();
        for (int i = 0; i < lineasHeader.size(); i++) {
            String linea = lineasHeader.get(i)
                    .replace("{nombre}", servidorNombre)
                    .replace("{rangos}", rangos)
                    .replace("{tienda}", tienda)
                    .replace("{discord}", discord);

            linea = aplicarPlaceholderAPI(jugador, linea);
            linea = colorear(linea);

            if (i > 0) header.append("\n");
            header.append(linea);
        }

        headerCache.put(jugador.getUniqueId(), header.toString());
    }

    private String construirListaRangos() {
        StringBuilder sb = new StringBuilder();
        for (RangoTab rango : listaRangos) {
            String texto;
            if (rango.getTipo().equalsIgnoreCase("rangosmc")) {
                texto = hookRangosMC.obtenerPrefijo(rango.getNombre());
                if (texto == null) continue; // el rango no existe o RangosMC no esta disponible
            } else {
                texto = rango.getPrefixManual();
            }
            if (sb.length() > 0) sb.append(" &f");
            sb.append(texto);
        }
        return colorear(sb.toString());
    }

    /**
     * Arma el pie de pagina (en vivo) con las coordenadas y el bioma
     * segun la dimension en la que este el jugador ahora mismo.
     */
    public String construirFooter(Player jugador) {
        World.Environment entorno = jugador.getWorld().getEnvironment();
        String nombreMundo = switch (entorno) {
            case NETHER -> nombreNether;
            case THE_END -> nombreEnd;
            default -> nombreOverworld;
        };

        Location loc = jugador.getLocation();
        Biome bioma = loc.getBlock().getBiome();
        String nombreBioma = BiomaUtils.obtenerNombreBonito(bioma);

        String texto = footerFormato
                .replace("{mundo}", nombreMundo)
                .replace("{x}", String.valueOf(loc.getBlockX()))
                .replace("{y}", String.valueOf(loc.getBlockY()))
                .replace("{z}", String.valueOf(loc.getBlockZ()))
                .replace("{bioma}", nombreBioma);

        texto = aplicarPlaceholderAPI(jugador, texto);
        return colorear(texto);
    }

    public String getHeaderCacheado(Player jugador) {
        return headerCache.getOrDefault(jugador.getUniqueId(), "");
    }

    public void quitarJugadorDeCache(Player jugador) {
        headerCache.remove(jugador.getUniqueId());
    }

    public void actualizarTodosLosJugadoresConectados() {
        for (Player jugador : plugin.getServer().getOnlinePlayers()) {
            actualizarHeaderJugador(jugador);
            jugador.setPlayerListHeaderFooter(getHeaderCacheado(jugador), construirFooter(jugador));
        }
    }

    private String colorear(String texto) {
        if (texto == null) return "";
        return ChatColor.translateAlternateColorCodes('&', texto);
    }

    private String aplicarPlaceholderAPI(Player jugador, String texto) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(jugador, texto);
        }
        return texto;
    }
}
