package com.example.caiyue.androidstuiodemo.model;

public class BaseResponse<T> {
    private Error err;
    private T data;

    public BaseResponse(Error error, T data) {
        this.err = error;
        this.data = data;
    }

    public Object getError() {
        return err;
    }

    public T getData() {
        return data;
    }

    public void setErr(Error err) {
        this.err = err;
    }


    public void setData(T data) {
        this.data = data;
    }
    public class Error{
        private String code;
        private  String message;

        public void setCode(String code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "code=" + code + "message" + message;
        }
    }
}
