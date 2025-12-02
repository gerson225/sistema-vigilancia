// session-manager.js - VERSIÓN ROBUSTA Y UNIVERSAL

// 🔥 FUNCIÓN PRINCIPAL QUE SE EJECUTA EN TODAS LAS PÁGINAS
async function inicializarAplicacion() {
    
    
    try {
        // 1. VERIFICAR SESIÓN
        const sessionValida = await verificarSesion();
        if (!sessionValida) {
            return;
        }
        
        // 2. ASEGURAR ESTRUCTURA HTML CORRECTA
        asegurarEstructuraHTML();
        
        // 3. GENERAR SIDEBAR
        await generarSidebar();
        
        // 4. APLICAR ESTILOS DINÁMICOS
        aplicarEstilosDinamicos();
        
        console.log('✅ Aplicación inicializada correctamente');
        
    } catch (error) {
        console.error('❌ Error inicializando aplicación:', error);
    }
}

// 🔥 VERIFICAR SESIÓN
async function verificarSesion() {
    try {
        const response = await fetch('/api/check-session');
        
        if (!response.ok) {
            throw new Error('Error en verificación de sesión');
        }
        
        const sessionData = await response.json();
        
        if (!sessionData.success) {
            window.location.href = '/login';
            return false;
        }
        
        // Guardar datos en sessionStorage
        sessionStorage.setItem('rol', sessionData.rol);
        sessionStorage.setItem('idUsuario', sessionData.idUsuario);
        sessionStorage.setItem('nombre', sessionData.nombre || 'Usuario');
        
        return true;
        
    } catch (error) {
        console.error('Error verificando sesión:', error);
        window.location.href = '/login';
        return false;
    }
}

// 🔥 ASEGURAR QUE EL HTML TENGA LA ESTRUCTURA CORRECTA
function asegurarEstructuraHTML() {
    
    
    // Verificar si existe el sidebar
    let sidebar = document.querySelector('.sidebar');
    if (!sidebar) {
        console.log('⚠️ No se encontró .sidebar, creando uno...');
        sidebar = document.createElement('div');
        sidebar.className = 'sidebar';
        document.body.insertBefore(sidebar, document.body.firstChild);
    }
    
    // Verificar si existe main-content
    let mainContent = document.querySelector('.main-content');
    if (!mainContent) {
        console.log('⚠️ No se encontró .main-content, buscando contenido principal...');
        
        // Buscar el contenido principal (diferentes patrones)
        const posiblesContenedores = [
            document.querySelector('div[style*="padding: 20px"]'),
            document.querySelector('div:not(.sidebar)'),
            document.body.children[1] // Segundo hijo después del sidebar
        ];
        
        for (const contenedor of posiblesContenedores) {
            if (contenedor && contenedor !== sidebar) {
                contenedor.className = 'main-content';
                mainContent = contenedor;
                break;
            }
        }
        
        // Si no se encuentra, crear uno
        if (!mainContent) {
            mainContent = document.createElement('div');
            mainContent.className = 'main-content';
            
            // Mover todo el contenido (excepto sidebar) al main-content
            const elementos = Array.from(document.body.children);
            elementos.forEach(element => {
                if (element !== sidebar) {
                    mainContent.appendChild(element);
                }
            });
            
            document.body.appendChild(mainContent);
        }
    }
    
    // Aplicar estilos CSS necesarios
    aplicarEstilosBase();
}

// 🔥 GENERAR SIDEBAR DINÁMICAMENTE
async function generarSidebar() {
    const rol = sessionStorage.getItem('rol');
    const nombreUsuario = sessionStorage.getItem('nombre') || 'Usuario';
    const paginaActual = window.location.pathname;
    
    console.log(`🔧 Generando sidebar para: ${nombreUsuario} (${rol})`);
    
    const esAdministrador = rol === 'ADMINISTRADOR';
    
    // Construir HTML del sidebar
    let sidebarHTML = `
        <h2>📹 SIVI</h2>
        <a href="/dashboard" class="${paginaActual === '/dashboard' ? 'active' : ''}">📊 Dashboard</a>
        <a href="/alertas" class="${paginaActual === '/alertas' ? 'active' : ''}">🚨 Alertas</a>
        <a href="/camaras" class="${paginaActual === '/camaras' ? 'active' : ''}">📹 Cámaras</a>
        <a href="/detecciones" class="${paginaActual === '/detecciones' ? 'active' : ''}">🔍 Detecciones</a>
    `;
    
    // Solo administradores pueden ver Personas
    if (esAdministrador) {
        const enPersonas = paginaActual === '/vistas/personas' || paginaActual === '/personas';
        sidebarHTML += `<a href="/vistas/personas" class="${enPersonas ? 'active' : ''}">👥 Personas</a>`;
    }
    
    // Sección de Configuración (solo para administradores)
    if (esAdministrador) {
        const enConfiguracion = paginaActual.includes('/configuracion');
        sidebarHTML += `
            <div class="section-title">CONFIGURACIÓN</div>
            <a href="/configuracion/camaras" class="${paginaActual === '/configuracion/camaras' ? 'active' : ''} submenu-item">
                📷 Gestión de Cámaras
            </a>
            <a href="/configuracion/usuarios" class="${paginaActual === '/configuracion/usuarios' ? 'active' : ''} submenu-item">
                👤 Gestión de Usuarios
            </a>
            <a href="/configuracion/parametros" class="${paginaActual === '/configuracion/parametros' ? 'active' : ''} submenu-item">
                ⚙️ Parámetros del Sistema
            </a>
        `;
    }
    
    // Información del usuario y cerrar sesión
    sidebarHTML += `
        <div class="user-info">
            <div class="user-name">${nombreUsuario}</div>
            <div class="user-role">${rol}</div>
        </div>
        <a href="#" onclick="cerrarSesion()" class="logout-btn">🚪 Cerrar Sesión</a>
    `;
    
    // Insertar en el DOM
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
        sidebar.innerHTML = sidebarHTML;
    }
}

