package com.soledede.exec;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soledede.exec.entity.Msg;

@RestController
public class ExecController {

    @RequestMapping("/rest")
    public Msg greeting(
            @RequestParam(value = "cmd") String cmd,
            @RequestParam(value = "type", defaultValue = "asyn") String type,
            @RequestParam(value = "name", defaultValue= "") String name) {
        try {
            if (cmd == null || cmd.trim().equalsIgnoreCase("")) return new Msg("cmd can't be null or ''", null, 1);
            String cmdString = "sh /data/shell/incIndex.sh";
            if (!cmd.trim().equalsIgnoreCase("status")) {
                if (cmd.trim().equalsIgnoreCase("mkdir")) {
                    if (name != null && !name.trim().equalsIgnoreCase("")) {
                        return ScalaProcess.execMkdir(name.trim());
                    }
                } else {
                    if (type.trim().equalsIgnoreCase("syn")) {
                        if (ScalaProcess.execStatus() == -1)
                            return ScalaProcess.exec(cmdString);
                        else return new Msg("正在处理中", null, 1);
                    } else {
                        // ScalaProcess.execAsyn(cmdString);
                        try {
                            ScalaProcess.eventQueue().put(cmd.trim());
                        } catch (InterruptedException e) {
                            return new Msg("正在处理...，请勿频繁提交", null, 1);
                        }
                        return new Msg("该请求为异步请求，请通过参数cmd=status来查看", null, 0);
                    }
                }
            } else if (cmd.trim().equalsIgnoreCase("status")) {
                if (!ScalaProcess.stdout().toString().equalsIgnoreCase("") || !ScalaProcess.stderr().toString().equalsIgnoreCase("")) {
                    Msg msg = new Msg(ScalaProcess.stdout().toString(), ScalaProcess.stderr().toString(), ScalaProcess.execStatus());
                    return msg;
                } else {
                    return new Msg("请输入命令开始执行", null, 0);
                }

            } else return new Msg("请输正确参数", null, 0);
        } catch (Exception e) {
            return new Msg(e.getMessage(), null, 1);
        }
        return new Msg("null", null, -1);
    }
}
