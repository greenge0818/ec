package com.prcsteel.ec.controller;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.model.RestResult;
import com.prcsteel.ec.core.service.FileService;
import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.core.util.FileUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.service.CommonService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.prcsteel.ec.dto.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @ClassName: CommonController
 * @Description: 公共的controllor
 * @Author Tiny
 * @Date 2016年4月28日
 */
@Controller
@RequestMapping("/common")
public class CommonController {
    @Resource
    CommonService commonService;

    @Resource
    private FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private static final Gson gson = new Gson();

    /**
     * @Author: Tiny
     * @Description: 上传文件
     * @Date: 2016年4月28日
     */
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("uploadFile");
        try {
            String url = gson.toJson(new Result(MessageTemplate.UPLOAD_FILE_SUCCESS.getCode(),
                    commonService.uploadFile(file, CookieUtil.getCookieId(request), StringUtil.getDomain(request)), ResultMsgType.BUSINESS));
            try {
                return new String(url.getBytes("utf-8"), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                logger.error("Encoding failed!", e);
            }
        } catch (BusinessException ex) {
            return gson.toJson(new Result(ex.getCode()));
        }
        return null;
    }

    /**
     * @Author: Green.Ge
     * @Description: 上传文件（所有类型）
     * @Date: 2016年6月23日
     */
    @RequestMapping(value = "/uploadfileall", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFileAll(MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("uploadFile");
        try {
            return new Result(MessageTemplate.UPLOAD_FILE_SUCCESS.getCode(),
                    commonService.uploadFileAll(file, StringUtil.getDomain(request)), ResultMsgType.BUSINESS);
        } catch (BusinessException ex) {
            return new Result(ex.getCode());
        }
    }

    /**
     * 文件下载，如果是图片则自动设置content type为image
     *
     * @param response
     * @param key
     */
    @RequestMapping("/getfile")
    public void getFile(HttpServletResponse response, String key) {
        InputStream inStream = null;
        OutputStream ostream;
        try {
            inStream = fileService.getFileData(key);
            ostream = response.getOutputStream();

            // set content type
            String suffix = FileUtil.getFileSuffix(key).toLowerCase();
            if (Constant.IMAGE_SUFFIX.contains("[" + suffix + "]")) {
                response.setContentType("image/" + suffix);
            } else {
                //文件下载
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtil.getFileNameAll(key));
            }

            StreamUtils.copy(inStream, ostream);
        } catch (Exception e) {
            if (e.getClass().getName().contains("ClientAbortException")) {
                return;
            }
            logger.error("read file failed!", e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * @Author: Tiny
     * @Description: 获取掌柜指数
     * @Date: 2016年07月07日
     */
    @ResponseBody
    @RequestMapping(value = "/getpriceindexes", method = RequestMethod.GET)
    @ApiOperation("获取掌柜指数")
    public RestResult getPriceIndexes(HttpServletRequest request) {
        try {
            return commonService.getPriceIndexes(StringUtil.getCity(request));
        } catch (BusinessException e) {
            return new RestResult(e.getCode(), new ArrayList<>());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 获取广告
     * @Date: 2016年07月07日
     */
    @ResponseBody
    @RequestMapping(value = "/getad", method = RequestMethod.GET)
    @ApiOperation("获取广告")
    public RestResult getAd() {
        try {
            return commonService.getAd();
        } catch (BusinessException e) {
            return new RestResult(e.getCode(), new ArrayList<>());
        }
    }

    /**
     * @Author: Rabbit
     * @Description: 获取悬浮广告
     */
    @ResponseBody
    @RequestMapping(value = "/getfloatad", method = RequestMethod.GET)
    @ApiOperation("获取悬浮广告")
    public RestResult getFloatAd() {
        try {
            return commonService.getFloatAd();
        } catch (BusinessException e) {
            return new RestResult(e.getCode(), null);
        }
    }

    /**
     * @Author: Tiny
     * @Description: 获取会员活动
     * @Date: 2016年07月07日
     */
    @ResponseBody
    @RequestMapping(value = "/getactivities", method = RequestMethod.GET)
    @ApiOperation("获取会员活动")
    public RestResult getActivities() {
        try {
            return commonService.getActivities();
        } catch (BusinessException e) {
            return new RestResult(e.getCode(), new ArrayList<>());
        }
    }
}
