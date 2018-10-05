package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;

import java.io.File;

public class Font {
    public static Font standard = new Font(new File("res/font.png"));
    public static Font white = new Font(new File("res/fontwhite.png"));

    private String characterIndex = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,:;!? 'ÄÖÜ>\"-()/";
    private SpriteSheet spriteSheet;
    private Dimension2d characterSize;

    public Font(File bitmapFile) {
        Sprite sprite = new Sprite(bitmapFile);
        this.characterSize = new Dimension2d(6, 8);
        this.spriteSheet = new SpriteSheet(sprite, characterSize);
    }

    public Dimension2d sizeOfText(String text) {
        int width = 0, height = (int)characterSize.getHeight() + 1;
        int currentLineWidth = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == '\n') {
                height += (int) characterSize.getHeight() + 1;
                if (currentLineWidth > width)
                    width = currentLineWidth;
                currentLineWidth = 0;
            } else {
                currentLineWidth += (int) characterSize.getWidth();
            }
        }
        if (currentLineWidth > width)
            width = currentLineWidth;
        return new Dimension2d(width, height);
    }

    public Sprite getSprite() {
        return spriteSheet.getSprite();
    }

    public Dimension2d getCharacterSize() {
        return characterSize;
    }

    public Vector2d getCharacterVector(char ch) {
        return spriteSheet.getTileVector(characterIndex.indexOf(ch));
    }
}
