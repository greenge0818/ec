package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.service.SystemOperationLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CAS SSO登录支持
 *
 * Created by Rolyer on 2016/5/2.
 */
@RestController
@RequestMapping("/api/passport")
public class PassportRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassportRestController.class);

    @Value("${casRestlet}")
    private String server;
    @Value("${casService}")
    private String service;

    @Resource
    private SystemOperationLogService systemOperationLogService;


    @ApiOperation(value = "获取CAS登录验证（地址）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号/手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码/动态密码", dataType = "string", paramType = "query")
    })
    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getVerifyAddress(String username, String password) {
        Result result = new Result();
        CasResponse res = getTicket(server, username, password, service);
        if (res.isSuccess() && StringUtils.isNoneBlank(res.getText())) {
            LOGGER.debug("ST: {}", res.getText());

            if (StringUtils.isNotBlank(res.getText())) {
                String url = service + "?ticket=" + res.getText();

                result.setCode(MessageTemplate.LOGIN_SUCCESS.getCode());
                result.setData(url);

                //添加登陆日志
                systemOperationLogService.insertLoginLog(username,password);

                return result;//ResponseEntity.ok().body(url);
            }
        }
        return matchResult(res);
    }

    private static final String MSG_ACCOUNT_NOT_FOUND = "AccountNotFound";
    private static final String MSG_INVALID_PASSWORD = "InvalidPassword";
    private static final String MSG_PASSWORD_EXPIRED = "PasswordExpired";
    private static final String MSG_ACCOUNT_LOCKED = "AccountLocked";

    private Result matchResult(CasResponse response){
        Result result = new Result();

        if (response==null || StringUtils.isBlank(response.getText())){
            result.setCode(MessageTemplate.UNKNOW_EXCEPTION.getCode());
            return result;
        }

        switch (response.getText()) {
            case MSG_ACCOUNT_NOT_FOUND:
                result.setCode(MessageTemplate.PHONE_UNREGISTER.getCode());
                return result;
            case MSG_INVALID_PASSWORD:
                result.setCode(MessageTemplate.LOGIN_PWD_ERROR.getCode());
                return result;
            case MSG_PASSWORD_EXPIRED:
                result.setCode(MessageTemplate.SMS_SEND_TIMEOUT.getCode());
                return result;
            case MSG_ACCOUNT_LOCKED: //账号锁定
               result.setCode(MessageTemplate.USER_ACCOUNT_LOCKED.getCode());
               return result;
            default:

                break;
        }

        result.setCode(MessageTemplate.UNKNOW_EXCEPTION.getCode());
        return result;
    }

    @ApiOperation(value = "验证用户是否登录")
    @RequestMapping(value = "/verify", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> userLoginVerification() {
        String account = UserUtils.getPrincipal();

        if (StringUtils.isNotBlank(account)) {
            return ResponseEntity.ok().body(true);
        }

        return ResponseEntity.ok().body(false);
    }

    /**
     * 获取CAS ST
     * @param server CAS Ticket RESTful API url
     * @param username 账号
     * @param password 密码
     * @param service 当前应用服务器地址
     * @return
     */
    public CasResponse getTicket(final String server, final String username,
                            final String password, final String service) {
        Assert.notNull(server, "server must not be null");
        Assert.notNull(username, "username must not be null");
        Assert.notNull(password, "password must not be null");
        Assert.notNull(service, "service must not be null");

        CasResponse response = getTicketGrantingTicket(server, username, password);
        if (response.isSuccess()) {
            return new CasResponse(true, getServiceTicket(server, response.getText(), service));
        } else {
            return response;
        }


    }

    /**
     * 获取CAS Service Ticket
     * @param server CAS Ticket RESTful API url
     * @param ticketGrantingTicket CAS TGT
     * @param service 当前应用服务器地址
     * @return
     */
    private String getServiceTicket(final String server,
                                    final String ticketGrantingTicket, final String service) {
        if (ticketGrantingTicket == null)
            return null;

        final HttpClient client = new HttpClient();

        final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);

        post.setRequestBody(new NameValuePair[]{new NameValuePair("service", service)});

        try {
            client.executeMethod(post);

            final String response = post.getResponseBodyAsString();

            switch (post.getStatusCode()) {
                case 200:

                    return response;
                default:
                    LOGGER.warn("Invalid text code (" + post.getStatusCode() + ") from CAS server!");
                    LOGGER.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));

                    break;
            }
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            post.releaseConnection();
        }

        return null;
    }

    /**
     * 获取CAS TGT
     * @param server CAS Ticket RESTful API url
     * @param username 账号
     * @param password 密码
     * @return
     */
    private CasResponse getTicketGrantingTicket(final String server,
                                           final String username, final String password) {
        final HttpClient client = new HttpClient();
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);
        final PostMethod post = new PostMethod(server);
        post.setRequestBody(new NameValuePair[]{
                new NameValuePair("username", username),
                new NameValuePair("password", password)});

        try {
            client.executeMethod(post);

            final String response = post.getResponseBodyAsString();

            switch (post.getStatusCode()) {
                case 201: {
                    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*")
                            .matcher(response);

                    if (matcher.matches()) {
                        return new CasResponse(true, matcher.group(1));
                    }

                    LOGGER.warn("Successful ticket granting request, but no ticket found!");
                    LOGGER.info("Response (1k): "
                            + response.substring(0, Math.min(1024, response.length())));
                    break;
                }
                case 400: {
                    return new CasResponse(false, response);
                }
                default:
                    LOGGER.warn("Invalid text code (" + post.getStatusCode()
                            + ") from CAS server!");
                    LOGGER.info("Response (1k): "
                            + response.substring(0, Math.min(1024, response.length())));
                    break;
            }
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            post.releaseConnection();
        }

        return null;
    }

    class CasResponse {
        private boolean success;
        private String text;

        public CasResponse(boolean success, String response) {
            this.success = success;
            this.text = response;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
