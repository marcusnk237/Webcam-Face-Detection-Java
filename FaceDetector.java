/* Importation de openCV et de swing */
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class FaceDetector {

  public static void main(String arg[]) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    /* Cr�ation du GUI */
    String window_name = "D�tection de visage sur Java";
    JFrame frame = new JFrame(window_name);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /* Taille de la fen�tre du GUI */
    frame.setSize(600, 600); 
    /* Instanciation d'un nouveau d�tecteur et d'un nouveau panel */
    FaceProcessor processor = new FaceProcessor();
    MyPanel panel = new MyPanel();
    frame.setContentPane(panel);
    frame.setVisible(true);
    /* Initialisation de la webcam */
    Mat webcamPhoto = new Mat();
    VideoCapture capture = new VideoCapture(0);
    /* D�marrage de la webcam et d�tection des param�tres (visage(s),yeux,bouche(s))*/
    if (capture.isOpened()) {
      /* La webcam est active */
      while (true) {
        capture.read(webcamPhoto);
        /* La webcam r�cup�re bien les images */
        if (!webcamPhoto.empty()) {
          /* Redimensionnement */
          frame.setSize(webcamPhoto.width() + 40, webcamPhoto.height() + 60);
          /* D�tection */
          webcamPhoto = processor.detect(webcamPhoto);
          /* Incrustration sur l'image en temps r�el */
          panel.MatToBufferedImage(webcamPhoto);
          panel.repaint();
        } 
        /* La webcam n'a pas pu r�cup�r� les images */
        else {
          System.out.println("Impossible de r�cup�rer des images ...");
          break;
        }
      }
    }
    return;
  }
}
