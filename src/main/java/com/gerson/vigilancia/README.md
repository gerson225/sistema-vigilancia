# Sistema de Vigilancia y Monitoreo Inteligente

## Información General
- **Estudiantes:** Gerson David Ochoa Alarcón, Erick Martines  
- **Docente:** Milton Freddy Amache Sanchez  
- **Universidad:** Universidad Tecnológica del Perú (UTP)  
- **Fecha de inicio:** Septiembre 2025  
- **Fecha de finalización:** 1 de Diciembre 2025  

---

## Agradecimiento
Agradecemos a nuestro docente por la guía brindada en el desarrollo de este proyecto y a nuestros compañeros por su apoyo constante durante cada etapa del trabajo.

---

## Dedicatoria
Este proyecto está dedicado a nuestro aprendizaje como futuros ingenieros de sistemas, con el propósito de seguir mejorando en el diseño y desarrollo de soluciones tecnológicas innovadoras.

---

## Resumen
Este proyecto propone el desarrollo de un **sistema de vigilancia inteligente** que permite la detección y comparación de personas mediante **reconocimiento facial** y **biometría de marcha**.  
El sistema busca sentar las bases de un prototipo funcional desarrollado en **Spring Boot**, que registre personas y almacene sus datos de manera organizada.  
Esta propuesta representa una solución innovadora en el ámbito de la seguridad ciudadana y el control de accesos, aprovechando tecnologías de inteligencia artificial y visión artificial.

---

## Abstract
This project proposes the development of an **intelligent surveillance system** that enables the detection and comparison of individuals through **facial recognition** and **gait biometrics**.  
The objective is to establish the foundation of a functional prototype developed in **Spring Boot**, capable of registering people and storing their data in an organized manner, as part of an innovative solution in the field of security and monitoring.

---

## Realidad Problemática
La inseguridad ciudadana representa un problema creciente en diversos contextos.  
Los sistemas de vigilancia tradicionales presentan limitaciones, ya que dependen exclusivamente de la visibilidad del rostro para identificar personas.  
En situaciones donde el rostro está cubierto o no es visible, la efectividad de dichos sistemas disminuye considerablemente.  
Esto plantea la necesidad de soluciones que integren nuevas técnicas de identificación complementarias, como la biometría de la marcha, que analiza la forma de caminar de cada individuo.

---

## Justificación
La seguridad es una necesidad crucial en la sociedad actual.  
Sin embargo, los sistemas de vigilancia tradicionales se enfocan únicamente en el reconocimiento facial, dejando fuera otras características humanas identificables.  
Este proyecto propone un enfoque innovador al incorporar la **biometría de la marcha**, lo que permite aumentar la precisión en la identificación de personas y mejorar la cobertura de los sistemas de seguridad en espacios públicos y privados.  

Desde el punto de vista técnico, la implementación con **Spring Boot**, **MySQL** y librerías de visión por computadora busca demostrar la viabilidad de un sistema modular, escalable y eficiente para entornos de vigilancia inteligente.

---

## Objetivo General
Desarrollar un prototipo de sistema de vigilancia inteligente basado en **Spring Boot** que permita registrar y gestionar información de personas detectadas mediante reconocimiento facial y análisis de marcha.

---

## Objetivos Específicos
1. Analizar las técnicas de reconocimiento facial y de marcha aplicables en un sistema de vigilancia.  
2. Diseñar la arquitectura del sistema aplicando el patrón **Modelo–Vista–Controlador (MVC)**.  
3. Implementar un backend en **Spring Boot** que gestione el almacenamiento de datos de personas y registros de vigilancia.  
4. Desarrollar un módulo inicial que permita registrar personas y sus rostros en la base de datos.  
5. Proponer la integración de algoritmos de detección de marcha para fases futuras del proyecto.  
6. Evaluar el funcionamiento del prototipo mediante pruebas de detección y almacenamiento.

---

## Marco Teórico
- **Vigilancia Inteligente:** Los sistemas de vigilancia moderna integran análisis automatizado de video, detección de movimiento y reconocimiento de patrones para aumentar la seguridad y reducir la intervención humana.  
- **Reconocimiento Facial:** Técnica biométrica que identifica individuos a partir de rasgos faciales únicos, utilizando redes neuronales convolucionales (CNN).  
- **Biometría de la Marcha:** Método de identificación basado en el patrón de movimiento del cuerpo humano durante la caminata, útil cuando el rostro no es visible.  
- **Spring Boot:** Framework de Java que facilita la creación de aplicaciones web y APIs REST, basado en el patrón MVC y con integración nativa con bases de datos.  

