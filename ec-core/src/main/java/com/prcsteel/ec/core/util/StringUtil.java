package com.prcsteel.ec.core.util;

import com.prcsteel.ec.core.model.BaiduIPResult;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.SinaIPResult;
import com.prcsteel.ec.core.model.TaobaoIPResult;
import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtil
 * @Description: 字符串工具
 * @Author Green.Ge
 * @Date 2016年4月28日
 */
public class StringUtil {

    private static Logger logger = Logger.getLogger(StringUtil.class);

    private static final String getCitySwitch = getCitySwitch();  // 通过配置文件获取城市开关

    /**
     * 从配制文件中获取是否通过配置文件获取城市开关
     *
     * @return
     */
    public static String getCitySwitch() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(ResourceUtils.getFile("classpath:ecweb.properties")));
            return prop.getProperty("getCity.switch");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @Author: Green.Ge
     * @Description: 获取N位随机数字验证码（可用户短信验证码和初始化密码），支持数字和字母混合
     * @Date: 2016年4月28日
     */
    public static String genRandomCode(boolean numberFlag, int length) {
        String retStr;
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    /**
     * @Author: Green.Ge
     * @Description: 生成六位数字密码
     * @Date: 2016年4月28日
     */
    public static String genRandomCode() {
        return genRandomCode(true, 6);
    }

    /**
     * @Author: Green.Ge
     * @Description: 生成4位短信验证码
     * @Date: 2016年4月28日
     */
    public static String genSMSValidCode() {
        return genRandomCode(true, 4);
    }

    /**
     * @param phone 手机号码
     * @description 手机号码验证
     * @author peanut
     * @date 2016/04/29
     */
    public static boolean isPhoneNumberCheck(String phone) {
        Pattern pattern = Pattern.compile("[1][3,4,5,7,8][0-9]{9}", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(phone).matches();
    }

    private static final String localhost = "127.0.0.1";

    public static String getIpAddr(HttpServletRequest request) {
        // 是否读配置文件
        String isReadByProp = getCitySwitch;
        if (StringUtils.isBlank(getCitySwitch)) {
            isReadByProp = getCitySwitch();
        }
        if ("true".equals(isReadByProp)) {
            return getIp();
        }
        String ipAddress = null;
        // ipAddress = this.getRequest().getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals(localhost)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    logger.error("获取客户端ip时发生错误");
                }
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static String getCityByIp(String ip) {
        if (StringUtils.isBlank(ip)) return null;
        //从新浪获取
        SinaIPResult result_sina = getCityFromSinaByIp(ip);
        if (result_sina != null) {
            return result_sina.getCity();
        }
        //从淘宝获取
        TaobaoIPResult result_taobao = getCityFromTaobaoByIp(ip);
        if (result_taobao != null) {
            return result_taobao.getData().getCity();
        }
        //百度不可用
//		BaiduIPResult result_baidu = getCityFromBaiduByIp(ip);
//		if(result_baidu!=null){
//			return result_baidu.getRetData().getCity();
//		}
        //从IP138获取
        return getCityFromIP138ByIp(ip);
    }

    /**
     * 用request获取城市数据
     *
     * @param request
     * @return
     */
    public static String getCity(HttpServletRequest request) {
        return getCityByIp(getIpAddr(request));
    }

    /**
     * 从配制文件中获取ip
     *
     * @return
     */
    public static String getIp() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(ResourceUtils.getFile("classpath:change-city.properties")));
            return prop.getProperty("ip");
        } catch (IOException e) {
            return null;
        }
    }

    private static HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
    private static HttpGet get;

    /**
     * @Author: Green.Ge
     * @Description: 从淘宝取IP信息
     * @Date: 2016年4月29日
     */

    private static SinaIPResult getCityFromSinaByIp(String ip) {
        SinaIPResult result = null;
        String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip;

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 100);
        get = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String retString = EntityUtils.toString(httpResponse.getEntity());
            if (StringUtils.isBlank(retString)) {
                return null;
            }
            String placeholder = "var remote_ip_info = ";
            String json = retString.substring(placeholder.length(), retString.length() - 1);
            JSONObject jsonObj = JSONObject.fromObject(json);
            result = (SinaIPResult) JSONObject.toBean(jsonObj, SinaIPResult.class);
        } catch (Exception e) {
            logger.error("调用新浪ip api出错：" + e.getMessage());
        }
        return result;
    }

    /**
     * @Author: Green.Ge
     * @Description: 从淘宝取IP信息
     * @Date: 2016年4月29日
     */

    private static TaobaoIPResult getCityFromTaobaoByIp(String ip) {
        TaobaoIPResult result = null;
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constant.GET_CITY_TIMEOUT);
        get = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String json = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObj = JSONObject.fromObject(json);
            result = (TaobaoIPResult) JSONObject.toBean(jsonObj, TaobaoIPResult.class);
        } catch (Exception e) {
            logger.error("调用淘宝ip api出错：" + e.getMessage());
        }
        return result;
    }

    /**
     * @Author: Green.Ge
     * @Description: 从百度取IP信息
     * @Date: 2016年4月29日
     */
    private static BaiduIPResult getCityFromBaiduByIp(String ip) {
        BaiduIPResult result = null;
        String url = "http://apistore.baidu.com/microservice/iplookup?ip=" + ip;
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constant.GET_CITY_TIMEOUT);
        get = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String json = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObj = JSONObject.fromObject(json);
            TaobaoIPResult r = (TaobaoIPResult) JSONObject.toBean(jsonObj, TaobaoIPResult.class);
        } catch (Exception e) {
            logger.error("调用百度ip api出错：" + e.getMessage());
        }
        return result;
    }

    /**
     * @Author: Green.Ge
     * @Description: 从IP138取IP信息
     * @Date: 2016年4月29日
     */
    private static String getCityFromIP138ByIp(String ip) {
        String result = "";
        String placeHolder = "<li>本站主数据：";
        String placeHolder2 = "自治区";
        String url = "http://ip138.com/ips138.asp?ip=" + ip;
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constant.GET_CITY_TIMEOUT);
        get = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String html = EntityUtils.toString(httpResponse.getEntity(), "GBK");
            if (StringUtils.isBlank(html) || html.indexOf(placeHolder) < 0) {
                return result;
            }

            String regex = placeHolder + "(.*?)</li>";
            Pattern pattern = Pattern.compile(regex);
            Matcher mc = pattern.matcher(html);

            while (mc.find()) {
                String info = mc.group(0);
                String cityName = info.substring(info.indexOf("省") + 1, info.indexOf("市") + 1);
                int idx = cityName.indexOf(placeHolder);
                if (idx > -1) {
                    cityName = cityName.substring(placeHolder.length());
                }
                idx = cityName.indexOf(placeHolder2);
                if (idx > -1) {
                    cityName = cityName.substring(idx + placeHolder2.length());
                }
                return cityName;
            }
        } catch (Exception e) {
            logger.error("调用百度ip api出错：" + e.getMessage());
        }
        return result;
    }

    /**
     * 截断手机号
     *
     * @param phone 手机号
     * @return
     */
    public static String truncatePhone(String phone) {
        if (!StringUtils.isBlank(phone)) {
            return phone.substring(0, 3) + "*****" + phone.substring(8, phone.length());
        }
        return null;
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += java.lang.Character.toString(input[i]);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        if (StringUtils.isBlank(chinese)) {
            return "";
        }
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null && temp.length >= 1) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }

    public static String build(String origin, String charsetName) {
        if (origin == null)
            return null;

        StringBuilder sb = new StringBuilder();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("获取加密方式时发生错误");
            return null;
        }

        // 生成一组length=16的byte数组
        byte[] bs = digest.digest(origin.getBytes(Charset.forName(charsetName)));

        for (int i = 0; i < bs.length; i++) {
            int c = bs[i] & 0xFF; // byte转int为了不丢失符号位， 所以&0xFF
            if (c < 16) { // 如果c小于16，就说明，可以只用1位16进制来表示， 那么在前面补一个0
                sb.append("0");
            }
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    private static final String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h",

            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",

            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",

            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",

            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",

            "U", "V", "W", "X", "Y", "Z"};

    public static String build(String url) {
        if (url == null) {
            return null;
        }
        // 先得到url的32个字符的md5码
        String md5 = build(url, "UTF-8");
        StringBuilder sb = new StringBuilder();
        // 将32个字符的md5码分成4段处理，每段8个字符
        for (int i = 0; i < 4; i++) {
            int offset = i * 8;
            String sub = md5.substring(offset, offset + 8);
            long sub16 = Long.parseLong(sub, 16); // 将sub当作一个16进制的数，转成long
            // & 0X3FFFFFFF，去掉最前面的2位，只留下30位
            sub16 &= 0X3FFFFFFF;
            // 将剩下的30位分6段处理，每段5位
            for (int j = 0; j < 6; j++) {
                // 得到一个 <= 61的数字
                long t = sub16 & 0x0000003D;
                sb.append(chars[(int) t]);
                sub16 >>= 5; // 将sub16右移5位
            }
        }
        return sb.toString();
    }

    /**
     * 签名生成算法
     *
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @param secret 签名密钥
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(Map<String, String> params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            sb.append(param.getKey()).append("=").append(param.getValue());
        }
        sb.append(secret);

        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(sb.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }


//    public static boolean verifySignature(HashMap<String,String> params, String token) {
//        try {
//            String myToken = StringUtil.getSignature(params, Constant.SECRET);
//
//            if (token.equals(myToken)) {
//                return true;
//            }
//
//        } catch (IOException e) {
//            new BusinessException("500", "Get signature failed");
//        }

//        return false;
//    }

    public static String getDomain(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }

    /**
     * 把中文转成Unicode码
     *
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {//汉字范围 \u4e00-\u9fa5 (中文)
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
}
