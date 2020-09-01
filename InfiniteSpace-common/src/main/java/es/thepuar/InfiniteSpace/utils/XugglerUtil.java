package es.thepuar.InfiniteSpace.utils;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

import java.io.File;

public class XugglerUtil {

    public static Integer getVideoSeconds(File file) {

        IContainer container = IContainer.make();
        container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
        int numStreams = container.getNumStreams();
        Long duration = null;
        Double timebase = null;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

                System.out.printf("width: %d; ", coder.getWidth());

                System.out.printf("height: %d; ", coder.getHeight());

                System.out.printf("format: %s; ", coder.getPixelType());

                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());

                duration = stream.getDuration();

                timebase =  stream.getTimeBase().getNumerator() /(double)stream.getTimeBase().getDenominator();
            }
        }
        return (int)(duration.doubleValue() * timebase.doubleValue());
    }

    public static Integer getVideoHeight(File file){
        IContainer container = IContainer.make();
        container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
        int numStreams = container.getNumStreams();
        Long duration = null;
        Double timebase = null;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                return  coder.getHeight();
            }
        }
        return null;
    }

    public static Integer getVideoWidth(File file){
        IContainer container = IContainer.make();
        container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
        int numStreams = container.getNumStreams();
        Long duration = null;
        Double timebase = null;
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
              return  coder.getWidth();
            }
        }
        return null;
    }
}