Asimismo, la combinación de reconocimiento facial y biometría de marcha representa una tendencia clave en la seguridad inteligente moderna.  
Estos sistemas combinan visión artificial, aprendizaje automático y bases de datos relacionales para mejorar la detección y respuesta ante incidentes.  
El uso de frameworks como **Spring Boot** proporciona una arquitectura robusta, escalable y de fácil mantenimiento.

---

## Metodología
El proyecto se desarrolla bajo un enfoque de **investigación aplicada** con metodología **experimental**, orientada a la creación de un prototipo funcional que integre herramientas de software modernas.

### Fases principales
1. **Análisis de requerimientos:** Identificación de las necesidades de vigilancia y diseño de los casos de uso.  
2. **Diseño del sistema:** Creación del modelo de base de datos, estructura de clases y arquitectura MVC.  
3. **Implementación:** Desarrollo del backend en **Spring Boot** con conexión a base de datos **MySQL**.  
4. **Pruebas:** Verificación de funcionalidades básicas y validación de detección y registro de personas.  
5. **Documentación:** Redacción de informes y actualización continua en GitHub.

Adicionalmente, se aplicará la metodología ágil **SCRUM**, con entregas iterativas denominadas *sprints*.  
Cada sprint contempla la planificación, desarrollo de módulos, pruebas unitarias y revisión de avances en GitHub, garantizando trazabilidad y mejora continua.

---

## Cronograma de Actividades
| Semana | Actividad |
|--------|------------|
| 1 | Revisión bibliográfica y estado del arte |
| 2 | Configuración del proyecto en Spring Boot y GitHub |
| 3 | Diseño de la base de datos y entidades |
| 4 | Implementación del módulo de registro de personas |
| 5 | Pruebas iniciales y depuración |
| 6 | Documentación de resultados |
| 7 | Presentación del avance parcial |

> **Nota:** Cada actividad será supervisada semanalmente y documentada en GitHub mediante *commits* descriptivos y etiquetas por fase del proyecto.

---

## Desarrollo del Proyecto

### Aplicación
El sistema está siendo desarrollado en **Spring Boot** como un backend modular que permitirá:
- Registrar personas con sus datos y rostros.  
- Consultar información almacenada en la base de datos.  
- Preparar la integración de algoritmos de biometría de marcha para fases futuras.  

### Base de Datos
El sistema utiliza **MySQL** (o H2 en modo desarrollo) para la gestión de datos.  
Entidad inicial:  
- `Persona(id, nombre, foto)`  

La estructura se diseñará para facilitar la futura integración de módulos de análisis de video y almacenamiento de eventos.

---

## Resultados Esperados
- Un prototipo funcional que registre y consulte personas mediante reconocimiento facial.  
- Integración parcial de biometría de marcha para pruebas experimentales.  
- Documentación completa en GitHub siguiendo el formato académico oficial.  
- Validación del sistema con pruebas de detección y almacenamiento.  

---

## Conclusiones
El proyecto de vigilancia y monitoreo inteligente demuestra la viabilidad de integrar tecnologías de **visión artificial** y **biometría de marcha** en un entorno de seguridad automatizado.  
El uso de **Spring Boot** y **MySQL** permitió establecer una arquitectura sólida, escalable y de fácil mantenimiento.  
Asimismo, el trabajo colaborativo mediante **GitHub** facilitó el control de versiones, la trazabilidad y la documentación académica del desarrollo.

---

## 📂 Estructura del Repositorio
- [`docs/`](docs/) → Documentación del proyecto  
- [`src/`](src/) → Código fuente del sistema  
- [`informes/`](informes/) → Informes académicos (Word/PDF)  
- [`resultados/`](resultados/) → Evidencias y capturas del sistema  
- [`pom.xml`](pom.xml) → Configuración de dependencias del proyecto  

---

## Palabras Clave
Vigilancia, Reconocimiento Facial, Biometría de Marcha, Spring Boot, Seguridad, Inteligencia Artificial
