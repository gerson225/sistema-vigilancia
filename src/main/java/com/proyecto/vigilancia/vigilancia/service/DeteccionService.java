package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.repository.DeteccionRepository;
import com.proyecto.vigilancia.vigilancia.repository.PersonaRepository;

@Service
public class DeteccionService {
    private final DeteccionRepository repo;
    private final PersonaRepository personaRepository;
    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    // 🔥 CONSTRUCTOR CORREGIDO: Ahora recibe todas las dependencias
    public DeteccionService(DeteccionRepository repo,
                           PersonaRepository personaRepository,
                           AlertaService alertaService,
                           UsuarioService usuarioService) {
        this.repo = repo;
        this.personaRepository = personaRepository;
        this.alertaService = alertaService;
        this.usuarioService = usuarioService;
    }

    // 🔥 MÉTODOS EXISTENTES (con pequeñas mejoras)
    public List<Deteccion> listarDetecciones() {
        List<Deteccion> detecciones = repo.findAll();
        
        System.out.println("🔍 Número de detecciones en BD: " + detecciones.size());
        
        for (int i = 0; i < detecciones.size(); i++) {
            Deteccion det = detecciones.get(i);
            System.out.println("📊 Detección " + (i + 1) + ": ID=" + det.getIdDeteccion() + 
                             ", Persona=" + (det.getPersona() != null ? det.getPersona().getNombre() : "NULL") +
                             ", Cámara=" + (det.getCamara() != null ? det.getCamara().getNombreCamara() : "NULL") +
                             ", ImagenURL=" + det.getImagenUrl());
        }
        
        // Asegurar URLs de imagen válidas
        for (Deteccion det : detecciones) {
            if (det.getImagenUrl() == null || det.getImagenUrl().isEmpty()) {
                det.setImagenUrl("/images/persona1.png");
            } else if (!det.getImagenUrl().startsWith("/images/")) {
                det.setImagenUrl("/images/" + det.getImagenUrl());
            }
        }
        
        return detecciones;
    }

    public List<Deteccion> filtrarDetecciones(Map<String, String> filtros) {
        System.out.println("🔍 Filtros recibidos: " + filtros);

        List<Deteccion> todasDetecciones = listarDetecciones();
        System.out.println("📊 Total detecciones: " + todasDetecciones.size());
        
        List<Deteccion> filtradas = todasDetecciones.stream()
            .filter(det -> filtrarPorPersona(det, filtros.get("persona")))
            .filter(det -> filtrarPorCamara(det, filtros.get("camara")))
            .filter(det -> filtrarPorTipo(det, filtros.get("tipo")))
            .filter(det -> filtrarPorFecha(det, filtros.get("fecha")))
            .collect(Collectors.toList());

        System.out.println("✅ Detecciones filtradas: " + filtradas.size());
        return filtradas;
    }

    private boolean filtrarPorPersona(Deteccion deteccion, String filtroPersona) {
        if (filtroPersona == null || filtroPersona.isEmpty()) return true;
        
        String nombrePersona = deteccion.getPersona() != null ? 
            deteccion.getPersona().getNombre() : "";
        return nombrePersona.toLowerCase().contains(filtroPersona.toLowerCase());
    }

    private boolean filtrarPorCamara(Deteccion deteccion, String filtroCamara) {
        if (filtroCamara == null || filtroCamara.isEmpty()) return true;
        if (deteccion.getCamara() == null) return false;
        
        return filtroCamara.equals(deteccion.getCamara().getIdCamara().toString());
    }

    private boolean filtrarPorTipo(Deteccion deteccion, String filtroTipo) {
        if (filtroTipo == null || filtroTipo.isEmpty()) return true;
        
        return filtroTipo.equals(deteccion.getTipoDeteccion().toString());
    }

    private boolean filtrarPorFecha(Deteccion deteccion, String filtroFecha) {
        if (filtroFecha == null || filtroFecha.isEmpty()) return true;
        
        // Convertir LocalDateTime a LocalDate para comparar solo la fecha
        return deteccion.getFechaHora().toLocalDate().toString().equals(filtroFecha);
    }

    // 🔥 NUEVOS MÉTODOS PARA LAS FUNCIONALIDADES EXTRA

