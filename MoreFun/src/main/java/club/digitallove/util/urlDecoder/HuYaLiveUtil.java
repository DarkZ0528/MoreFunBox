package club.digitallove.util.urlDecoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HuYaLiveUtil {
    public static final String urlFlag = "https://www.huya.com/";

    /**
     * 判断是否是虎牙直播的网址
     *
     * @param url
     * @return
     */
    public static Boolean isHuYaLiveUrl(String url) {
        if (url.indexOf(urlFlag) != -1) {
            return true;
        }
        return false;
    }

    /**
     * 根据Url解析流地址
     */

    public static Map<String, String>  getRealSrcFromUrl(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element element = document.getElementsByTag("body").first();
        String sub1 = element.html().substring(element.html().indexOf("hyPlayerConfig"));
        String sub2 = sub1.substring(sub1.indexOf("{"), sub1.indexOf("}") + 1).trim();
        JSONObject hyPlayerConfig = JSONObject.parseObject(sub2);

        JSONObject stream = JSONObject.parseObject(new String(Base64.getDecoder().decode((String) hyPlayerConfig.get("stream")), "UTF-8"));
        JSONArray streamInfoList = stream.getJSONArray("data").getJSONObject(0).getJSONArray("gameStreamInfoList");
        JSONObject streamInfo = null;
        for (int i = 0; i < streamInfoList.size(); i++) {
            JSONObject streamInfoListItem = streamInfoList.getJSONObject(i);
//            System.out.println(streamInfoListItem);
            if ("AL".equals((String) streamInfoListItem.get("sCdnType"))) {
                streamInfo = streamInfoListItem;
            }
        }
        String realUrl = streamInfo.getString("sFlvUrl") + "/" + streamInfo.getString("sStreamName") + "." + streamInfo.getString("sFlvUrlSuffix") + "?" + streamInfo.getString("sFlvAntiCode");
        Map<String ,String> realUrlMap=new HashMap<>();
        for (int i = 0; i < stream.getJSONArray("vMultiStreamInfo").size(); i++) {
            JSONObject vMultiStreamInfoItem = stream.getJSONArray("vMultiStreamInfo").getJSONObject(i);
            String label = vMultiStreamInfoItem.getString("sDisplayName");
//            System.out.println(label);
//            System.out.println((realUrl + "&ratio=" + vMultiStreamInfoItem.getString("iBitRate")).replace("&amp;", "&"));
            realUrlMap.put(label,(realUrl + "&ratio=" + vMultiStreamInfoItem.getString("iBitRate")).replace("&amp;", "&"));
        }
        System.out.println(realUrlMap.toString());
        return realUrlMap;
    }

    public static void main(String[] args) throws IOException {
        HuYaLiveUtil.getRealSrcFromUrl("https://www.huya.com/949527");
    }

}
