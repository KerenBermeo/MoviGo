import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;

public class MoviGoApp extends JFrame implements ActionListener {

    private JTextField textfield1;
    private JLabel logoLabel, label2, label3, label4;
    private JButton boton1;
    private JPanel panelContenido, panelVideo;
   
    // Método para crear el JLabel con el logo
    private JLabel createLogoLabel(String imagePath, int width, int height) {
        // Crear el ImageIcon desde el archivo
        ImageIcon originalIcon = new ImageIcon(imagePath);

        // Redimensionar la imagen
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Crear un JLabel con el icono redimensionado
        JLabel logoLabel = new JLabel(scaledIcon);
        return logoLabel;
    }

    public MoviGoApp() {
        setTitle("Bienvenido a MoviGo");
        getContentPane().setBackground(new Color(0, 51, 102)); // Color de fondo
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Crear los paneles para el contenido y el video
        panelContenido = new JPanel();
        panelContenido.setLayout(new GridBagLayout());
        panelContenido.setBackground(new Color(0, 51, 102));
        
        // **Inicialización de panelVideo**
        panelVideo = new JPanel();
        panelVideo.setLayout(new BorderLayout()); // Establecer BorderLayout para el panel de video
        panelVideo.setBackground(new Color(0, 0, 0)); // Azul oscuro
	
     

        // Contenido de la aplicación en el panel izquierdo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Aquí se agrega el logo como JLabel con imagen
        String logoPath = "logo_MoviGo.png"; // Ruta de tu archivo de imagen
        JLabel logoLabel = createLogoLabel(logoPath, 150, 150); // Tamaño deseado del logo
        panelContenido.add(logoLabel, gbc);

        label2 = new JLabel("Sistema de Transporte - MoviGo", SwingConstants.CENTER);
        label2.setFont(new Font("Andale Mono", Font.BOLD, 18));
        label2.setForeground(new Color(163, 217, 0));
        gbc.gridy = 1;
        panelContenido.add(label2, gbc);

        label3 = new JLabel("Ingrese su nombre:", SwingConstants.CENTER);
        label3.setFont(new Font("Andale Mono", Font.PLAIN, 12));
        label3.setForeground(new Color(255, 255, 255));
        gbc.gridy = 2;
        panelContenido.add(label3, gbc);

        textfield1 = new JTextField(15);
        textfield1.setBackground(new Color(255, 255, 255));
        textfield1.setFont(new Font("Andale Mono", Font.PLAIN, 14));
        textfield1.setForeground(new Color(0, 51, 102));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelContenido.add(textfield1, gbc);

        boton1 = new JButton("Ingresar");
        boton1.setBackground(new Color(57, 255, 20)); // Verde fosforescente
        boton1.setFont(new Font("Andale Mono", Font.PLAIN, 14));
        boton1.setForeground(new Color(0, 51, 102)); // azul oscuro
        boton1.addActionListener(this);
        gbc.gridy = 4;
        panelContenido.add(boton1, gbc);

        label4 = new JLabel("@2024 MoviGo Team", SwingConstants.CENTER);
        label4.setFont(new Font("Andale Mono", Font.PLAIN, 12));
        label4.setForeground(new Color(255, 255, 255));
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelContenido.add(label4, gbc);

        // Agregar el contenido a la ventana principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(panelContenido, gbc);
        
        // Configuración de la ventana para mostrar el video en la columna 1
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 6; // Tomar toda la altura
        add(panelVideo, gbc);

        // Reproductor de video
        JFXPanel jfxPanel = new JFXPanel(); // Crear el panel para integrar JavaFX
        panelVideo.add(jfxPanel, BorderLayout.CENTER);
        
        initializeMediaPlayer(jfxPanel);

        // Configuración de la ventana
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeMediaPlayer(JFXPanel jfxPanel) {
        // Cargar el video
        String mediaPath = "file:/" + new java.io.File("video/Elevator_pitch_MoviGo.mp4").getAbsolutePath().replace("\\", "/");

        Media media = new Media(mediaPath); 
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Reducir el tamaño del video
        mediaView.setFitWidth(145);  // ancho 
        mediaView.setPreserveRatio(true); // Mantener la relación de aspecto original del video

        // Crear el grupo y agregar el MediaView
        Group root = new Group(mediaView); 
        Scene scene = new Scene(root);

        // Asignar la escena al JFXPanel
        jfxPanel.setScene(scene);

        // Reproducir automáticamente el video
        mediaPlayer.setAutoPlay(false);

        // Crear controles (Play y Pause)
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.setBackground(new Color(0, 51, 102));

        JButton playButton = new JButton("Play");
        playButton.setBackground(new Color(57, 255, 20));
        playButton.setForeground(new Color(0, 51, 102));
        playButton.addActionListener(e -> mediaPlayer.play());

        JButton pauseButton = new JButton("Pause");
        pauseButton.setBackground(new Color(255, 69, 0));
        pauseButton.setForeground(new Color(255, 255, 255));
        pauseButton.addActionListener(e -> mediaPlayer.pause());

        // Agregar botones al panel de controles
        controlsPanel.add(playButton);
        controlsPanel.add(pauseButton);

        // Agregar el panel de controles debajo del video
        panelVideo.add(controlsPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton1) {
            String nombre = textfield1.getText();
            JOptionPane.showMessageDialog(this, "¡Bienvenido a MoviGo, " + nombre + "!");

            Licencia.main(new String[]{});
        }
    }

    public static void main(String[] args) {
        new MoviGoApp();
    }
}


