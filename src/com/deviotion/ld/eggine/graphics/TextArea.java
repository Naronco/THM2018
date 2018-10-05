package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.math.Dimension2d;

import java.util.ArrayList;
import java.util.List;

public class TextArea {
    private int x, y, width, height;
    private Font font;
    private boolean animated = true;
    private boolean bordered = false;
    private int ticksPerCharacter = 1;
    private int maxLineCount = 0;

    private String activeMessage;

    private boolean buildingMessage = false;

    private int lineHeight;
    private int currentFirstLine;
    private int lineOffset;
    private int ticksSinceLastPixel = 0;
    private int ticksPerScrollingPixel = 2;

    private int visibleCharacters;
    private int ticksSinceLastCharacter;

    private List<ITextAreaListener> listeners = new ArrayList<>();
    private ITextAreaListener oneTimeListener = null;

    public TextArea(int x, int y, int width, int height, Font font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;

        this.lineHeight = ((int)font.getCharacterSize().getHeight() + 1);
    }

    public void showText(String text, ITextAreaListener oneTimeListener) {
        this.oneTimeListener = oneTimeListener;

        String[] words = text.toUpperCase().split(" ");

        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            int newLength = currentLine.length() + word.length();
            if (currentLine.length() > 0)
                ++newLength;

            boolean overBounds = newLength * Font.standard.getCharacterSize().getWidth() >= width;

            if (overBounds) {
                messageBuilder.append(currentLine).append('\n');
                currentLine = new StringBuilder();
            }

            if (currentLine.length() > 0)
                currentLine.append(' ');

            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            messageBuilder.append(currentLine);
        }

        activeMessage = messageBuilder.toString();
        visibleCharacters = animated ? 1 : activeMessage.length();
        ticksSinceLastCharacter = 0;
        buildingMessage = animated;
        currentFirstLine = 0;
        lineOffset = 0;
    }

    public void showText(String text) {
        showText(text, null);
    }

    public void update() {
        if (buildingMessage) {
            ++ticksSinceLastCharacter;

            if (ticksSinceLastCharacter >= ticksPerCharacter) {
                ++visibleCharacters;

                if (visibleCharacters == activeMessage.length()) {
                    for (ITextAreaListener listener : listeners)
                        listener.onScrollingFinished(this);
                    if (oneTimeListener != null) {
                        oneTimeListener.onScrollingFinished(this);
                        oneTimeListener = null;
                    }
                    buildingMessage = false;
                } else {
                    if (activeMessage.charAt(visibleCharacters) == '\n') {
                        if (currentFirstLine >= maxLineCount - 1) {
                            lineOffset = (currentFirstLine - maxLineCount + 1) * lineHeight + 1;
                            ticksSinceLastPixel = 0;
                        }
                        ++currentFirstLine;
                    }
                }

                ticksSinceLastCharacter = 0;
            }
        }

        if (lineOffset % lineHeight != 0 && ticksSinceLastPixel >= ticksPerScrollingPixel) {
            ++lineOffset;
            ticksSinceLastPixel = 0;
        }
        ++ticksSinceLastPixel;
    }

    public void render(Screen screen) {
        if (activeMessage != null) {
            if (bordered) {
                screen.mixOutlinedRectangle(x, y, width, height, 0xE0121212);
                screen.mixOutlinedRectangle(x + 1, y + 1, width, height, 0xE0121212);
                screen.mixRectangle(x + 1, y + 1, width - 2, height - 2, 0xE0F2F2F2);
            }

            String partialMessage = activeMessage.substring(0, visibleCharacters);

            int textStartX = bordered ? x + 2 : x;
            int textStartY = bordered ? y + 2 : y;
            int textWidth = bordered ? width - 4 : width;
            int textHeight = bordered ? height - 4 : height;

            screen.renderClippedText(textStartX, textStartY - lineOffset, textStartX, textStartY, textWidth, textHeight, font, partialMessage);
        }
    }

    public void sizeToFit() {
        Dimension2d size = font.sizeOfText(activeMessage);
        width = (int)size.getWidth();
        height = (int)size.getHeight();
        if (bordered) {
            width += 4;
            height += 4;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Font getFont() {
        return font;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public boolean isBordered() {
        return bordered;
    }

    public void setBordered(boolean bordered) {
        this.bordered = bordered;
    }

    public int getTicksPerCharacter() {
        return ticksPerCharacter;
    }

    public void setTicksPerCharacter(int ticksPerCharacter) {
        this.ticksPerCharacter = ticksPerCharacter;
    }

    public int getMaxLineCount() {
        return maxLineCount;
    }

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void addListener(ITextAreaListener listener) {
        listeners.add(listener);
    }
}
