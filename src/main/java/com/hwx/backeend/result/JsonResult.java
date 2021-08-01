package com.hwx.backeend.result;

public class JsonResult {
    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private int serviceStatus;
    private Object data;

    public JsonResult(int serviceStatus, Object data) {
        this.serviceStatus = serviceStatus;
        this.data = data;
    }
}
