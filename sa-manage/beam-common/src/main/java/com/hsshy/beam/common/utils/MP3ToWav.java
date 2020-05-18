package com.hsshy.beam.common.utils;
import javax.sound.sampled.*;
import java.io.*;

/**
 * MP3转wav工具类
 *
 * @author hs
 * @date 2018年9月2日 下午4:14:22
 */
public class MP3ToWav {



    /**
     * mp3的字节数组生成wav文件
     * @param sourceBytes
     * @param targetPath
     */
    public static boolean byteToWav(byte[] sourceBytes, String targetPath) {
        if (sourceBytes == null || sourceBytes.length == 0) {
            System.out.println("Illegal Argument passed to this method");
            return false;
        }

        try (final ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes); final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(bais)) {
            AudioFormat sourceFormat = sourceAIS.getFormat();
            // 设置MP3的语音格式,并设置16bit
            AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            // 设置百度语音识别的音频格式
            AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
            try (
                // 先通过MP3转一次，使音频流能的格式完整
                final AudioInputStream mp3AIS = AudioSystem.getAudioInputStream(mp3tFormat, sourceAIS);
                // 转成百度需要的流
                final AudioInputStream pcmAIS = AudioSystem.getAudioInputStream(pcmFormat, mp3AIS)) {
                // 根据路径生成wav文件
                AudioSystem.write(pcmAIS, AudioFileFormat.Type.WAVE, new File(targetPath));
            }
            return true;
        } catch (IOException e) {
            System.out.println("文件转换异常：" + e.getMessage());
            return false;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println("文件转换异常：" + e.getMessage());
            return false;
        }
    }

    /**
     * 将文件转成字节流
     * @param filePath
     * @return
     */
    private static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void main(String args[]) {
        String filePath = "d:/tmp/delay.mp3";
        String targetPath = "d:/tmp/test1.wav";
        byteToWav(getBytes(filePath), targetPath);

    }
}
