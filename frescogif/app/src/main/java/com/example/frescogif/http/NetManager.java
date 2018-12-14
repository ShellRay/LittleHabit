package com.example.frescogif.http;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description
 *
 * @author Xue Wenchao
 * @version 1.0.0
 * @modify
 */
public class NetManager {
    private HttpConfig mHttpConfig;

    private static NetManager instance = new NetManager();

    public static NetManager getInstance() {
        return instance;
    }

    private NetManager() {

    }

    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.mHttpConfig = httpConfig;
    }
}
