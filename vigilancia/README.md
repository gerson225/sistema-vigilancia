# Sistema de Vigilancia Inteligente

## Información General
- **Estudiantes:** Gerson David Ochoa Alarcón, Erick Martines
- **Docente:** Milton Freddy Amache Sanchez 
- **Universidad:** UTP  
- **Fecha de inicio:** Septiembre 2025  
- **Fecha de finalización:** septiembre/2025 

## Agradecimiento
Agradezemos a nuestro docente por la guía brindada en el desarrollo de este proyecto y compañeros por su apoyo constante.

## Dedicatoria
Este proyecto está dedicado a nuestro aprendizaje para seguir mejorando como ingenieros de sistemas.

## Resumen
Este proyecto propone el desarrollo de un sistema de vigilancia inteligente que permita la detección y comparación de personas mediante el reconocimiento facial y la biometría de la marcha. Se busca sentar las bases de un prototipo funcional desarrollado en **Spring Boot**, que permita registrar personas y almacenar sus datos de manera organizada, como parte de una solución innovadora en el ámbito de la seguridad.

## Abstract
This project proposes the development of an intelligent surveillance system that allows the detection and comparison of individuals through facial recognition and gait biometrics. The objective is to establish the foundation of a functional prototype developed in **Spring Boot**, capable of registering people and storing their data in an organized manner, as part of an innovative solution in the field of security.

## Realidad Problemática
La inseguridad ciudadana representa un problema creciente en distintos contextos. Los sistemas de vigilancia tradicionales presentan limitaciones, ya que dependen exclusivamente de la visibilidad del rostro para identificar personas. En situaciones donde el rostro está cubierto o no es visible, la efectividad de dichos sistemas disminuye considerablemente. Esto plantea la necesidad de soluciones que integren nuevas técnicas de identificación complementarias.

## Justificación
La seguridad es una necesidad crucial en la sociedad actual. Sin embargo, los sistemas de vigilancia tradicionales presentan limitaciones, ya que se enfocan única y esencialmente en el reconocimiento facial. Este proyecto propone un enfoque innovador al considerar también la biometría de la marcha, lo que permite aumentar la precisión en la identificación de personas y mejorar la seguridad en espacios públicos y privados.

## Objetivo General
Desarrollar un prototipo de sistema de vigilancia inteligente basado en **Spring Boot** que permita registrar y gestionar información de personas detectadas mediante reconocimiento facial y forma de caminar.

## Objetivos Específicos
1. Analizar las técnicas de reconocimiento facial y de marcha aplicables en un sistema de vigilancia.  
2. Implementar un backend en Spring Boot que gestione el almacenamiento de datos de personas.  
3. Desarrollar un módulo inicial que permita registrar personas y sus rostros en la base de datos.  
4. Proponer la integración de algoritmos de detección de marcha para fases futuras del proyecto.  

## Marco Teórico
- **Vigilancia Inteligente:** Sistemas que van más allá de la simple grabación de video, integrando análisis y reconocimiento.  
- **Reconocimiento Facial:** Técnica biométrica ampliamente usada en seguridad y control de accesos.  
- **Biometría de la Marcha:** Método de identificación basado en la forma de caminar.  
- **Spring Boot:** Framework de Java que facilita la creación de aplicaciones web y APIs REST.  

## Metodología
El proyecto se desarrollará bajo un enfoque de **investigación aplicada** y una metodología **experimental**:  
- Revisión bibliográfica sobre biometría de marcha y reconocimiento facial.  
- Diseño e implementación de un backend en Spring Boot.  
- Creación de una base de datos relacional para almacenar registros.  
- Pruebas iniciales de registro y consulta de personas.  

## Cronograma de Actividades
| Semana | Actividad |
|--------|-----------|
| 1 | Revisión bibliográfica y estado del arte |
| 2 | Configuración del proyecto en Spring Boot y GitHub |
| 3 | Diseño de la base de datos y entidades |
| 4 | Implementación del módulo de registro de personas |
| 5 | Pruebas iniciales y depuración |
| 6 | Documentación de resultados |
| 7 | Presentación del avance parcial |

## Desarrollo del Proyecto
### Aplicación
El sistema está siendo desarrollado en **Spring Boot** y se estructura como un backend que permitirá:  
- Registrar personas con sus datos y rostros.  
- Consultar información almacenada.  
- Preparar la integración de algoritmos de biometría de marcha.  

### Base de Datos
Se utilizará **MySQL** (o H2 en modo desarrollo) para gestionar la información.  
Entidad inicial:  
- `Persona(id, nombre, foto)`  

## Resultados Esperados
- Un prototipo básico en Spring Boot para registrar personas y almacenar rostros.  
- Documentación del proceso en GitHub siguiendo el formato académico.  
- Bases para integrar biometría de marcha en el futuro.  


## Palabras Clave
Vigilancia, Reconocimiento facial, Biometría de marcha, Spring Boot, Seguridad