// 🔥 APLICAR ESTILOS BASE DINÁMICAMENTE
function aplicarEstilosBase() {
    if (!document.querySelector('#dynamic-styles')) {
        const style = document.createElement('style');
        style.id = 'dynamic-styles';
        style.textContent = `
            /* Estilos para asegurar consistencia */
            .section-title {
                margin: 20px 0 5px 20px;
                font-size: 12px;
                color: #7f8c8d;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 1px;
            }
            
            .submenu-item {
                padding-left: 30px !important;
                font-size: 14px;
            }
            
            .sidebar a.active {
                background-color: #34495e !important;
                border-left: 4px solid #3498db !important;
                color: white !important;
                font-weight: bold;
            }
            
            .sidebar a {
                color: #ecf0f1 !important;
                text-decoration: none !important;
                padding: 12px 20px !important;
                display: block !important;
                transition: all 0.3s !important;
                border-left: 4px solid transparent !important;
                font-size: 15px !important;
            }
            
            .sidebar a:hover {
                background-color: #34495e !important;
                border-left: 4px solid #3498db !important;
                padding-left: 16px !important;
            }
            
            .user-info {
                margin-top: auto;
                padding: 15px;
                border-top: 1px solid #34495e;
                background: rgba(0,0,0,0.1);
            }
            
            .user-name {
                color: white;
                font-weight: bold;
                font-size: 14px;
                margin-bottom: 5px;
            }
            
            .user-role {
                color: #bdc3c7;
                font-size: 12px;
            }
            
            .logout-btn {
                display: block;
                padding: 12px 20px;
                color: #e74c3c !important;
                text-align: center;
                border-top: 1px solid #34495e;
                background: rgba(231, 76, 60, 0.1);
                transition: background 0.3s;
                text-decoration: none;
            }
            
            .logout-btn:hover {
                background: rgba(231, 76, 60, 0.2) !important;
            }
            
            /* Asegurar que body tenga flex */
            body {
                display: flex !important;
                margin: 0 !important;
                height: 100vh !important;
                overflow: hidden !important;
            }
            
            /* Asegurar que main-content tenga margen */
            .main-content {
                margin-left: 220px !important;
                flex: 1 !important;
                padding: 20px !important;
                overflow-y: auto !important;
                height: 100vh !important;
                box-sizing: border-box !important;
            }
        `;
        document.head.appendChild(style);
    }
}

// 🔥 APLICAR ESTILOS DINÁMICOS ADICIONALES
function aplicarEstilosDinamicos() {
    // Asegurar que el sidebar tenga posición fija
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
        sidebar.style.position = 'fixed';
        sidebar.style.left = '0';
        sidebar.style.top = '0';
        sidebar.style.width = '220px';
        sidebar.style.height = '100vh';
        sidebar.style.backgroundColor = '#2c3e50';
        sidebar.style.zIndex = '1000';
    }
}

// 🔥 CERRAR SESIÓN
async function cerrarSesion() {
    try {
        await fetch('/api/logout', { method: 'POST' });
        sessionStorage.clear();
        localStorage.clear();
        window.location.href = '/login';
    } catch (error) {
        console.error('Error cerrando sesión:', error);
        window.location.href = '/login';
    }
}

// 🔥 FUNCIONES PARA VERIFICAR ROLES (añade al final del archivo)
function esAdministrador() {
    const rol = sessionStorage.getItem('rol');
    return rol === 'ADMINISTRADOR';
}

function esOperador() {
    const rol = sessionStorage.getItem('rol');
    return rol === 'OPERADOR';
}

function obtenerRol() {
    return sessionStorage.getItem('rol') || '';
}

function obtenerIdUsuario() {
    return sessionStorage.getItem('idUsuario');
}

function obtenerNombreUsuario() {
    return sessionStorage.getItem('nombre') || 'Usuario';
}

// Hacerlas globales
window.esAdministrador = esAdministrador;
window.esOperador = esOperador;
window.obtenerRol = obtenerRol;
window.obtenerIdUsuario = obtenerIdUsuario;
window.obtenerNombreUsuario = obtenerNombreUsuario;

// 🔥 INICIALIZAR CUANDO EL DOM ESTÉ LISTO
document.addEventListener('DOMContentLoaded', function() {
    
    setTimeout(() => {
        inicializarAplicacion();
    }, 100);
});

// También ejecutar cuando la ventana se carga
window.addEventListener('load', function() {
    
    // Re-aplicar estilos por si acaso
    setTimeout(aplicarEstilosDinamicos, 500);
});

// Hacer funciones globales
window.cerrarSesion = cerrarSesion;