import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import es.thepuar.InfiniteSpace.utils.XugglerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;

import java.io.File;

public class XuggerUtilTest {

//    @Test
    public void getVideoSecondTest(){
        File file = new File("Z:\\App\\InfiniteSpace\\upload\\One Piece - 001 [SC] [Animelvnet].mp4");
        Integer segundos = XugglerUtil.getVideoSeconds(file);
        System.out.println("Duracion en segundos: "+segundos);
    }

    @Test
    public void leerTodo() {

        File file = new File("Z:\\App\\InfiniteSpace\\upload\\One Piece - 001 [SC] [Animelvnet].mp4");
        IContainer container = IContainer.make();
        container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
        // query how many streams the call to open found

        int numStreams = container.getNumStreams();

        // query for the total duration

        long duration = container.getDuration();

        // query for the file size

        long fileSize = container.getFileSize();

        // query for the bit rate

        long bitRate = container.getBitRate();

        System.out.println("Number of streams: " + numStreams);

        System.out.println("Duration (ms): " + duration);

        System.out.println("File Size (bytes): " + fileSize);

        System.out.println("Bit Rate: " + bitRate);

        // iterate through the streams to print their meta data

        for (int i = 0; i < numStreams; i++) {

// find the stream object

            IStream stream = container.getStream(i);

// get the pre-configured decoder that can decode this stream;

            IStreamCoder coder = stream.getStreamCoder();

            System.out.println("*** Start of Stream Info ***");

            System.out.printf("stream %d: ", i);

            System.out.printf("type: %s; ", coder.getCodecType());

            System.out.printf("codec: %s; ", coder.getCodecID());

            System.out.printf("duration: %s; ", stream.getDuration());

            System.out.printf("start time: %s; ", container.getStartTime());

            System.out.printf("timebase: %d/%d; ",

                    stream.getTimeBase().getNumerator(),

                    stream.getTimeBase().getDenominator());

            System.out.printf("coder tb: %d/%d; ",

                    coder.getTimeBase().getNumerator(),

                    coder.getTimeBase().getDenominator());

            System.out.println();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {

                System.out.printf("sample rate: %d; ", coder.getSampleRate());

                System.out.printf("channels: %d; ", coder.getChannels());

                System.out.printf("format: %s", coder.getSampleFormat());



            } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

                System.out.printf("width: %d; ", coder.getWidth());

                System.out.printf("height: %d; ", coder.getHeight());

                System.out.printf("format: %s; ", coder.getPixelType());

                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());

            }

            System.out.println();

            System.out.println("*** End of Stream Info ***");

        }

    }
}
