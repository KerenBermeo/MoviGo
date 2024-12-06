import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Licencia extends JFrame implements ActionListener, ChangeListener {
    private JTextArea textarea1;
    private JLabel label1;
    private JCheckBox check1;
    private JButton boton1, boton2;
    private JScrollPane scrollpanel1;

    public Licencia() {
        // Establecer el layout como GridBagLayout para centrar los componentes
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setTitle("Licencia de Uso");
        setIconImage(new ImageIcon(getClass().getResource("imagenes/logo_MoviGo.png")).getImage());

        // Establecer el color de fondo de la ventana (JFrame)
        getContentPane().setBackground(new Color(0, 51, 102)); // Azul oscuro

        // Label for title
        label1 = new JLabel("TÉRMINOS Y CONDICIONES");
        label1.setFont(new Font("Andale Mono", Font.BOLD, 14));
        label1.setForeground(Color.WHITE); // Color blanco para mejor contraste
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0); // Espaciado alrededor del título
        add(label1, gbc);

        // TextArea for terms
        textarea1 = new JTextArea();
        textarea1.setEditable(false);
        textarea1.setFont(new Font("Andale Mono", Font.PLAIN, 12)); // Aumentar tamaño de fuente
        textarea1.setText("\n\n          TÉRMINOS Y CONDICIONES" +
                "\n\n            A.  PROHIBIDA SU VENTA O DISTRIBUCIÓN SIN AUTORIZACIÓN DE LA EMPRESA MOVIGO." +
                "\n            B.  PROHIBIDA LA ALTERACIÓN DEL CÓDIGO FUENTE O DISEÑO DE LAS INTERFACES GRÁFICAS." +
                "\n            C.  MOVIGO NO SE HACE RESPONSABLE DEL MAL USO DE ESTE SOFTWARE." +
                "\n\n          LOS ACUERDOS LEGALES EXPUESTOS ACONTINUACIÓN RIGEN EL USO QUE USTED HAGA DE ESTE SOFTWARE" +
                "\n          MOVIGO, NO SE RESPONSABILIZAN DEL USO QUE USTED" +
                "\n          HAGA CON ESTE SOFTWARE Y SUS SERVICIOS. PARA ACEPTAR ESTOS TERMINOS HAGA CLIC EN (ACEPTO)" +
                "\n          SI USTED NO ACEPTA ESTOS TERMINOS, HAGA CLIC EN (NO ACEPTO) Y NO UTILICE ESTE SOFTWARE." +
                "\n\n          PARA MAYOR INFORMACIÓN SOBRE NUESTROS PRODUCTOS O SERVICIOS, POR FAVOR VISITE" +
                "\n          http://www.MoviGoApp.com");

        // ScrollPane for JTextArea
        scrollpanel1 = new JScrollPane(textarea1);
        scrollpanel1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpanel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Esto hace que el JTextArea se ajuste automáticamente al espacio disponible
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 20, 20); // Espaciado alrededor del área de texto
        add(scrollpanel1, gbc);

        // CheckBox for acceptance
        check1 = new JCheckBox("Yo Acepto");
        check1.setForeground(Color.BLACK); // Color negro para el texto
        check1.addChangeListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 10, 10); // Ajustar espacio alrededor del checkbox
        add(check1, gbc);

        // Botones
        boton1 = new JButton("Continuar");
        boton1.setBackground(new Color(0, 51, 102)); // Azul oscuro
        boton1.setForeground(Color.WHITE); // Blanco
        boton1.setFont(new Font("Arial", Font.BOLD, 16));
        boton1.setPreferredSize(new Dimension(150, 50));
        boton1.setEnabled(false);
        boton1.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 20, 20); // Ajustar espacio para el botón "Continuar"
        add(boton1, gbc);

        boton2 = new JButton("No aceptar");
        boton2.setBackground(new Color(255, 51, 51)); // Rojo para "No aceptar"
        boton2.setForeground(Color.WHITE); // Blanco
        boton2.setFont(new Font("Arial", Font.BOLD, 16));
        boton2.setPreferredSize(new Dimension(150, 50));
        boton2.addActionListener(this);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 20, 20); // Ajustar espacio para el botón "No aceptar"
        add(boton2, gbc);

        // Configuración final de la ventana
        setSize(800, 600); // Ajuste el tamaño de la ventana para una vista más apropiada
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton1) {
            JOptionPane.showMessageDialog(this, "Gracias por aceptar los términos.");
            Principal.main(new String[]{});
        } else if (e.getSource() == boton2) {
            JOptionPane.showMessageDialog(this, "No has aceptado los términos. Cerrando la aplicación.");
            System.exit(0);
        }
    }

    public void stateChanged(ChangeEvent e) {
        // Habilitar o deshabilitar el botón "Continuar" según el estado del CheckBox
        boton1.setEnabled(check1.isSelected());
    }

    public static void main(String[] args) {
        Licencia licencia = new Licencia();
        licencia.setVisible(true);
    }
}
