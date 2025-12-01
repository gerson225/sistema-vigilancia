package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import com.proyecto.vigilancia.vigilancia.entity.ParametroSistema;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.repository.ParametroSistemaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParametroSistemaService {

    private final ParametroSistemaRepository parametroRepository;
    private final UsuarioService usuarioService;

    public ParametroSistemaService(ParametroSistemaRepository parametroRepository, UsuarioService usuarioService) {
        this.parametroRepository = parametroRepository;
        this.usuarioService = usuarioService;
    }

    public List<ParametroSistema> obtenerTodosLosParametros() {
        return parametroRepository.findAll();
    }

    public Optional<ParametroSistema> obtenerParametroPorNombre(String nombreParametro) {
        return parametroRepository.findByNombreParametro(nombreParametro);
    }

    public ParametroSistema guardarParametro(ParametroSistema parametro) {
        return parametroRepository.save(parametro);
    }

    public ParametroSistema actualizarParametro(String nombreParametro, String nuevoValor, Integer idUsuario) {
        Optional<ParametroSistema> parametroOpt = parametroRepository.findByNombreParametro(nombreParametro);
        
        if (parametroOpt.isPresent()) {
            ParametroSistema parametro = parametroOpt.get();
            parametro.setValor(nuevoValor);
            
            // Actualizar el usuario que modificó el parámetro
            Usuario usuario = usuarioService.buscarPorId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            parametro.setUsuario(usuario);
            
            return parametroRepository.save(parametro);
        } else {
            // Crear nuevo parámetro si no existe
            ParametroSistema nuevoParametro = new ParametroSistema();
            nuevoParametro.setNombreParametro(nombreParametro);
            nuevoParametro.setValor(nuevoValor);
            
            Usuario usuario = usuarioService.buscarPorId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            nuevoParametro.setUsuario(usuario);
            
            return parametroRepository.save(nuevoParametro);
        }
    }

    // 🔥 MÉTODOS PARA OBTENER PARÁMETROS ESPECÍFICOS CON VALORES POR DEFECTO
    
    public double obtenerConfianzaMinima() {
        return obtenerParametroDouble("confianza_minima", 0.8);
    }
    
    public int obtenerDiasRetencionDetecciones() {
        return obtenerParametroInt("dias_retencion_detecciones", 30);
    }
    
    public int obtenerDiasRetencionAlertas() {
        return obtenerParametroInt("dias_retencion_alertas", 90);
    }
    
    public boolean obtenerNotificacionesEmail() {
        return obtenerParametroBoolean("notificaciones_email", true);
    }
    
    public boolean obtenerNotificacionesSMS() {
        return obtenerParametroBoolean("notificaciones_sms", false);
    }
    
    public String obtenerIdioma() {
        return obtenerParametroString("idioma", "es");
    }
    
    public String obtenerZonaHoraria() {
        return obtenerParametroString("zona_horaria", "America/Lima");
    }
    
    // 🔥 MÉTODOS AUXILIARES PRIVADOS
    
    private String obtenerParametroString(String nombre, String valorDefecto) {
        return parametroRepository.findByNombreParametro(nombre)
            .map(ParametroSistema::getValor)
            .orElse(valorDefecto);
    }
    
    private int obtenerParametroInt(String nombre, int valorDefecto) {
        return parametroRepository.findByNombreParametro(nombre)
            .map(param -> {
                try {
                    return Integer.parseInt(param.getValor());
                } catch (NumberFormatException e) {
                    return valorDefecto;
                }
            })
            .orElse(valorDefecto);
    }
    
    private double obtenerParametroDouble(String nombre, double valorDefecto) {
        return parametroRepository.findByNombreParametro(nombre)
            .map(param -> {
                try {
                    return Double.parseDouble(param.getValor());
                } catch (NumberFormatException e) {
                    return valorDefecto;
                }
            })
            .orElse(valorDefecto);
    }
    
    private boolean obtenerParametroBoolean(String nombre, boolean valorDefecto) {
        return parametroRepository.findByNombreParametro(nombre)
            .map(param -> Boolean.parseBoolean(param.getValor()))
            .orElse(valorDefecto);
    }
}