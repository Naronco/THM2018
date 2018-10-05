package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.math.Dimension2d;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Sprite {

    private File spriteFile;
    private BufferedImage bufferedImage;
    private int transparentColor;
    private Dimension2d dimension;

    public Sprite (File spriteFile, int transparentColor) {
        this.spriteFile = spriteFile;

        try {
            this.bufferedImage = ImageIO.read(this.spriteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.transparentColor = transparentColor;

        this.dimension = new Dimension2d(this.bufferedImage.getWidth(), this
                .bufferedImage.getHeight());
    }
    public Sprite (File spriteFile) {
        this(spriteFile, 0xFFFF00FF);
    }

    public File getSpriteFile () {
        return this.spriteFile;
    }
    public BufferedImage getBufferedImage () {
        return this.bufferedImage;
    }
    public int getTransparentColor () {
        return this.transparentColor;
    }
    public Dimension2d getDimension () {
        return this.dimension;
    }

    public int getPixel (int x, int y) {
        return this.bufferedImage.getRGB(x, y);
    }

}