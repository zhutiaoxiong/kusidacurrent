package common.pinyinzhuanhuan;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by qq522414074 on 2016/9/28.
 */
public class StreamTool {
    /**
     * 从流中读取数据
     * @param inStream
     * @return
     */
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*1024*100];
        int len = 0;
        while( (len = inStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