    // 1. Eliminar detección (solo admin)
    public void eliminarDeteccion(Integer idDeteccion) {
        // Verificar que no tenga alertas asociadas
        boolean tieneAlertas = alertaService.tieneAlertasAsociadas(idDeteccion);
        if (tieneAlertas) {
            throw new RuntimeException("No se puede eliminar: tiene alertas asociadas");
        }
        repo.deleteById(idDeteccion);
    }

    // 2. Vincular persona
    public Deteccion vincularPersona(Integer idDeteccion, Integer idPersona) {
        Deteccion deteccion = buscarPorId(idDeteccion);
        
        Persona persona = personaRepository.findById(idPersona)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        
        deteccion.setPersona(persona);
        
        // Actualizar última detección de la persona
        persona.setUltimaDeteccion(LocalDateTime.now());
        personaRepository.save(persona);
        
        return repo.save(deteccion);
    }

    // 3. Marcar como revisado
    public Deteccion marcarComoRevisado(Integer idDeteccion, String observaciones, Integer idUsuario) {
        Deteccion deteccion = buscarPorId(idDeteccion);
        
        // Si no tienes campo 'revisado' en la entidad, podemos usar las observaciones
        String nuevasObservaciones = deteccion.getObservaciones() != null ? 
            deteccion.getObservaciones() + " | Revisado: " + observaciones : 
            "Revisado: " + observaciones;
        
        deteccion.setObservaciones(nuevasObservaciones);
        
        return repo.save(deteccion);
    }

    // 4. Generar alerta con tipo específico
    public Alerta generarAlertaDesdeDeteccion(Integer idDeteccion, String tipoAlerta, 
                                             String descripcion, String nivelCriticidad, 
                                             Integer idUsuario) {
        Deteccion deteccion = buscarPorId(idDeteccion);
        
        Usuario usuario = usuarioService.buscarPorId(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Alerta alerta = new Alerta();
        alerta.setDeteccion(deteccion);
        alerta.setTipoAlerta(tipoAlerta != null ? tipoAlerta : "GENERADA_DESDE_DETECCION");
        alerta.setDescripcion(descripcion != null ? descripcion : 
            "Alerta generada desde detección #" + idDeteccion);
        alerta.setFechaAlerta(LocalDateTime.now());
        alerta.setNivelCriticidad(nivelCriticidad != null ? nivelCriticidad : "MEDIA");
        alerta.setEstadoAlerta("PENDIENTE");
        alerta.setUsuario(usuario);
        
        // Inicializar historial
        String historialInicial = String.format("[%s] CREADA: Alerta generada desde detección #%d por %s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            idDeteccion, usuario.getNombre());
        alerta.setHistorial(historialInicial);
        
        return alertaService.registrarAlerta(alerta);
    }

    // 5. Escalar directamente a alerta crítica (solo admin)
    public Alerta escalarAAleraCritica(Integer idDeteccion, String motivo, Integer idUsuario) {
        Deteccion deteccion = buscarPorId(idDeteccion);
        
        Usuario usuario = usuarioService.buscarPorId(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Alerta alerta = new Alerta();
        alerta.setDeteccion(deteccion);
        alerta.setTipoAlerta("ESCALADA_DIRECTAMENTE");
        alerta.setDescripcion("Escalada directamente desde detección. Motivo: " + motivo);
        alerta.setFechaAlerta(LocalDateTime.now());
        alerta.setNivelCriticidad("CRITICA");
        alerta.setEstadoAlerta("PENDIENTE");
        alerta.setUsuario(usuario);
        
        // Historial
        String historial = String.format("[%s] ESCALADA_DIRECTAMENTE: Desde detección por %s. Motivo: %s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            usuario.getNombre(), motivo);
        alerta.setHistorial(historial);
        
        return alertaService.registrarAlerta(alerta);
    }

    // 6. Obtener detección por ID (método auxiliar reutilizable)
    public Deteccion buscarPorId(Integer idDeteccion) {
        return repo.findById(idDeteccion)
            .orElseThrow(() -> new RuntimeException("Detección no encontrada"));
    }
    
    // 🔥 NUEVO: Método para obtener todas las detecciones sin procesar imágenes
    public List<Deteccion> obtenerTodasLasDetecciones() {
        return repo.findAll();
    }
}