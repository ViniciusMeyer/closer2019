package Workers; 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author vinicius meyer
 */
public class Filter {


    public static BufferedImage grayScale(BufferedImage imagem) {
        int width = imagem.getWidth();
        int height = imagem.getHeight();

        int media = 0;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                       
                int rgb = imagem.getRGB(i, j);
                int r = (int) ((rgb & 0x00FF0000) >>> 16); //R
                int g = (int) ((rgb & 0x0000FF00) >>> 8);  //G
                int b = (int) (rgb & 0x000000FF);       //B

     
                media = (r + g + b) / 3;

                Color color = new Color(media, media, media);
                
                imagem.setRGB(i, j, color.getRGB());
            }
        }
        return imagem;
    }

    public static BufferedImage negative(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                
                int r = 255 - (int) ((rgb & 0x00FF0000) >>> 16);
                int g = 255 - (int) ((rgb & 0x0000FF00) >>> 8);
                int b = 255 - (int) (rgb & 0x000000FF);
                Color color = new Color(r, g, b);
                image.setRGB(i, j, color.getRGB());
            }
        }

        return image;
    }

    public static BufferedImage threshold(BufferedImage image, int limiar) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int r = (int) ((rgb & 0x00FF0000) >>> 16);
                int g = (int) ((rgb & 0x0000FF00) >>> 8);
                int b = (int) (rgb & 0x000000FF);
                int media = (r + g + b) / 3;
                Color white = new Color(255, 255, 255);
                Color black = new Color(0, 0, 0);

                if (media < limiar) {
                    image.setRGB(i, j, black.getRGB());
                } else {
                    image.setRGB(i, j, white.getRGB());
                }
            }
        }
        return image;
    }
}
