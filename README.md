# InterCommerceApp

Aplicación Android de comercio electrónico orientada a la demostración de buenas prácticas, consumo de APIs y manejo robusto de persistencia local mediante una estrategia Offline-First.

## 🏗️ Arquitectura y Decisiones Técnicas

El proyecto está construido bajo un enfoque **Offline-First** utilizando una arquitectura **multimódulo**, siguiendo de manera estricta los principios de **Clean Architecture** y el patrón **MVVM** con un estado de UI reactivo y unidireccional apoyado en **Kotlin Coroutines** y **Flow**. La interfaz gráfica es 100% declarativa mediante **Jetpack Compose**.

### Estrategia de Modularización
Para garantizar un desacoplamiento efectivo, facilitar el trabajo en paralelo y optimizar el rendimiento de Gradle, el proyecto se divide en módulos con responsabilidades aisladas:
* **Módulo de Aplicación (`:app`):** Actúa como el orquestador global. Configura la inicialización de la app, la inyección de dependencias principal y el grafo de navegación.
* **Módulos de Características (`:feature:X`):** Módulos independientes y aislados por funcionalidad (e.g., `:feature:catalog`, `:feature:cart`). Cada uno encapsula su propia lógica de presentación, ViewModels y componentes de UI en Compose, comunicándose mediante interfaces de navegación abstractas.
* **Módulos Centrales (`:core:X`):** Contienen las utilidades y componentes compartidos por toda la aplicación, divididos en librerías reutilizables como red (`:core:network`), base de datos local (`:core:database`) y sistema de diseño común (`:core:designsystem`).

### Justificación de la Herramienta de Persistencia (Room)
Se seleccionó **Room (SQLite)** como motor de base de datos local por las siguientes razones:
1. **Single Source of Truth (SSOT):** Room se integra nativamente con `Flow`, permitiendo que la UI reaccione automáticamente a los cambios en la base de datos de forma asíncrona.
2. **Seguridad en tipos e integración:** A diferencia de SQLite puro, Room provee validación de consultas en tiempo de compilación y se integra perfectamente con el ecosistema de Jetpack.
3. **Escalabilidad:** Permite un manejo de esquemas y migraciones controlado, ideal para proyectos que tienden a crecer.

### Estrategia de Mitigación de Pérdida de Datos (Offline-First)
Para garantizar la resiliencia de los datos frente a fallos de red o cierres inesperados de la app:
* La aplicación no renderiza datos directamente desde la capa de red. En su lugar, el `ApiService` descarga la información y el `Repository` la inserta/actualiza en Room.
* La capa de presentación (`ViewModel`) observa de forma exclusiva los `Flows` emitidos por Room. 
* Si la conexión a internet falla, el usuario continuará visualizando el último estado válido almacenado localmente. La búsqueda de productos y la navegación del catálogo funcionarán de manera ininterrumpida con los datos cacheados en memoria/disco.

## 🚀 Instrucciones de Ejecución

### Requisitos Previos
* Android Studio (Iguana o superior recomendado).
* JDK 17.
* Un dispositivo físico o emulador con Android 8.0 (API 26) o superior.

### Correr la Aplicación
1. Clona este repositorio:
   `git clone https://github.com/pipearcos221/InterCommerceApp.git`
2. Abre el proyecto en Android Studio.
3. Permite que Gradle sincronice las dependencias.
4. Selecciona un dispositivo y presiona el botón **Run** (o utiliza el comando `./gradlew assembleDebug` seguido de la instalación manual del APK).

### Ejecutar la Suite de Pruebas
El proyecto incluye pruebas unitarias para asegurar la integridad de la lógica de negocio. Para ejecutarlas:
Desde la terminal en la raíz del proyecto, ejecuta:
```bash
./gradlew test
```
Los resultados de las pruebas se generarán en la carpeta app/build/reports/tests/.

## 📌 Supuestos Técnicos y Limitaciones

* Manejo de Errores Visual: Los errores de red se manejan silenciosamente si existen datos en caché (Offline-First). Las pantallas de estado vacío (Empty States) se priorizaron para búsquedas sin resultados.

* Alcance del Carrito: La lógica del carrito de compras es efímera/local para fines de esta demostración y podría requerir sincronización bidireccional con un backend en un escenario de producción completo.
