package club.digitallove.util.urlDecoder;

import java.util.HashMap;
import java.util.Map;

import static club.digitallove.util.urlDecoder.VideoType.*;

public class UrlUtil {
    public static VideoType GetVideoType(String url){
        if (BiliBiliUtil.isBiliBiliUrl(url)){
            return BiliBiliVideo;
        }else if (HuYaLiveUtil.isHuYaLiveUrl(url)){
            return HuYaLive;
        }else {
            return NotSupposed;
        }
    }
}
