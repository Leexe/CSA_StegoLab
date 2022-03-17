import java.awt.Color;

public class Steganography
{
  /**
  * Clear the lower (rightmost) two bits in a pixel
  */
  public static void clearLow( Pixel p )
  {
    p.setBlue(p.getBlue()*4/4);
    p.setRed(p.getRed()*4/4);
    p.setGreen(p.getGreen()*4/4);
  }

  public static Picture testClearLow( Picture picture ) 
  {
    Picture newPicture = new Picture(picture);
    for (int x = 0; x < newPicture.getWidth(); x++) 
    {
      for (int y = 0; y < newPicture.getHeight(); y++) 
      {
        clearLow(newPicture.getPixel(x, y));
      }
    }
      
    return newPicture;
  } 

  /**
  * Set the lower 2 bits in a pixel to the highest 2 bits in c
  */
  public static void setLow(Pixel p, Color c)
  {
    clearLow(p);
    p.setBlue(p.getBlue() + (c.getBlue()/64));
    p.setRed(p.getRed() + (c.getRed()/64));
    p.setGreen(p.getGreen() + (c.getGreen()/64));
  }

  public static Picture testSetLow( Picture picture, Color color ) 
  {
    Picture newPicture = new Picture(picture);
    for (int x = 0; x < newPicture.getWidth(); x++) 
    {
      for (int y = 0; y < newPicture.getHeight(); y++) 
      {
        setLow(newPicture.getPixel(x, y), color);
      }
    }
      
    return newPicture;
  } 

  /**
   * Sets the highest two bits of each pixel's colors
   * to the lowest two bits of each pixel's colors
   */
  public static Picture revealPicture(Picture hidden)
  {
    Picture copy = new Picture(hidden);
    Pixel[][] pixels = copy.getPixels2D();
    Pixel[][] source = hidden.getPixels2D();
    for (int r = 0; r < pixels.length; r++)
    {
      for (int c = 0; c < pixels[0].length; c++) {
        Color col = source[r][c].getColor();
        Pixel pixel = pixels[r][c];
        pixel.setBlue(rightToLeftMath(col.getBlue()));
        pixel.setRed(rightToLeftMath(col.getRed()));
        pixel.setGreen(rightToLeftMath(col.getGreen()));
      }
    }

    return copy;
  }

  public static int rightToLeftMath(int col)
  {
    int newColor = col%4*64 + col%64;
    return newColor;  
  }
  
  public static void main(String[] args)
  {
    Picture beach = new Picture ("StegoLab_Code/beach.jpg");
    beach.explore();
    // Test Clear Low Method:
    Picture copy1 = testClearLow(beach);
    // copy1.explore();
    // Test Clear Set Method:
    Picture copy2 = testSetLow(beach, Color.PINK);
    // copy2.explore();
    // Test Reveal Picture Method:
    Picture copy3 = revealPicture(copy2);
    copy3.explore();
  }
}