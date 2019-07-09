package com.example.caiyue.androidstuiodemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static android.os.Environment.isExternalStorageRemovable;

public class DisklrucacheHelper {
    private static Context context;
    private static final int MAX_SIZE = 10 * 1024 * 1024;//10MB
    private static DiskLruCache diskLruCache;
    public DisklrucacheHelper(Context context) {
        this.context = context;
        this.initDiskLruCache();
    }
    /**
     * Get a usable cache directory (external if available, internal otherwise).
     * external：如：/storage/emulated/0/Android/data/package_name/cache
     * internal 如：/data/data/package_name/cache
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||!isExternalStorageRemovable()
                ? context.getExternalCacheDir().getPath()
                : context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
    // 初始化 DiskLruCache
    public void initDiskLruCache() {
        if (diskLruCache == null || diskLruCache.isClosed()) {
            try {
                File cacheDir = getDiskCacheDir(context, "CacheDir");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                //初始化DiskLruCache
                diskLruCache = DiskLruCache.open(cacheDir, 1, 1, MAX_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    // 写入缓存
    public boolean setCache(String path, InputStream inputStream) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(hashKeyForDisk(path));
            OutputStream outputStream = editor.newOutputStream(0);
            BufferedInputStream in = new BufferedInputStream(inputStream);
            BufferedOutputStream ou = new BufferedOutputStream(outputStream);
            int read;
            while ((read = in.read()) != -1) {
                ou.write(read);
            }
            editor.commit();
            in.close();
            ou.close();
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // 读取缓存
    public InputStream getCache(String path) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(hashKeyForDisk(path));

            if (snapshot != null) {
                Log.i("wel",  "true");
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void clearCache(String path) {
        String key = hashKeyForDisk(path);
        try {
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public long getCacheSize() {
        return diskLruCache.size();
    }

    public void deleteAll() {
        /**
         * 这个方法用于将所有的缓存数据全部删除
         * 其实只需要调用一下DiskLruCache的delete()方法就可以实现了。
         * 会删除包括日志文件在内的所有文件
         */
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeDiskLruCache(){
        try {
            diskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    public interface CacheCallback{
        void success(String key);
    }
}
