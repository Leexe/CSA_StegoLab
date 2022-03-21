import java.awt.Color;

public class Steganography
{
  /**
  * Clear the lower (rightmost) two bits in a pixel
  */
  public static void clearLow( Pixel p )
  {
    p.setBlue(p.getBlue()/4*4);
    p.setRed(p.getRed()/4*4);
    p.setGreen(p.getGreen()/4*4);
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
      for (int c = 0; c < pixels[0].length; c++) 
      {
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
    int newColor = col%4*64; //+ col%64
    return newColor;  
  }

  /**
   * Determines if the secret image can fit in source, which if
   * source nad secret are the same dimensions
   * @param source is not null
   * @param secret is not null
   * @return true if secret can be hidden in source, false otherwise
   */
  public static boolean canHide(Picture source, Picture secret)
  {
    if (source.getHeight() == secret.getHeight() && source.getWidth() == secret.getWidth()) {
      return true;
    }
    return false;
  }

  /**
   * Creates a new picture with data from the secret picture
   * @param source is not null
   * @param secret is not null
   * @return combined Picture of source and secret
   * Precondition: source is the same height and width as secret
   */
  public static Picture hidePicture(Picture source, Picture secret) 
  {
    Picture newPicture = new Picture(source);
    Pixel[][] pixels = newPicture.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    try 
    {
      for (int r = 0; r < pixels.length; r++)
      {
        for (int c = 0; c < pixels[0].length; c++) 
        {
          setLow(pixels[r][c], secretPixels[r][c].getColor());
        }
      }

      return newPicture;
    }
    catch (Exception e)
    {
      System.out.println("ERROR: The image you are trying to commbine does not fit!");
      
      return newPicture;
    }
  }

  /**
   * 
   * @param source
   * @param secret
   * @param startRow
   * @param startColumn
   * @return
   */
  public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn) 
  {
    Picture newPicture = new Picture(source);
    Pixel[][] pixels = newPicture.getPixels2D();
    Pixel[][] secretPixels = secret.getPixels2D();

    for (int r = 0; r < secretPixels.length; r++)
    {
      for (int c = 0; c < secretPixels[0].length; c++) 
      {
        setLow(pixels[r+startColumn][c+startRow], secretPixels[r][c].getColor());
      }
    }

    return newPicture;
  }
  
  public static void main(String[] args)
  {
    Picture beach = new Picture ("StegoLab_Code/beach.jpg");
    Picture flower1 = new Picture ("StegoLab_Code/flower1.jpg");
    Picture flower2 = new Picture ("StegoLab_Code/flower2.jpg");
    Picture robot = new Picture("StegoLab_Code/robot.jpg");

    // Picture copy1 = testClearLow(beach);
    // Picture copy2 = testSetLow(beach, Color.PINK);
    // Picture copy3 = hidePicture(flower1, flower2);
    // Picture revealCopy = revealPicture(copy3);

    Picture hidden1 = hidePicture(beach, robot, 65, 208);
    Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
    Picture unhidden = revealPicture(hidden2);

    unhidden.explore();
    // flower1.explore();
    // copy3.explore();
    // copy1.explore();
    // copy2.explore();
    // copy3.explore();
    // revealCopy.explore();
  }
}