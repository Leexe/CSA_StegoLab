import java.awt.Color;

public class Steganography
{
  /**
  * Clear the lower (rightmost) two bits in a pixel
  */
  public static void clearLow( Pixel p )
  {
    p.setBlue(p.getBlue()/4);
    p.setRed(p.getRed()/4);
    p.setGreen(p.getGreen()/4);
  }

  public static Picture testClearLow( Picture picture ) 
  {
    Picture newPicture = new Picture(picture);
    for (int x = 0; x < newPicture.getHeight(); x++) 
    {
      for (int y = 0; y < newPicture.getWidth(); y++) 
      {
        clearLow(newPicture.getPixel(x, y));
      }
    }
      
    return newPicture;
  } 
  
  public static void main(String[] args)
  {
    Picture beach = new Picture ("beach.jpg");
    beach.explore();
    Picture copy = testClearLow(beach);
    copy.explore();
    
  }
}