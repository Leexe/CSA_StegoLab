import java.awt.Color;
import java.util.ArrayList;

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
   * Hides a secret picture in the smallest digits of the source
   * @param source image that is hiding the secret image
   * @param secret the secret image
   * @param startRow row where the top-right pixel of the secret image is going to go
   * @param startColumn column where the top-right pixel of the secret image is going to go
   * @return new image with a hidden secret
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

  /**
   * Compares two images if they are the same image
   * @param image1 one of the images you are comparing
   * @param image2 one of the image you are comparing
   * @return if the image is the same
   */
  public static Boolean isSame(Picture image1, Picture image2)
  {
    if (sameSize(image1, image2))
    {
      for (int x = 0; x < image1.getWidth(); x++ ) 
      {
        for (int y = 0; y < image1.getHeight(); y++)
        {
          if (image1.getPixel(x, y).getColor().equals(image2.getPixel(x, y).getColor()) != true) 
          {
            return false;
          }
        }
      }
      return true;
    }

    return false;
  }

  /**
   * Compares two pictures if they have the same width/height
   * @param image1 one of the images you are comparing
   * @param image2 one of the images you are comparing
   * @return boolean if the images are the same
   */
  public static Boolean sameSize(Picture image1, Picture image2)
  {
    if (image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight())
    {
      return true;
    }
    return false;
  }

  /**
   * stores the coordinates of the different pixels between two images in an array list
   * @param image1 one of the images you are comparing
   * @param image2 one of the images you are comparing
   * @return an array list with coordinates
   * precondition: the two images are the same size
   */
  public static ArrayList<Integer[]> findDifferences(Picture image1, Picture image2)
  {
    ArrayList<Integer[]> pointList = new ArrayList<Integer[]>();

    if (sameSize(image1, image2))
    {
      for (int x = 0; x < image1.getWidth(); x++ ) 
      {
        for (int y = 0; y < image1.getHeight(); y++)
        {
          if (image1.getPixel(x, y).getColor().equals(image2.getPixel(x, y).getColor()) != true) 
          {
            Integer[] coords = {x,y};
            pointList.add(coords);
          }
        }
      }
      return pointList;
    }

    return pointList;
  }

  /**
   * Draws a rectangle around the part of a picture that is different
   * @param image the original image
   * @param arrayList list of coords for the different pixels
   * @return a Picture with a new rectangle 
   */
  public static Picture showDifferentArea (Picture image, ArrayList<Integer[]> arrayList)
  {
    Picture newPicture = new Picture(image);
    int minWidth = Integer.MAX_VALUE;
    int maxWidth = Integer.MIN_VALUE;
    int minHeight = Integer.MAX_VALUE;
    int maxHeight = Integer.MIN_VALUE;

    for (Integer[] coords : arrayList)
    {
      if(coords[0] < minWidth)
      {
        minWidth = coords[0];
      }
      if(coords[0] > maxWidth)
      {
        maxWidth = coords[0];
      }
      if(coords[1] < minHeight)
      {
        minHeight = coords[1];
      }
      if(coords[1] > maxHeight)
      {
        maxHeight = coords[1];
      }
    }
    // Debug:
    // System.out.println("This is the min x coord:" + minWidth);
    // System.out.println("This is the max x coord:" + maxWidth);
    // System.out.println("This is the min y coord:" + minHeight);
    // System.out.println("This is the max y coord:" + maxHeight);
    
    for (Integer[] coords : arrayList) 
    {
      if (coords[0] == minWidth || coords[0] == maxWidth || coords[1] == minHeight || coords[1] == maxHeight)
      {  
        newPicture.getPixel(coords[0], coords[1]).setColor(Color.RED);
      }
    }

    return newPicture;
  }

