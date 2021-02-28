/* Importation de openCV */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceProcessor {
  /* Variables pour les d�tecteurs de Haas de visage, d'yeux et de sourire */
  private CascadeClassifier faceCascade;
  private CascadeClassifier eyesCascade;
  private CascadeClassifier smileCascade;
  
  /* Instanciation du d�tecteur */
  public FaceProcessor() {
    /* Initialisation du d�tecteur de visage */
    faceCascade = new CascadeClassifier(
        FaceDetector.class.getResource("haarcascade_frontalface_alt.xml")
            .getPath());
    /* Initialisation du d�tecteur d'yeux */
    eyesCascade = new CascadeClassifier(
        FaceDetector.class.getResource("haarcascade_eye_tree_eyeglasses.xml")
            .getPath());
    /* Initialisation du d�tecteur de sourire*/        
    smileCascade = new CascadeClassifier(
        FaceDetector.class.getResource("haarcascade_smile.xml").getPath());
    
    
    /* V�rification du chargement effectif des d�tecteurs */
    
    /*d�tecteur de visage */
    if (faceCascade.empty()) {
      System.out.println("Echec du chargement du d�tecteur de visage...");
      return;
    } else {
      System.out.println("Chargement du d�tecteur de visage r�ussi...");
    }
    /*d�tecteur d'yeux */
    if (eyesCascade.empty())  {
        System.out.println("Echec du chargement du d�tecteur d'yeux...");
        return;
      } else {
        System.out.println("Chargement du d�tecteur d'yeux r�ussi...");
      }
    /*d�tecteur de bouche */
    if (smileCascade.empty())  {
        System.out.println("Echec du chargement du d�tecteur de sourire...");
        return;
      } else {
        System.out.println("Chargement du d�tecteur de sourire r�ussi...");
      }

  }
  
  /* M�thode permettant la d�tection des param�tres */
  public Mat detect(Mat inputframe) {
	/* Image finale en couleur */
    Mat mRgba = new Mat();
    /* Image en niveau de gris */
    Mat mGrey = new Mat();
    /* Variable qui va contenir les donn�es du 
    MatOfRect faces = new MatOfRect();
    /* Copie de l'image en couleur et de l'image en niveau de gris */
    inputframe.copyTo(mRgba);
    inputframe.copyTo(mGrey);
    /* Conversion en niveau de gris */
    Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
    /* Eglisation d'histogramme */
    Imgproc.equalizeHist(mGrey, mGrey);
    
    /* D�tection des visages */
    faceCascade.detectMultiScale(mGrey, faces);
    System.out
        .println(String.format(" %s Visage(s) d�tect�(s)", faces.toArray().length));
    faceCascade.detectMultiScale(mGrey, faces, 1.1, 5, 0, new Size(30, 30),
        new Size());
    Rect[] facesArray = faces.toArray();

    for (int i = 0; i < facesArray.length; i++) {
      Point centre1 = new Point(facesArray[i].x + facesArray[i].width * 0.5,
          facesArray[i].y + facesArray[i].height * 0.5);
      Core.ellipse(mRgba, centre1,
          new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5), 0, 0,
          360,
          new Scalar(192, 202, 235), 3, 8, 0);
      /* Initialisation de la ROI pour d�tecter les yeux */
      Mat faceROI = mGrey.submat(facesArray[i]);
      MatOfRect eyes = new MatOfRect();
      /* D�tection d'yeux */
      eyesCascade.detectMultiScale(faceROI, eyes, 1.1, 5, 0, new Size(30, 30),
          new Size());
      /* Initialisation de la ROI pour les yeux */
      Rect[] eyesArray = eyes.toArray();

      for (int j = 0; j < eyesArray.length; j++) {
        Point centre2 = new Point(
            facesArray[i].x + eyesArray[j].x + eyesArray[j].width * 0.5,
            facesArray[i].y + eyesArray[j].y + eyesArray[j].height * 0.5);
        int radius = (int) Math
            .round((eyesArray[j].width + eyesArray[j].height) * 0.25);
        Core.circle(mRgba, centre2, radius, new Scalar(255, 202, 121), 3, 8, 0);
      }
      /* Initialisation du d�tecteur de sourire */
      MatOfRect smile = new MatOfRect();

      facesArray[i].height = (int) Math.round(facesArray[i].height * 0.5);
      facesArray[i].y = facesArray[i].y + facesArray[i].height;
      /* Circonscription de la ROI pour le sourire */
      faceROI = mGrey.submat(facesArray[i]);
      /* D�tection de sourire */
      smileCascade
          .detectMultiScale(faceROI, smile, 1.1, 80, 0, new Size(30, 30),
              new Size());
      /* ROI pour le sourire */
      Rect[] smileArray = smile.toArray();

      for (int k = 0; k < smileArray.length; k++) {
        Point centre3 = new Point(
            facesArray[i].x + smileArray[k].x + smileArray[k].width * 0.5,
            facesArray[i].y + smileArray[k].y + smileArray[k].height * 0.5);
        Core.ellipse(mRgba, centre3,
            new Size(smileArray[k].width * 0.5, smileArray[k].height * 0.5), 0,
            0, 360,
            new Scalar(177, 138, 255), 3, 8, 0);
      }

    }
    /* Renvoi de la frame finale */
    return mRgba;
  }
}
