package club.digitallove.util.urlDecoder;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mortbay.util.MultiMap;
import org.mortbay.util.UrlEncoded;

import java.io.IOException;

public class BiliBiliUtil {
    public static final String urlFlag = "https://www.bilibili.com/video/";

    /**
     * 根据url获得真实mp4地址
     *
     * @param url
     * @return
     */
    public static String getMp4SrcFromUrl(String url) throws IOException {
        return getMp4Src(getBVFromUrl(url));
    }

    /**
     * 判断是否是bilibili的网址
     * @param url
     * @return
     */
    public static Boolean isBiliBiliUrl(String url){
        if(url.indexOf(urlFlag)!=-1){
            return true;
        }
        return false;
    }
    /**
     * 根据网址获得BV号
     */
    public static String getBVFromUrl(String url) {
        String bvNum = null;
        if (url.indexOf("?") != -1) {
            bvNum = url.substring(url.indexOf(urlFlag) + urlFlag.length(), url.indexOf("?"));
        } else {
            bvNum = url.substring(url.indexOf(urlFlag) + urlFlag.length());
        }
        return bvNum;
    }

    /**
     * 根据BV号解析bilibili视频播放地址
     */

    public static String getMp4Src(String bv) throws IOException {
        String selieInfo = urlFlag + bv;
        Document document = Jsoup.connect(selieInfo).get();
        Elements elements = document.getElementsByTag("script");
        Integer aid = null;
        Integer cid = null;
        for (Element element : elements) {
            if (element.toString().indexOf("window.__INITIAL_STATE__") != -1) {
                JSONObject initialState=JSONObject.parseObject(element.html().substring(element.html().indexOf("=") + 1,element.html().indexOf(";(function()")));
                JSONObject data =initialState.getJSONObject("videoData");
//                System.out.println( recData);
                aid = (Integer) data.get("aid");
                if (initialState.getJSONObject("cidMap").containsKey("aid")){
                    cid= (Integer) initialState.getJSONObject("cidMap").getJSONObject("aid").get("cid");
                }else {
                    cid = (Integer) data.get("cid");
                }
                break;
            }
        }
//        System.out.println("https://www.xbeibeix.com/api/bilibiliapi.php?url=https://www.bilibili.com/&aid=" + aid + "&cid=" + cid);
        document = Jsoup.connect("https://www.xbeibeix.com/api/bilibiliapi.php?url=https://www.bilibili.com/&aid=" + aid + "&cid=" + cid).get();
        JSONObject mp4Src = JSONObject.parseObject(document.getElementsByTag("body").html());
//        return URLEncoder.encode((String) mp4Src.get("url"),"GBK");
        System.out.println("=========解析BILIBILI视频完成=========\n视频CID:"+cid+"\t视频AID:"+aid);
        System.out.println("视频播放源:"+((String) mp4Src.get("url")).replace("&amp;", "&"));
        return ((String) mp4Src.get("url")).replace("&amp;", "&");
    }

    public static void main(String[] args) throws IOException {
        BiliBiliUtil.getMp4SrcFromUrl("https://www.bilibili.com/video/BV11z4y1D7Ji");
    }

}
