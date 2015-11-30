package com.soledede.exec.entity;

import java.util.List;

public class Msg {

    private String info;
    private String erro;
    private int code;

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public Msg() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Msg(String info, String erro, int code) {
        super();
        this.info = info;
        this.code = code;
        this.erro = erro;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
