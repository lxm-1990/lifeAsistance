package com.lxm.smartbutler.entity;
import java.util.List;

/**
 * Created by lxm on 17/2/14.
 */

public class CourierData {
    private String resultcode;
    private String reason;
    private Result result;
    private int error_code;

    public class Result {
        private String company;
        private String no;
        private String status;
        private List<CourierItem> list;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public List<CourierItem> getList() {
            return list;
        }

        public void setList(List<CourierItem> list) {
            this.list = list;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class CourierItem {
        private String datetime;
        private String remark;
        private String zone;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }
}
