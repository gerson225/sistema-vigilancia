// session-manager.js
async function verificarSesionYGenerarSidebar() {
    try {
        const response = await fetch('/api/check-session');
        
        if (!response.ok) {
            throw new Error('Sesión no válida');
        }
        
        const sessionData = await response.json();
        
        if (!sessionData.success) {
            // Redirigir al login
            window.location.href = '/login';
            return false;
        }
        
        // 🔥 ACTUALIZAR SESSIONSTORAGE CON DATOS ACTUALES
        sessionStorage.setItem('rol', sessionData.rol);
        sessionStorage.setItem('idUsuario', sessionData.idUsuario);
        sessionStorage.setItem('usuario', sessionData.usuario || '');
        
        // 🔥 GENERAR SIDEBAR DINÁMICO
        generarSidebarSegunRol(sessionData.rol, sessionData.nombre || sessionData.usuario);
        
        return true;
        
    } catch (error) {
        console.error('Error verificando sesión:', error);
        window.location.href = '/login';
        return false;
    }
}

function generarSidebarSegunRol(rol, nombreUsuario) {
    const esAdministrador = rol === 'ADMINISTRADOR';
    
    let sidebarHTML = `
        <h2>SIVI</h2>
        <a href="/dashboard">Dashboard</a>
        <a href="/alertas">Alertas</a>
        <a href="/camaras">Cámaras</a>
        <a href="/detecciones">Detecciones</a>
    `;
    
    // 🔐 SOLO ADMINISTRADORES PUEDEN VER PERSONAS Y CONFIGURACIÓN
    if (esAdministrador) {
        sidebarHTML += `
            <a href="/vistas/personas">Personas</a>
            <div class="submenu">
                <a href="/configuracion/camaras">📷 Cámaras</a>
                <a href="/configuracion/usuarios">👥 Usuarios</a>
                <a href="/configuracion/parametros">⚙️ Parámetros</a>
            </div>
        `;
    }
    
    sidebarHTML += `
        <div style="margin-top: 20px; padding: 10px; border-top: 1px solid #34495e;">
            <small>Usuario: <strong>${nombreUsuario}</strong></small><br>
            <small>Rol: <strong>${rol}</strong></small>
        </div>
        <a href="#" onclick="cerrarSesion()" style="margin-top: 10px; color: #e74c3c;">🚪 Cerrar Sesión</a>
    `;
    
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
        sidebar.innerHTML = sidebarHTML;
    }
}

async function cerrarSesion() {
    try {
        await fetch('/api/logout', { method: 'POST' });
        // Limpiar storage
        sessionStorage.clear();
        localStorage.clear();
        window.location.href = '/login';
    } catch (error) {
        console.error('Error cerrando sesión:', error);
        window.location.href = '/login';
    }
}

// 🔥 VERIFICAR SI LA SESIÓN HA EXPIRADO (30 minutos)
function verificarExpiracionSesion() {
    const timestamp = sessionStorage.getItem('timestamp');
    if (timestamp) {
        const ahora = new Date().getTime();
        const tiempoTranscurrido = ahora - parseInt(timestamp);
        const minutosTranscurridos = tiempoTranscurrido / (1000 * 60);
        
        if (minutosTranscurridos > 30) {
            cerrarSesion();
        }
    }
}

// 🔥 INICIALIZAR AL CARGAR LA PÁGINA
document.addEventListener('DOMContentLoaded', function() {
    // Verificar expiración cada minuto
    setInterval(verificarExpiracionSesion, 60000);
    
    // Verificar sesión y generar sidebar
    setTimeout(() => {
        verificarSesionYGenerarSidebar();
    }, 100);
});