import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GlamifyGUI gui = new GlamifyGUI();
            gui.setVisible(true);
        });
    }
}
