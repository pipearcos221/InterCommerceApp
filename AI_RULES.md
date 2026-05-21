# AI Coding Rules para InterCommerce App

El asistente de IA debe respetar estrictamente las siguientes directrices al generar código para este proyecto:

## 1. Arquitectura y Patrones
- **Clean Architecture Estricta:** Separación inquebrantable en capas (Data, Domain, Presentation).
- **Single Activity Pattern:** El proyecto utiliza una única `MainActivity` que actúa como contenedor exclusivo para el `NavHost` de Jetpack Compose.
- **Principios SOLID:** Priorizar la Inversión de Dependencias (uso de interfaces) y la Responsabilidad Única en cada clase y función.

## 2. Reglas de Dominio y Datos
- **Dominio Puro:** El módulo `:core:domain` es Kotlin puro. Absolutamente prohibido usar dependencias del framework de Android (`android.*` o `androidx.*`).
- **Aislamiento de Lógica:** El cálculo de totales de carrito, impuestos y descuentos reside exclusivamente en `UseCases` dentro de la capa Domain.
- **Offline-First (SSOT):** La UI solo observa `Flows` emitidos por la base de datos local (Room). Nunca recibe datos directamente de las peticiones de red.
- **Persistencia Restringida:** Uso obligatorio de Room. Cero SharedPreferences o DataStore para persistir el catálogo o el volumen transaccional del carrito.

## 3. Stack Tecnológico
- Jetpack Compose para toda la UI.
- Compose Navigation Type-Safe (basado en tipos/objetos serializables).
- Hilt para Inyección de Dependencias.
- Kotlin Coroutines y Flow (`StateFlow` / `SharedFlow`).
- Retrofit para Networking.
- Coil para el manejo de imagenes.

## 4. Calidad, Seguridad y Código Estático
- **Kotlin Coding Conventions:** El código debe estar formateado como si fuera a ser evaluado por Detekt o Ktlint (nombres descriptivos, inmutabilidad con `val`, funciones puras donde sea posible).
- **Null Safety Estricto:** Está absolutamente prohibido el uso del operador de aserción no nula (`!!`). Todo manejo de nulabilidad debe resolverse de forma segura utilizando *Safe Calls* (`?.`), funciones de alcance (`?.let`), el operador Elvis (`?:`) o un mapeo temprano en la capa de datos.
- **Seguridad Básica:** No imprimir datos sensibles (como información de tarjetas, detalles de transacciones o tokens) en el Logcat.

## 5. Prácticas de Seniority y Testabilidad
- **Inyección de Dispatchers:** Está estrictamente prohibido hardcodear `Dispatchers.IO`, `Dispatchers.Main` o `Dispatchers.Default`. Los dispatchers deben inyectarse mediante Hilt o pasarse como dependencias para garantizar pruebas unitarias deterministas.
- **Manejo de Estados (UI State):** La capa de Presentación debe observar un único estado consolidado. Utilizar `sealed interfaces` o `sealed classes` para representar explícitamente los estados de la UI (ej. `Loading`, `Success<T>`, `Error`).
- **Mappers Obligatorios:** Los DTOs de red (Retrofit) y las Entidades de base de datos (Room) NUNCA deben cruzar hacia la capa de Dominio o Presentación. Deben ser transformados a modelos puramente de Dominio (Data Classes) en la capa Data utilizando funciones de extensión de mapeo (`.toDomain()`).
- **Testing BDD (Behavior-Driven Development):** Todas las pruebas unitarias (con JUnit y MockK) deben estructurarse semánticamente usando el formato `Given`, `When`, `Then` (Dado, Cuando, Entonces) a través de comentarios o nombres de variables para garantizar claridad y documentación viva.