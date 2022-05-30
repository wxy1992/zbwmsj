package com.bjrxkj.image

import javax.imageio.ImageIO
import java.awt.*

// cf. http://kyle-in-jp.blogspot.com/2008/08/java2d.html
// cf. http://d.hatena.ne.jp/toshyon/20060609/p1
import java.awt.image.BufferedImage

class GroovyLogoAdder {
  static def OUTPUT_FORMAT = 'png'

  void makeImage(File file,String filename, Position pos, int ratio,File iconImgFile) {
    BufferedImage iconImg=ImageIO.read(iconImgFile)
    String[] formatNames=ImageIO.getReaderFormatNames();
    def names=filename.toLowerCase().tokenize("\\.")
    if(names.size()==0){
        return
    }else{
        if(!formatNames.contains(names[-1])){
            return
        }else{
            OUTPUT_FORMAT=names[-1];
        }
    }
    //
    BufferedImage yourImg = ImageIO.read(file)

    //
    int newX = yourImg.width * (ratio / 100)
    int newY = newX * iconImg.height / iconImg.width
    //BufferedImage resizedIconImg = new BufferedImage(newX, newY, iconImg.getType())
      BufferedImage resizedIconImg = new BufferedImage(newX, newY,6)
    resizedIconImg.getGraphics().drawImage(iconImg.getScaledInstance(newX, newY, Image.SCALE_AREA_AVERAGING), 0, 0, newX, newY, null)

    //
    def putPosCalc = [
      (Position.topLeft)     : { [x: 0,                                    y: 0]                                      },
      (Position.topRight)    : { [x: yourImg.width - resizedIconImg.width, y: 0]                                      },
      (Position.bottomLeft)  : { [x: 0,                                    y: yourImg.height - resizedIconImg.height] },
      (Position.bottomRight) : { [x: yourImg.width - resizedIconImg.width, y: yourImg.height - resizedIconImg.height] },
      (Position.center)      : { [x: (yourImg.width / 2 - resizedIconImg.width / 2) as int,
                                  y: (yourImg.height / 2 - resizedIconImg.height / 2) as int] }
    ]

    //
    Graphics2D gr = yourImg.createGraphics()
    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F))
    def putPos = putPosCalc[pos]() //
    gr.drawImage(resizedIconImg, putPos.x, putPos.y ,null)
    gr.dispose() //

    //
     /*
    def resultFile = new File(file.name + '_groovy.' + OUTPUT_FORMAT)
    ImageIO.write(yourImg, OUTPUT_FORMAT, resultFile)
    println "${resultFile.name} created."
    */
      ImageIO.write(yourImg, OUTPUT_FORMAT, file)
  }
}