/**
 * Takes a string consisting of letters and spaces and 
 * encodes the string into an arraylist of integers.
 * The integers are 1-26 for A-Z, 27 for space, and 0 for end of string. 
 * The arraylist of integers is returned.
 * @param s string consisting of letters and spaces
 * @return ArrayList containing integer encoding of uppercase version of s
 */
  public static ArrayList<Integer> encodeString(String s)
  {
    s = s.toUpperCase();
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < s.length(); i++){
      if (s.substring(i,i+1).equals(" "))
      {
        result.add(27);
      }
      else{
        result.add(alpha.indexOf(s.substring(i,i+1))+1);
      } 
    }
    result.add(0);
    return result;
  }
  
  /**
   * Give a number from 0 - 63, creates and returns a 3-element
   * int array consisting of the itegers representing the 
   * pairs of bits in the number from right to left
   * @param num number to be broken up
   * @return bit pairs in number
   */
  private static int[] getBitPairs(int num)
  {
    int[] bits = new int[3];
    int code = num;
    for (int i = 0; i < 3; i++)
    {
      bits[i] = code % 4;
      code = code / 4;
    }
    return bits;
  }
/**
 * Returns the string represented by the codes arraylistt.
 * 1-26 = A-Z, 27 = space
 * @param s string to be encoded into numbers
 * @return multiple integers that are the encoded string
 */
 public static String decodeString(ArrayList<Integer> codes)
  {
    String result= ""; 
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
    for (int i=0; i < codes.size(); i++) 
    {
      if (codes.get(i) == 27) 
    {
      result = result + " ";
    }
    else 
    {
      result = result + alpha.substring(codes.get(i)-1,codes.get(i)); 
    }
  }
   return result; 
  }

/**
 * returns a string hidden in the picture
 * @param source picture with hidden string
 * @return revealed string
 */
public static String revealText(Picture source)
{
  String end = new String();

  return end;
}

  public static void main(String[] args)
  {
    Picture beach = new Picture ("StegoLab_Code/beach.jpg");
    Picture flower1 = new Picture ("StegoLab_Code/flower1.jpg");
    Picture flower2 = new Picture ("StegoLab_Code/flower2.jpg");
    Picture robot = new Picture("StegoLab_Code/robot.jpg");
    Picture swan = new Picture("StegoLab_Code/swan.jpg");
    Picture swan2 = new Picture("StegoLab_Code/swan.jpg");

    // A1: Tests clearLow, setLow, and hidePicture methods
    // Picture copy1 = testClearLow(beach);
    // Picture copy2 = testSetLow(beach, Color.PINK);
    // Picture copy3 = hidePicture(flower1, flower2);
    // Picture revealCopy = revealPicture(copy3);
    // copy1.explore();
    // copy2.explore();
    // copy3.explore();

    // A2: Tests hidePicture method
    // Picture hidden1 = hidePicture(beach, robot, 65, 208);
    // Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
    // Picture unhidden = revealPicture(hidden2);
    // unhidden.explore();

    // A3: Tests isSame method
    // System.out.println("Swan and swan2 are the same: " + isSame(swan, swan2));
    // swan = testClearLow(swan);
    // System.out.println("Swan and swan2 are the same (after clearLow run on swan): " 
    // + isSame(swan, swan2));

    // A3: Tests findDifferences method
    // ArrayList<Integer[]> pointList = findDifferences(swan, swan2);
    // System.out.println("PointList after comparing two identical images " 
    // + pointList.size());
    // pointList = findDifferences(swan, koala);
    // System.out.println("PointList after comparing two different sized " 
    // + pointList.size());
    // swan2 = hidePicture(swan, robot, 10, 20);
    // pointList = findDifferences(swan, swan2);
    // System.out.println("PointList after hiding a picture with a size of " 
    // + pointList.size());

    // A3: Tests the showDifferentAreas method
    Picture hall = new Picture("StegoLab_Code/femaleLionAndHall.jpg");
    Picture hall2 = hidePicture(hall, robot, 50, 300);
    Picture hall3 = hidePicture(hall2, flower1, 115, 275);
    hall3.explore();
    if(!isSame(hall, hall3))
    {
      Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
      hall4.show();
      Picture unhiddenHall3 = revealPicture(hall3);
      unhiddenHall3.show();
    }
  }
}