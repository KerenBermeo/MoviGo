import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Principal extends JFrame {
    // Componentes de la interfaz
    private JPanel panelPrincipal, formularioPrestarVehiculo, formularioAlquilarVehiculo, panelFormularios ;
    private JButton btnPrestar, btnAlquilar, btnAyuda, btnAcercaDe, btnPrestarVehiculo;
    private JTextField txtNombre,  txtCorreo, txtCorreo2,  txtModelo, txtMarca, txtPlaca, txtCapacidad, txtUbicacion, txtDestino;
    private JComboBox<String> tipoComboBox;
    private PreparedStatement stmt;
    private ResultSet rs;
    private CardLayout cardLayout;

    String[] tipos = {"bicicleta", "carro", "moto", "monopatín"};

    // Conexión con la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/movi_go";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection obtenerConexion() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
            return connection;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null; 
        }
    }
    // Crear un método para acceder al valor de tipoComboBox
    public String obtenerTipoSeleccionado() {
        return (String) tipoComboBox.getSelectedItem();
    }

    private void prestarVehiculo() {
        // Recoger los datos del formulario
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String tipo = obtenerTipoSeleccionado();
        String modelo = txtModelo.getText();
        String marca = txtMarca.getText();
        String placa = txtPlaca.getText();
        String capacidad = txtCapacidad.getText();

        JOptionPane.showMessageDialog(null, "Datos del vehículo:\n" +
            "Nombre: " + nombre + "\n" +
            "Correo: " + correo + "\n" +
            "Tipo: " + tipo + "\n" +
            "Modelo: " + modelo + "\n" +
            "Marca: " + marca + "\n" +
            "Placa: " + placa + "\n" +
            "Capacidad: " + capacidad
        );


        if (nombre.isEmpty() || correo.isEmpty() || tipo.isEmpty() || modelo.isEmpty() || 
        marca.isEmpty() || placa.isEmpty() || capacidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }
    
        // Conexión con la base de datos para insertar estos datos
        try (Connection conn = obtenerConexion()) {
            // Comenzar la transacción
            conn.setAutoCommit(false);  // Desactivar autocommit para controlar la transacción
        
            try {
                // 1. Insertar el usuario en la tabla "usuario"
                String sqlUsuario = "INSERT INTO usuario (nombre, correo) VALUES (?, ?)";
                try (PreparedStatement pstUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    pstUsuario.setString(1, nombre);
                    pstUsuario.setString(2, correo);
                    pstUsuario.executeUpdate();
                    
                    // Obtener el ID del usuario insertado
                    ResultSet rs = pstUsuario.getGeneratedKeys();
                    int usuarioId = 0;
                    if (rs.next()) {
                        usuarioId = rs.getInt(1);  // Obtener el ID generado para el usuario
                    }
        
                    // 2. Insertar el vehículo en la tabla "vehiculo" utilizando el id del usuario
                    String sqlVehiculo = "INSERT INTO vehiculo (tipo, modelo, marca, placa, capacidad, usuario_id) "
                                       + "VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstVehiculo = conn.prepareStatement(sqlVehiculo)) {
                        pstVehiculo.setString(1, tipo);
                        pstVehiculo.setString(2, modelo);
                        pstVehiculo.setString(3, marca);
                        pstVehiculo.setString(4, placa);
                        pstVehiculo.setString(5, capacidad);
                        pstVehiculo.setInt(6, usuarioId);  // Asociar el vehículo al usuario insertado
                        
                        // Ejecutar la inserción del vehículo
                        pstVehiculo.executeUpdate();
        
                        // Confirmar la transacción
                        conn.commit();
                        
                        // Mensaje de éxito
                        JOptionPane.showMessageDialog(this, "Vehículo registrado correctamente.");
                    }
                } catch (SQLException e) {
                    conn.rollback();  // Si hay un error, revertir la transacción
                    JOptionPane.showMessageDialog(this, "Error al registrar el vehículo: " + e.getMessage());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);  // Volver a activar el autocommit
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener la conexión: " + e.getMessage());
        }
        
    }

    

    public void registrarNuevoUsuario() {
        String nombre = JOptionPane.showInputDialog("¡Bienvenido! Ingresa tu nombre:");
        String correo = JOptionPane.showInputDialog("¡Bienvenido! Ingresa tu correo:");
    
        // Conexión con la base de datos para insertar estos datos
        try (Connection conn = obtenerConexion()) {
            String query = "INSERT INTO usuario (nombre, correo) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombre);
                stmt.setString(2, correo);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!");
                    alquilar();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar al usuario.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener la conexión: " + e.getMessage());
        }
    }

    private void verificarUsuario(String correo) {

        // Conexión con la base de datos para insertar estos datos
        try (Connection conn = obtenerConexion()){
            String query = "SELECT * FROM usuario WHERE correo = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, correo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Si el correo existe, saludar y mostrar opciones de vehículos
                String nombreUsuario = rs.getString("nombre");
                JOptionPane.showMessageDialog(this, "Bienvenid@ "+ nombreUsuario);
                alquilar();
            } else {
                registrarNuevoUsuario();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener la conexión: " + e.getMessage());
        }

    }

    private void alquilar() {
        // Limpiar el contenido del panel para evitar duplicados
        formularioAlquilarVehiculo.removeAll();
    
        // Configuración del layout principal del formulario
        formularioAlquilarVehiculo.setLayout(new BorderLayout(10, 10));
    
        // Etiqueta principal en la parte superior
        JLabel titulo = new JLabel("Selecciona el vehículo que deseas alquilar:");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);
        formularioAlquilarVehiculo.add(titulo, BorderLayout.NORTH);
    
        // Panel central para opciones de vehículos y campos de texto
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(new Color(24, 36, 51));
    
        // Configuración del panel de opciones de vehículos con imágenes
        JPanel panelOpciones = new JPanel(new GridLayout(1, tipos.length, 10, 10));
        panelOpciones.setBackground(new Color(24, 36, 51));
        panelOpciones.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), 
            "Opciones de Vehículos", 0, 0, null, Color.WHITE));
    
        ButtonGroup grupoVehiculos = new ButtonGroup();
        JRadioButton[] botonesVehiculos = new JRadioButton[tipos.length];
    
        // Array de rutas de las imágenes para cada vehículo
        String[] imagenesVehiculos = {
            "imagenes/bici.png",  // Imagen para el primer vehículo
            "imagenes/carro.png",  // Imagen para el segundo vehículo
            "imagenes/moto.png",  // Imagen para el tercer vehículo
            "imagenes/monopatin.png"   // Imagen para el cuarto vehículo
        };
    
        // Redimensionar las imágenes para que todas tengan el mismo tamaño
        int anchoImagen = 100; // Ancho fijo para todas las imágenes
        int altoImagen = 100;  // Alto fijo para todas las imágenes
    
        for (int i = 0; i < tipos.length; i++) {
            // Cargar la imagen correspondiente a cada vehículo
            ImageIcon iconoOriginal = new ImageIcon(imagenesVehiculos[i]);
            Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(anchoImagen, altoImagen, Image.SCALE_SMOOTH);
            ImageIcon icono = new ImageIcon(imagenRedimensionada);
    
            // Crear el botón con la imagen
            JRadioButton radioButton = new JRadioButton(tipos[i], icono);
            radioButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            radioButton.setHorizontalTextPosition(SwingConstants.CENTER);
            radioButton.setForeground(Color.WHITE);
            radioButton.setBackground(new Color(24, 36, 51));
            grupoVehiculos.add(radioButton);
            panelOpciones.add(radioButton);
            botonesVehiculos[i] = radioButton;
        }
    
        panelCentral.add(panelOpciones, BorderLayout.NORTH);
    
        // Configuración de los campos de texto
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBackground(new Color(24, 36, 51));
        panelCampos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), 
            "Información de Alquiler", 0, 0, null, Color.WHITE));
    
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setForeground(Color.WHITE);
        txtUbicacion = new JTextField(10); 
        txtUbicacion.setPreferredSize(new Dimension(150, 25)); 
        txtUbicacion.setFont(new Font("Arial", Font.PLAIN, 12));
    
        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setForeground(Color.WHITE);
        txtDestino = new JTextField(10); 
        txtDestino.setPreferredSize(new Dimension(150, 25)); 
        txtDestino.setFont(new Font("Arial", Font.PLAIN, 12));
    
        panelCampos.add(lblUbicacion);
        panelCampos.add(txtUbicacion);
        panelCampos.add(lblDestino);
        panelCampos.add(txtDestino);
    
        panelCentral.add(panelCampos, BorderLayout.CENTER);
    
        formularioAlquilarVehiculo.add(panelCentral, BorderLayout.CENTER);
    
        // Configuración del botón Confirmar
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(24, 36, 51));
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBackground(new Color(0, 123, 255));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar.addActionListener(e -> {
            String vehiculoSeleccionado = null;
            for (JRadioButton boton : botonesVehiculos) {
                if (boton.isSelected()) {
                    vehiculoSeleccionado = boton.getText();
                    break;
                }
            }
    
            if (vehiculoSeleccionado == null || txtUbicacion.getText().trim().isEmpty() || txtDestino.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(formularioAlquilarVehiculo, 
                    "Por favor, completa todos los campos y selecciona un vehículo.");
            } else {
                JOptionPane.showMessageDialog(formularioAlquilarVehiculo, 
                    "Vehículo: " + vehiculoSeleccionado + " alquilado desde " + txtUbicacion.getText() + " hasta " + txtDestino.getText() + ".");
            }
        });
    
        panelBoton.add(btnConfirmar);
        formularioAlquilarVehiculo.add(panelBoton, BorderLayout.SOUTH);
    
        // Mostrar el formulario en el CardLayout
        cardLayout.show(panelFormularios, "Alquilar");
    
        // Actualizar el formulario después de modificarlo
        formularioAlquilarVehiculo.revalidate();
        formularioAlquilarVehiculo.repaint();
    }
    
    
    public Principal() {
        // Configuración de la ventana
        setTitle("Gestión de Vehículos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Cambiar el icono de la ventana
        ImageIcon icon = new ImageIcon("imagenes/logo_MoviGo.png");
        setIconImage(icon.getImage());

        // Establecer fondo azul oscuro
        getContentPane().setBackground(new Color(18, 32, 47));

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();

        menuBar.setBackground(new Color(255, 69, 0));
        
        // Menú de "Ayuda"
        JMenu menuAyuda = new JMenu("Ayuda");
        btnAyuda = new JButton("Ayuda");
        btnAyuda.setBackground(new Color(255, 69, 0)); // Naranja
        btnAyuda.setForeground(Color.WHITE);
        btnAyuda.setBorderPainted(false);
        btnAyuda.setFocusPainted(false);
        btnAyuda.setFont(new Font("Arial", Font.BOLD, 12));
        btnAyuda.addActionListener(e -> mostrarMensaje("Este es el sistema de gestión de vehículos."));
        menuAyuda.add(btnAyuda);
        
        // Menú "Acerca de"
        JMenu menuAcercaDe = new JMenu("Acerca de");
        btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.setBackground(new Color(255, 69, 0)); // Naranja
        btnAcercaDe.setForeground(Color.WHITE);
        btnAcercaDe.setBorderPainted(false);
        btnAcercaDe.setFocusPainted(false);
        btnAcercaDe.setFont(new Font("Arial", Font.BOLD, 12));
        btnAcercaDe.addActionListener(e -> mostrarMensaje("Sistema creado por [Tu Nombre]."));
        menuAcercaDe.add(btnAcercaDe);

        menuBar.add(menuAyuda);
        menuBar.add(menuAcercaDe);
        setJMenuBar(menuBar);

        // Crear panel principal
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(18, 32, 47));

        // Crear botones de "Prestar Vehículo" y "Alquilar Vehículo"
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        btnPrestar = new JButton("Prestar Vehículo");
        btnPrestar.setBackground(new Color(255, 69, 0)); // Naranja
        btnPrestar.setForeground(Color.WHITE);
        btnPrestar.setFont(new Font("Arial", Font.BOLD, 14));
        btnPrestar.setFocusPainted(false);
        btnPrestar.setBorderPainted(false);
        btnPrestar.addActionListener(e -> {
            formularioAlquilarVehiculo.setVisible(false);  // Ocultar formulario de alquiler
            formularioPrestarVehiculo.setVisible(true);  // Mostrar formulario de préstamo
        });
        
        btnAlquilar = new JButton("Alquilar Vehículo");
        btnAlquilar.setBackground(new Color(255, 69, 0)); // Naranja
        btnAlquilar.setForeground(Color.WHITE);
        btnAlquilar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAlquilar.setFocusPainted(false);
        btnAlquilar.setBorderPainted(false);
        btnAlquilar.addActionListener(e -> {
            formularioPrestarVehiculo.setVisible(false);  // Ocultar formulario de préstamo
            formularioAlquilarVehiculo.setVisible(true);  // Mostrar formulario de alquiler
        });
        
        panelBotones.add(btnPrestar);
        panelBotones.add(btnAlquilar);

        // Crear CardLayout y panel para los formularios
        cardLayout = new CardLayout();
        panelFormularios = new JPanel(cardLayout);

        // Panel para los formularios
        formularioPrestarVehiculo = new JPanel();
        formularioPrestarVehiculo.setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar
        formularioPrestarVehiculo.setBackground(new Color(24, 36, 51)); // Fondo más suave
        formularioPrestarVehiculo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Bordes para que se vea más espacioso

        // Inicialización de formularioAlquilarVehiculo
        formularioAlquilarVehiculo = new JPanel();
        formularioAlquilarVehiculo.setLayout(new GridBagLayout());
        formularioAlquilarVehiculo.setBackground(new Color(24, 36, 51)); 
        formularioAlquilarVehiculo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // Añadir formularios al CardLayout
        panelFormularios.add(formularioPrestarVehiculo, "Prestar");
        panelFormularios.add(formularioAlquilarVehiculo, "Alquilar");

        // Establecer la visibilidad inicial
        cardLayout.show(panelFormularios, "Prestar");

        // Añadir panel de formularios al panel principal
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(panelFormularios, BorderLayout.CENTER);

        // Añadir el panel principal a la ventana
        add(panelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre los componentes

        // Configuramos las filas de los formularios y etiquetas
        JLabel labelNombre = new JLabel("Nombre del Usuario:");
        labelNombre.setForeground(Color.WHITE); // Cambié el color de los Labels a blanco
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPrestarVehiculo.add(labelNombre, gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtNombre, gbc);

        JLabel labelCorreo = new JLabel("Correo del Usuario:");
        labelCorreo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formularioPrestarVehiculo.add(labelCorreo, gbc);

        txtCorreo = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtCorreo, gbc);

        // Crear el JComboBox con las opciones de vehículos
        tipoComboBox = new JComboBox<>(tipos);

        JLabel labelTipo = new JLabel("Selecciona el tipo de vehículo:");
        labelTipo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioPrestarVehiculo.add(labelTipo, gbc);

        // Añadir el JComboBox al formulario
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(tipoComboBox, gbc);


        JLabel labelModelo = new JLabel("Modelo del Vehículo:");
        labelModelo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formularioPrestarVehiculo.add(labelModelo, gbc);

        txtModelo = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtModelo, gbc);

        JLabel labelMarca = new JLabel("Marca del Vehículo:");
        labelMarca.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formularioPrestarVehiculo.add(labelMarca, gbc);

        txtMarca = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtMarca, gbc);

        JLabel labelPlaca = new JLabel("Placa del Vehículo:");
        labelPlaca.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formularioPrestarVehiculo.add(labelPlaca, gbc);

        txtPlaca = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtPlaca, gbc);

        JLabel labelCapacidad = new JLabel("Capacidad del Vehículo:");
        labelCapacidad.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formularioPrestarVehiculo.add(labelCapacidad, gbc);

        txtCapacidad = new JTextField(20);
        gbc.gridx = 1;
        formularioPrestarVehiculo.add(txtCapacidad, gbc);

        // Botón para prestar el vehículo
        btnPrestarVehiculo = new JButton("Prestar Vehículo");
        btnPrestarVehiculo.setBackground(new Color(255, 69, 0)); // Naranja
        btnPrestarVehiculo.setForeground(Color.WHITE);
        btnPrestarVehiculo.setFont(new Font("Arial", Font.BOLD, 14));
        btnPrestarVehiculo.setFocusPainted(false);
        btnPrestarVehiculo.setBorderPainted(false);
        btnPrestarVehiculo.addActionListener(e -> prestarVehiculo());
        gbc.gridx = 1;
        gbc.gridy = 7;
        formularioPrestarVehiculo.add(btnPrestarVehiculo, gbc);

        formularioPrestarVehiculo.setVisible(false);

        // __________________________ Formulario Alquilar ______________________________
        JLabel labelCorreo2 = new JLabel("Correo del Usuario:");
        labelCorreo2.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioAlquilarVehiculo.add(labelCorreo2, gbc);

        // Crear el JTextField para ingresar el correo
        txtCorreo2 = new JTextField(20);
        gbc.gridx = 1;
        formularioAlquilarVehiculo.add(txtCorreo2, gbc);

        // declaro la variable btnLogin
        JButton btnLogin = new JButton("Login");

        // Botón de Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el correo ingresado
                String correoIngresado = txtCorreo2.getText();
                
                // Verificar si el usuario está registrado
                verificarUsuario(correoIngresado);
            }
        });

        gbc.gridx = 0; 
        gbc.gridy = 1; 
        formularioAlquilarVehiculo.add(btnLogin, gbc);


        // __________________________ FIN  Formulario Alquilar ______________________________

    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public static void main(String[] args) {
        if (obtenerConexion() != null) {
            System.out.println("Conexión establecida");
        }
        SwingUtilities.invokeLater(() -> {
            Principal principal = new Principal();
            principal.setVisible(true);
        });
    }
}

