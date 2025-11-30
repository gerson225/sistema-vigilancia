package com.proyecto.vigilancia.vigilancia.service;

import java.sql.Timestamp;
import java.util.Optional;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.repository.UsuarioRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    // Registrar usuario con idRol explícito
    public Usuario registrarUsuario(String nombre, String usuario, String contrasenaPlano, String rol, Integer idRol) {
        if (contrasenaPlano == null || contrasenaPlano.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if ("ADMINISTRADOR".equals(rol) && !usuario.matches("^A\\d{8}$")) {
            throw new IllegalArgumentException("Usuario administrador debe comenzar con A y tener 8 dígitos");
        }
        if ("OPERADOR".equals(rol) && !usuario.matches("^O\\d{8}$")) {
            throw new IllegalArgumentException("Usuario operador debe comenzar con O y tener 8 dígitos");
        }

        if (idRol == 1) rol = "ADMINISTRADOR";
        else if (idRol == 2) rol = "OPERADOR";
        else rol = "USUARIO";

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setUsuario(usuario);
        nuevo.setContrasena(encoder.encode(contrasenaPlano));
        nuevo.setRol(rol);
        nuevo.setIdRol(idRol);
        nuevo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        return usuarioRepository.save(nuevo);
    }

    // Sobrecarga para deducir idRol automáticamente
    public Usuario registrarUsuario(String nombre, String usuario, String contrasenaPlano, String rol) {
        Integer idRol;
        if ("ADMINISTRADOR".equals(rol)) {
            idRol = 1;
        } else if ("OPERADOR".equals(rol)) {
            idRol = 2;
        } else {
            idRol = 3;
        }
        return registrarUsuario(nombre, usuario, contrasenaPlano, rol, idRol);
    }

    // Validación de login con logs
    public Optional<Usuario> validarLogin(String usuario, String contrasenaIngresada) {
        Optional<Usuario> u = usuarioRepository.findByUsuario(usuario);
        if (u.isEmpty()) {
            System.out.println("Usuario no encontrado: " + usuario);
            return Optional.empty();
        }

        Usuario encontrado = u.get();
        boolean ok = encoder.matches(contrasenaIngresada, encontrado.getContrasena());
        System.out.println("Password match: " + ok);

        if (!ok) {
            System.out.println("Contraseña incorrecta para usuario: " + usuario);
            return Optional.empty();
        }

        if ("ADMINISTRADOR".equalsIgnoreCase(encontrado.getRol())) {
        if (!usuario.matches("^A\\d{8}$")) {
            System.out.println("Regex fallo para ADMINISTRADOR: " + usuario);
            return Optional.empty();
        }
    } else if ("OPERADOR".equalsIgnoreCase(encontrado.getRol())) {
        if (!usuario.matches("^O\\d{8}$")) {
            System.out.println("Regex fallo para OPERADOR: " + usuario);
            return Optional.empty();
        }
    }

    System.out.println("Login correcto para usuario: " + usuario + " con rol: " + encontrado.getRol());
    return Optional.of(encontrado);    
    }

    // Método para encriptar contraseñas inseguras
    public void encriptarContraseñasInseguras() {
        var usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            String pass = u.getContrasena();
            if (pass == null || pass.isEmpty() || !pass.startsWith("$2a$")) {
                String nueva = (pass == null || pass.isEmpty()) ? "Temporal123" : pass;
                u.setContrasena(encoder.encode(nueva));
                usuarioRepository.save(u);
            }
        }
    }
    public Optional<Usuario> buscarPorId(Integer idUsuario) {
    return usuarioRepository.findById(idUsuario);
}
}
