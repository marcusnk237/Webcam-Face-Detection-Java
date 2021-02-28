/* Importation de openCV et de swing */
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class FaceDetector {

  public static void main(String arg[]) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    /* Création du GUI */
    String window_name = "Détection de visage sur Java";
    JFrame frame = new JFrame(window_name);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /* Taille de la fenêtre du GUI */
    frame.setSize(600, 600); 
    /* Instanciation d'un nouveau détecteur et d'un nouveau panel */
    FaceProcessor processor = new FaceProcessor();
    MyPanel panel = new MyPanel();
    frame.setContentPane(panel);
    frame.setVisible(true);
    /* Initialisation de la webcam */
    Mat webcamPhoto = new Mat();
    VideoCapture capture = new VideoCapture(0);
    /* Démarrage de la webcam et détection des paramètres (visage(s),yeux,bouche(s))*/
    if (capture.isOpened()) {
      /* La webcam est active */
      while (true) {
        capture.read(webcamPhoto);
        /* La webcam récupère bien les images */
        if (!webcamPhoto.empty()) {
          /* Redimensionnement */
          frame.setSize(webcamPhoto.width() + 40, webcamPhoto.height() + 60);
          /* Détection */
          webcamPhoto = processor.detect(webcamPhoto);
          /* Incrustration sur l'image en temps réel */
          panel.MatToBufferedImage(webcamPhoto);
          panel.repaint();
        } 
        /* La webcam n'a pas pu récupéré les images */
        else {
          System.out.println("Impossible de récupérer des images ...");
          break;
        }
      }
    }
    return;
  }
}
