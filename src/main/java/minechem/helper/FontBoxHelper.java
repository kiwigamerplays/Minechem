package minechem.helper;

import fontbox.FontException;
import fontbox.FontMetric;
import fontbox.FontRenderBuffer;
import fontbox.LayoutCalculator;
import fontbox.PageBox;
import fontbox.WrittenFontRenderer;
import fontbox.io.StackedPushbackStringReader;
import java.io.IOException;
import minechem.Compendium;
import minechem.Config;
import org.apache.logging.log4j.Level;

public class FontBoxHelper
{
    public static LayoutCalculator layoutCalculator = new LayoutCalculator();
    public static final WrittenFontRenderer writtenFontRenderer = new WrittenFontRenderer();
    public static final FontMetric fontMetricDaniel;
    public static final FontRenderBuffer fontRenderBufferDaniel;

    /**
     * Sets up all static data
     */
    static
    {
        fontMetricDaniel = new FontMetric(Compendium.Resource.Font.danielFont, 418, 242, Compendium.Resource.Font.danielMetrics);
        try
        {
            fontMetricDaniel.buildFont();
        } catch (FontException e)
        {
            LogHelper.exception("Could not load custom font", e, Level.WARN);
        }
        fontRenderBufferDaniel = new FontRenderBuffer(fontMetricDaniel);
    }

    public static class PageBoxMetrics
    {
        private int height, width;
        private int margin_l, margin_r;
        private int min_space, min_lineHeight;

        public PageBoxMetrics(int width, int height, int margin_l, int margin_r, int min_space, int min_lineHeight)
        {
            this.width = width;
            this.height = height;
            this.margin_l = margin_l;
            this.margin_r = margin_r;
            this.min_space = min_space;
            this.min_lineHeight = min_lineHeight;
        }
    }

    /**
     * Adds line to a {@link fontbox.PageBox} TODO: fix this the value reference makes it derp
     *
     * @param text    the text to add
     * @param pageBox the pageBox to add to
     */
    public static void boxLine(String text, PageBox pageBox)
    {
        try
        {
            layoutCalculator.boxLine(fontMetricDaniel, new StackedPushbackStringReader(text), pageBox);
        } catch (IOException e)
        {
            LogHelper.exception("Something went wrong during the boxing of the text", e, Level.WARN);
        }
    }

    /**
     * Renders a given {@link fontbox.PageBox} at given position
     *
     * @param pageBox
     * @param x
     * @param y
     * @param z
     */
    public static void renderPageBox(PageBox pageBox, float x, float y, float z)
    {
        writtenFontRenderer.renderPages(fontMetricDaniel, fontRenderBufferDaniel, pageBox, x, y, z, Config.debugMode);
    }

    /**
     * Creates a {@link fontbox.PageBox} with the text as content
     *
     * @param text     content
     * @param width
     * @param height
     * @param margin_l left margin
     * @param margin_r right margin
     * @param min_sp   minimum space size
     * @param min_lhs  minimum font size
     * @return the PageBox with content
     */
    public static PageBox boxText(String text, int width, int height, int margin_l, int margin_r, int min_sp, int min_lhs)
    {
        try
        {
            return layoutCalculator.boxParagraph(fontMetricDaniel, text, width, height, margin_l, margin_r, min_sp, min_lhs)[0];
        } catch (IOException e)
        {
            LogHelper.exception("Something went wrong during the boxing of the text", e, Level.WARN);
        }
        return null;
    }

    /**
     * Creates a {@link fontbox.PageBox} with the text as content
     *
     * @param text           the content
     * @param pageBoxMetrics metrics object for the pageBox
     * @return
     */
    public static PageBox boxText(String text, PageBoxMetrics pageBoxMetrics)
    {
        return boxText(text, pageBoxMetrics.width, pageBoxMetrics.height, pageBoxMetrics.margin_l, pageBoxMetrics.margin_r, pageBoxMetrics.min_space, pageBoxMetrics.min_lineHeight);
    }
}