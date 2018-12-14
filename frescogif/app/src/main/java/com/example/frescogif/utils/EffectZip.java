package com.example.frescogif.utils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by yuanwei on 17/3/23.
 */
public class EffectZip
{

    private static String getOutputDir(String zippath)
    {
        int index = zippath.lastIndexOf(".");

        String dir_path = zippath;

        if (index > 0)
        {
            dir_path = zippath.substring(0, index);
        }

        File dir = new File(dir_path);

        if (!dir.exists())
        {
            dir.mkdirs();
        }

        return dir_path;
    }

    private static String getPlistPath(String zippath)
    {
        return getOutputDir(zippath) + "/plist.json";
    }

    public static List<String> getFileList(String zippath)
    {
        List<String> list = new ArrayList();

        try
        {
            String filepath = getPlistPath(zippath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();

            JSONArray array = new JSONArray(builder.toString());

            int length = array.length();

            for (int i = 0; i < length; i++)
            {
                list.add(array.getString(i));
            }
        }
        catch (Exception e)
        {
            list.clear();
        }

        return list;
    }

    public static boolean needDecompress(String zippath)
    {
        List<String> list = getFileList(zippath);

        boolean result = false;

        if (list.size() > 0)
        {
            for (String string : list)
            {
                if (!new File(string).exists())
                {
                    result = true;
                    break;
                }
            }
        }
        else
        {
            result = true;
        }

        return result;
    }

    public static boolean decompressFile(String zippath)
    {
        boolean success = true;

        try
        {

            List<String> filelist = new ArrayList<String>();
            final String dir_path = getOutputDir(zippath);


            FileInputStream fis = new FileInputStream(zippath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry en = null;

            while ((en = zis.getNextEntry()) != null)
            {
                if (!en.isDirectory())
                {
                    String name = en.getName();
                    int start = name.lastIndexOf("/");

                    if (start < 0)
                    {
                        start = name.lastIndexOf("\\");
                    }

                    if (start > 0)
                    {
                        name = name.substring(start + 1);
                    }

                    String savepath = dir_path + "/" + name;

                    if (!new File(savepath).exists())
                    {
                        int readlen = 0;
                        byte[] buff = new byte[102400];

                        FileOutputStream fos = new FileOutputStream(savepath);
                        while ((readlen = zis.read(buff)) > 0)
                        {
                            fos.write(buff, 0, readlen);
                            fos.flush();
                        }
                        fos.close();
                    }

                    if (!filelist.contains(savepath))
                    {
                        filelist.add(savepath);
                    }
                }
            }

            zis.close();

            Comparator<String> cmp = new Comparator<String>()
            {
                @Override
                public int compare(String lhs, String rhs)
                {
                    int lenl = lhs.length();
                    int lenr = rhs.length();

                    if (lenl == lenr)
                    {
                        return lhs.compareTo(rhs);
                    }

                    return lenl - lenr;
                }
            };

            Collections.sort(filelist, cmp);

            JSONArray array = new JSONArray();

            for (String path : filelist)
            {
                array.put(path);
            }

            String jsonpath = getPlistPath(zippath);
            FileOutputStream fos = new FileOutputStream(jsonpath);
            fos.write(array.toString().getBytes());
            fos.flush();
            fos.close();
        }
        catch (Exception e)
        {
            success = false;
        }

        return success;
    }

}
