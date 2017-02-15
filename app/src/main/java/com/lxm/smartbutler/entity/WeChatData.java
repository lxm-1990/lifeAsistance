package com.lxm.smartbutler.entity;

/**
 * Created by lxm on 17/2/15.
 */
//        "id":"wechat_20170215030228",
//        "title":"幸福真的跟钱没有关系！",
//        "source":"励志爱情语录",
//        "firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-12898090.jpg/640",
//        "mark":"",
//        "url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20170215030228"


public class WeChatData {

    private String title;
    private String source;
    private String firstImg;
    private String url;

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
