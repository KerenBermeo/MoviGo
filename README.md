# MoviGo 🚴‍♀️🚗🛵

## 🌍 Propósito de la aplicación

**MoviGo** es una plataforma innovadora que revoluciona la movilidad urbana. Permite a los usuarios encontrar, reservar y gestionar diversas opciones de transporte sostenible, como bicicletas, scooters y autos compartidos, todo desde una sola aplicación.

Lo que hace única a **MoviGo**:
- **Movilidad sostenible**: Promueve el uso de medios de transporte que contribuyen a reducir el tráfico y la contaminación.

Con la capacidad de adaptarse a cualquier ciudad y escalar a nuevas localidades, **MoviGo** tiene el potencial de convertirse en la solución esencial para la vida urbana moderna.

---

## 🛠 Herramientas utilizadas

Este proyecto fue desarrollado utilizando Java, con el objetivo de certificar nuestra competencia en el lenguaje. Las principales herramientas y dependencias son:

- **Java**: Lenguaje principal utilizado para el desarrollo de la aplicación.
- **JavaFX SDK 21.0.5**: Framework para la interfaz gráfica de la aplicación. Asegúrate de descargarlo y configurarlo correctamente.
- **MySQL Connector/J 9.1.0**: Librería para la conexión con bases de datos MySQL.
- **XAMPP**: Herramienta utilizada para gestionar la base de datos local.

---

## 📦 Cómo compilar y empaquetar

### 🧑‍💻 Compilación

Para compilar los archivos Java de la aplicación, ejecuta los siguientes comandos en tu terminal:

1. **Compilar la clase principal** `MoviGoApp.java` con JavaFX:
   
   `javac --module-path javafx-sdk-21.0.5/lib --add-modules javafx.controls,javafx.media,javafx.swing MoviGoApp.java`
   

2. **Compilar la clase `Principal.java`** con el conector MySQL:
   
   `javac -cp ".;mysql-connector-j-9.1.0.jar" Principal.java`
   

3. **Compilar la clase `Licencia.java`**:
   `javac Licencia.java`
   

### 📦 Empaquetado

Una vez compilado el código, empaqueta la aplicación en un archivo `.jar` utilizando el siguiente comando:

`jar cvfm MoviGoApp.jar manifest.mf MoviGoApp.class Licencia.class Principal.class imagenes/logo_MoviGo.png imagenes/bici.png imagenes/carro.png imagenes/moto.png imagenes/monopatin.png video/Elevator_pitch_MoviGo.mp4`


### 🚀 Ejecución

Para ejecutar la aplicación, utiliza el siguiente comando:
`java --module-path javafx-sdk-21.0.5/lib --add-modules javafx.controls,javafx.media,javafx.swing -jar MoviGoApp.jar`


---

## 🗄 Base de datos

Se utilizó **XAMPP** como servidor local para gestionar la base de datos de la aplicación. Esta base de datos almacena y recupera información relevante de los usuarios y las opciones de transporte disponibles.

---

## ⚙️ Notas adicionales

- Asegúrate de tener instalada la **JDK de Java** en tu máquina y que el entorno esté correctamente configurado.
- El archivo `manifest.mf` es necesario para la creación del archivo `.jar` y debe incluir los archivos principales de la aplicación.
- La aplicación está diseñada para facilitar el uso de transporte urbano sostenible, promoviendo la reducción de la contaminación.


