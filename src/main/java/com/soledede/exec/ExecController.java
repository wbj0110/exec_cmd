package com.soledede.exec;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soledede.exec.entity.Msg;

@RestController
public class ExecController {

    @RequestMapping("/rest")
    public Msg greeting(
            @RequestParam(value = "cmd") String cmd, @RequestParam(value = "user", defaultValue = "") String user) {
        try {
            if (cmd == null || cmd.trim().equalsIgnoreCase("")) return new Msg("cmd can't be null or ''", 1);
            String cmdString = null;
            if (cmd.equalsIgnoreCase("exec")) cmdString = "sh /data/shell/incIndex.sh";
            if (!user.equalsIgnoreCase("soledede") && cmdString == null) return new Msg("forbidden", 1);
            if (cmdString == null) cmdString = cmd;
            Integer r = ScalaProcess.exec(cmdString);
            if (r == 0)
                return new Msg("sucesess", 0);
            else if (r == 1)
                return new Msg("failed", 1);
        } catch (Exception e) {
            return new Msg(e.getMessage(), 1);
        }
        return new Msg("sucesess", 0);
    }
}
