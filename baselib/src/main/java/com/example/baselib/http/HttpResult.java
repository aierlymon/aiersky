package com.example.baselib.http;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 这个是作为如果有相同的数据请求返回，可以外面再包一层
 */
public class HttpResult<T> {

    private T Data;

    private RspMsgHeader msgHeader;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public RspMsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(RspMsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public static class RspMsgHeader{
        int type;//状态码（0成功 1失败）
        String code;//错误码（0成功 其他错误码）
        String msg;//状态信息（若非约定错误码，请显示状态信息提示）

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "Data=" + Data +
                ", msgHeader=" + msgHeader +
                '}';
    }
}
