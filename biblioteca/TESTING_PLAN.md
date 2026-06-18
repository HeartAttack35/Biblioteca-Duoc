# Plan de Pruebas Unitarias y Cobertura

## 1. Reglas de Negocio Críticas del Sistema
Para garantizar la integridad del sistema de Reserva de Salas de la Biblioteca, las pruebas unitarias se enfocan en las siguientes reglas:

1. **Búsqueda y Disponibilidad de Salas:** El sistema debe ser capaz de listar todas las salas y encontrar salas específicas por su código. Si una sala no existe, debe manejarse adecuadamente devolviendo un valor vacío (Null/Optional.empty).
2. **Registro de Carreras y Estudiantes:** Todo estudiante debe estar asociado a una carrera válida, y el sistema debe permitir registrar y recuperar correctamente el catálogo de carreras.
3. **Gestión de Reservas (Transaccional):** Una reserva debe enlazar correctamente a un estudiante existente y una sala existente, inicializando su estado por defecto (ej. Pendiente/Activa).

## 2. Cobertura Actual de Pruebas Unitarias

A continuación, se detalla el estado de nuestras pruebas automatizadas implementadas con JUnit 5 y Mockito:

| Regla | Estado | Casos Cubiertos | Checklist |
| :--- | :--- | :--- | :--- |
| 1. Búsqueda de Salas | Cubierta | Búsqueda exitosa por código. Búsqueda fallida (no encontrada). | ✅ Caso feliz. ✅ Caso de error/vacío. |
| 2. Registro de Carreras | Cubierta | Obtener lista completa de carreras. Guardado de una nueva carrera. | ✅ Caso feliz (Listar). ✅ Caso feliz (Guardar). |
| 3. Gestión de Reservas | Parcial | Listado general de reservas. Creación de una reserva básica. | ✅ Caso feliz. ❌ Caso de error (estudiante/sala inválida). |

## 3. Reflexión y Deuda Técnica

* **Riesgo sin probar:** Actualmente, la *Gestión de Reservas* asume que el estudiante y la sala enviados siempre existen. No tenemos un test que verifique qué ocurre si se intenta hacer una reserva con un `estudiante_id` que no existe en la base de datos.
* **Impacto:** Si se viola esta regla, el sistema podría arrojar excepciones no controladas de base de datos o crear reservas huérfanas, lo que representa un riesgo **Medio/Alto**.
* **Acción Futura:** Agregar pruebas unitarias para los casos de error (Caminos infelices) en `ReservaServiceTest` y agregar validaciones de cruce de horarios para las reservas.