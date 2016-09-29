package com.prcsteel.ec.controller;

import com.google.gson.Gson;
import com.prcsteel.ec.core.model.AMQMessage;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.cas.CasUser;
import com.prcsteel.ec.model.domain.ec.Demo;
import com.prcsteel.ec.service.ActiveMQService;
import com.prcsteel.ec.service.ActiveMQSinkService;
import com.prcsteel.ec.service.DemoService;
import com.prcsteel.ec.service.api.RestDemoService;
import com.prcsteel.ec.service.cas.CasUserService;
import com.prcsteel.ec.validator.DemoValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @Resource
    private DemoService demoService;

    @Resource
    private CasUserService casUserService;

    @Resource
    private RestDemoService restDemoService;

    @Resource
    private ActiveMQService activeMQService;

    @Resource
    private ActiveMQSinkService activeMQSinkService;

    @RequestMapping("")
    public void demo(ModelMap out) {
        String city = restDemoService.fetchCity();
        out.put("message", "Data List:");
        out.put("city", StringUtils.isNotBlank(city) ? new Gson().fromJson(city, Object.class) : null);
    }

    @RequestMapping("login")
    public String login() {
        return "/demo/login";
    }

    @RequestMapping("loginDiagram")
    public String loginDiagram() {
        return "/demo/loginDiagram";
    }

    @RequestMapping("user")
    public void user(ModelMap out, String id) {

        Demo demo = demoService.queryUserById(Integer.parseInt(id));

        out.put("user", demo);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public @ResponseBody Result add(@Valid Demo demo, BindingResult br) {
        Result result = new Result();

        //run Spring validator manually
        new DemoValidator().validate(demo, br);

        if (!br.hasErrors()) {
            demoService.add(demo);
            casUserService.add(new CasUser(UUID.randomUUID().toString().substring(1, 10), "pwd", "dpwd", new Date(), "Y", "N", "N", new Date(), new Date()));
        } else {
            result.setCode(br.getAllErrors().get(0).getDefaultMessage());
        }

        return result;
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    public @ResponseBody Result del(Demo demo) {
        Result result = new Result();

        demoService.delete(demo);

        return result;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public @ResponseBody Result edit(Demo demo) {
        Result result = new Result();

        demoService.update(demo);
        return result;
    }

    @RequestMapping(value = "load", method = RequestMethod.POST)
    public @ResponseBody Result load(Demo demo, int pageNum, int pageSize, String... orderBys) {
        Result result = new Result();

        result.setData(demoService.query(demo, pageNum, pageSize, orderBys));

        return result;
    }

    @RequestMapping(value = "ajaxLogin", method = RequestMethod.POST)
    public @ResponseBody Result ajaxLogin(String username, String password) {
        Result result = new Result();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);

        return result;
    }

    @RequestMapping("message")
    public void send() {
    }

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public @ResponseBody Result send(Object o) {
        Result result = new Result();
        LOGGER.debug("Sending {}", o);
        activeMQService.send(o);
        return result;
    }

    @RequestMapping("fetch")
    public  @ResponseBody Result fetch() {
        Result result = new Result();

        List<AMQMessage> messages = activeMQSinkService.getMessages();

        result.setData(messages);
        return result;
    }
}
