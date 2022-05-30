package com.bjrxkj.image

import com.sun.image.codec.jpeg.JPEGCodec
import com.sun.image.codec.jpeg.JPEGImageEncoder

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

public final class ImageUtils {
    public ImageUtils() {

    }

    /**//*
     * public final static String getPressImgPath() { return ApplicationContext
     * .getRealPath("/template/data/util/shuiyin.gif"); }
     */

    /** *//**
     * 把图片印刷到图片上
     *
     * @param pressImg --
     *            水印文件
     * @param targetImg --
     *            目标文件
     * @param x
     *            --x坐标
     * @param y
     *            --y坐标
     */
    public final static void pressImage(String pressImg, String targetImg,
                                        int x, int y) {
        try {
            //目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            //水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            //水印文件结束
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** *//**
     * 打印文字水印图片
     *
     * @param pressText
     *            --文字
     * @param targetImg --
     *            目标图片
     * @param fontName --
     *            字体名
     * @param fontStyle --
     *            字体样式
     * @param color --
     *            字体颜色
     * @param fontSize --
     *            字体大小
     * @param x --
     *            偏移量
     * @param y
     */

    public static void pressText(String pressText, String targetImg,
                                 String fontName, int fontStyle, int color, int fontSize, int x,
                                 int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // String s="www.qhd.com.cn";
            g.setColor(new Color(color,false));
            g.setFont(new Font(fontName, fontStyle, fontSize));

            g.drawString(pressText, wideth - fontSize - x, height - fontSize                    / 2 - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {

        }
    }
    public static  byte[] pressText(String pressText, byte[] targetImgBytes,
                                 String fontName, int fontStyle, int color, int fontSize, int x,
                                 int y) {
        try {
            InputStream _file=new ByteArrayInputStream(targetImgBytes) ;
            Image src = ImageIO.read(_file);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
                    Font font=new Font(fontName, fontStyle, fontSize)
                    Color color1=new Color(0,128,255,200)
                   // Color color1=new Color(255,false)
                    g.setColor(color1);
                    g.setFont(font);
                    int fw=g.getFontMetrics(font).stringWidth(pressText)

                    int w=width - fw - x
                    int h=height - fontSize/ 2 - y
                    g.drawString(pressText,w , h);
            g.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JPEGImageEncoder encoder= JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            byte[] b=out.toByteArray()
            out.close();
            return b
        } catch (Exception e) {

        }
        return null
    }
  //  public static void main(String[] args) {
      //  pressText("李白杜","e:\\1.jpg","font-weight", Font.BOLD,255,70,700, 100);
      //  pressImage("e:/2.png",          "e:/1.jpg", 0, 0);

   // }
}
