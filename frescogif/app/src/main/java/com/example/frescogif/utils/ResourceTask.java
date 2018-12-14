package com.example.frescogif.utils;

import android.os.Environment;
import android.util.Log;

import com.example.frescogif.http.HttpConfig;
import com.example.frescogif.http.NetManager;
import com.example.frescogif.http.SignHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class ResourceTask
{

    public static class EffectFile
    {
        public final String ziptype;

        public final String version;

        public final String gift_id;

        public final String fileurl;

        public EffectFile(String ziptype, String version, String gift_id, String fileurl)
        {
            this.ziptype = ziptype;
            this.version = version;
            this.gift_id = gift_id;
            this.fileurl = fileurl;
        }
    }

    private List<EffectFile> filelist = null;
    static  ResourceTask     instance = null;

    public static void intialize()
    {
        if (instance == null)
        {
            instance = new ResourceTask();
        }
    }

    public static ResourceTask getInstance()
    {
        return instance;
    }

    private ResourceTask()
    {
        filelist = new ArrayList<>();

        new Thread()
        {
            @Override
            public void run()
            {
                work();
            }
        }.start();

    }

    private void work()
    {

        // 获取大礼物特效列表
        String present = null;

        while (present == null)
        {
            present = execute("gift");
        }

      /*  //获取主播等级特效列表
        String anlevel = null;

        while (anlevel == null)
        {
            anlevel = execute("anlevel");
        }

        //获取主播勋章特效列表
        String anchorMedal = null;

        while (anchorMedal == null)
        {
            anchorMedal = execute("amedal");
        }
*/

        try
        {
            filelist.add(new EffectFile("0", "0", "1", "https://github.com/yyued/SVGA-Samples/blob/master/halloween.svga?raw=true"));

//            fill(filelist, present);
//            fill(filelist, anlevel);
//            fill(filelist, anchorMedal);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int datasize = filelist.size(), position = 0;

        while (position < datasize)
        {
            EffectFile sub = filelist.get(position);
            String localpath = getLocalPath(sub.version, sub.gift_id);
            String localtemp = getLocalTemp(sub.version, sub.gift_id);

            if (new File(localpath).exists())
            {
                position++;
            }
            else if (download(sub.fileurl, localtemp))
            {
                Log.e("shell","网络下载资源成功");
                new File(localtemp).renameTo(new File(localpath));

               /* if (EffectZip.needDecompress(localpath))
                {
                    EffectZip.decompressFile(localpath);
                }*/
                position++;
            }
        }

    }

    private void fill(List<EffectFile> list, String text) throws Exception
    {
        JSONObject json = new JSONObject(text);
        json = json.getJSONObject("content");
        JSONArray array = json.getJSONArray("list");

        int length = array.length();

        for (int i = 0; i < length; i++)
        {
            final JSONObject item = array.getJSONObject(i);
            String ziptype = item.getString("type");

            final JSONArray zips = item.getJSONArray("zips");
            int datasize = zips.length();

            for (int j = 0; j < datasize; j++)
            {
                final JSONObject zip = zips.getJSONObject(j);
                Iterator<String> keys = zip.keys();

                String version = "";
                String gift_id = "";
                String fileurl = "";

                while (keys.hasNext())
                {
                    String key = keys.next();

                    if (key.equals("ver"))
                    {
                        version = zip.getString(key);
                    }
                    else
                    {
                        fileurl = zip.getString(key);
                        gift_id = key;
                    }
                }

                list.add(new EffectFile(ziptype, version, gift_id, fileurl));
            }
        }

    }

    private String getReqUrl(String fields)
    {
        HashMap<String, String> params = new HashMap<>();
        long time = System.currentTimeMillis();
        params.put("r", String.valueOf(time));
        params.put("fields", String.valueOf(fields));
        SignHelper.addSign(params);

        StringBuffer builder = new StringBuffer();
//        builder.append(ApiConstant.URL_GIFT_ENTER);
        builder.append("?");

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, String> en = iterator.next();
            builder.append(en.getKey());
            builder.append("=");
            builder.append(en.getValue());

            if (iterator.hasNext())
            {
                builder.append("&");
            }
        }

        return builder.toString();
    }

    private String readText(HttpURLConnection connection) throws Exception
    {
        int responseCode = connection.getResponseCode();
        StringBuilder builder = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null)
            {
                builder.append(line);
            }

            br.close();
        }

        return builder.toString();
    }

    private String execute(String fields)
    {

        HttpURLConnection connection = null;
        String result = null;

        try
        {
            String url = "https://github.com/yyued/SVGA-Samples/blob/master/halloween.svga?raw=true";//getReqUrl(fields);

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setRequestMethod("GET");

            HttpConfig config = NetManager.getInstance().getHttpConfig();
            Map<String, String> header = config != null ? config.getHeader() : null;

            if (header != null)
            {
                Set<Map.Entry<String, String>> set = header.entrySet();
                for (Map.Entry<String, String> entry : set)
                {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            result = readText(connection);
            connection.disconnect();
        }
        catch (Exception e)
        {
            result = null;
        }

        if (connection != null)
        {
            connection.disconnect();
        }

        return result;
    }

    public boolean isAvaliable(String id)
    {
        String filepath = getFilePath(id);
        return new File(filepath).exists();
    }

    public String getFilePath(String id)
    {
        EffectFile find = null;
        int size = filelist.size();

        for (int i = 0; i < size; i++)
        {
            EffectFile file = filelist.get(i);

            if (file.gift_id.equals(id))
            {
                find = file;
                break;
            }
        }

        return find != null ? getLocalPath(find.version, find.gift_id) : "";
    }

    /**
     * 下载网络文件
     *
     * @param url      网络文件地址
     * @param savepath 本地存储位置
     * @return
     */
    private boolean download(String url, String savepath)
    {
        HttpURLConnection connect = null;
        FileOutputStream fos = null;
        InputStream sis = null;
        boolean success = true;

        File file = new File(savepath);
        File dir = file.getParentFile();

        if (!dir.exists())
        {
            dir.mkdirs();
        }

        int responseCode = 0;

        try
        {

            connect = (HttpURLConnection) new URL(url).openConnection();
            connect.setReadTimeout(10 * 1000);
            connect.setConnectTimeout(15 * 1000);
            connect.setRequestMethod("GET");
            connect.setDoInput(true);
            connect.setRequestProperty("Accept-Encoding", "identity");

            if (file.length() > 0)
            {
                connect.setRequestProperty("Range", "bytes=" + file.length() + "-");
            }

            connect.connect();
            responseCode = connect.getResponseCode();

            if (responseCode == 200 || responseCode == 206)
            {
                sis = connect.getInputStream();
                fos = new FileOutputStream(savepath, true);

                byte[] buffer = new byte[20480];
                long recvsize = 0;
                int readsize = 0;

                while ((readsize = sis.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, readsize);
                    fos.flush();
                    recvsize = recvsize + readsize;
                }

                sis.close();
                fos.close();
            }
            else
            {
                success = false;
            }
        }
        catch (Exception e)
        {
            success = false;
        }


        try
        {
            if(sis != null){
                sis.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (connect != null)
        {
            connect.disconnect();
        }

        if (responseCode == 416)
        {
            file.delete();
        }

        return success;

    }

    /**
     * 获取本地存储路径
     *
     * @param version 大礼物版本号
     * @param gift_id 大礼物id
     * @return
     */
    private String getLocalPath(String version, String gift_id)
    {
        String filepath = Environment.getExternalStorageDirectory() + "/fresco/zip/" + version + "/" + gift_id + ".zip";
        return filepath;
    }

    /**
     * 获取下载临时路径
     *
     * @param version 大礼物版本号
     * @param gift_id 大礼物id
     * @return
     */
    private String getLocalTemp(String version, String gift_id)
    {
        String filepath = Environment.getExternalStorageDirectory() + "/fresco/zip/" + version + "/" + gift_id + ".tmp";
        return filepath;
    }


}
