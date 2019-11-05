package com.sjjd.wyl.printtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by wyl on 2017/11/17.
 */

public class FileUtils {
    public static final String TAG = "FileUtils";
    public static final String PATH_DOWNLOAD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    //签名根目录
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() +"/sjjd";
    public static String picturePath = PATH_ROOT +"Pictures";
    public static String musicPath = PATH_ROOT + "/SignatureMusic";
    public static String videoPath = PATH_ROOT + "/SignatureVideo";
    public static String prizePath = PATH_ROOT + "/SignaturePrize";
    static private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Bitmap pathToBitmap(String path) {
        if (path == null || path.length() < 1)
            return null;
        return BitmapFactory.decodeFile(path);
    }

    public static boolean clearAllPictures() {
        File mFile = new File(picturePath);
        File[] mFiles = mFile.listFiles();
        for (File f : mFiles) {
            if (f.exists()) {
                f.delete();
            }
        }
        return true;
    }

    public static void createDirs(String uPath) {
        File dir_picture = new File(uPath);
        if (!dir_picture.exists())
            dir_picture.mkdirs();
       /* File dir_video = new File(videoPath);
        if (!dir_video.exists())
            dir_video.mkdirs();
        File dir_music = new File(musicPath);
        if (!dir_music.exists())
            dir_music.mkdirs();

        File dir_prize = new File(prizePath);
        if (!dir_prize.exists()) {
            dir_prize.mkdirs();
        }*/

    }


    public static String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }

    }

    public static File saveFile(Bitmap bitmap) {
        int mCount = bitmap.getByteCount();
        Log.e("====", "saveFile: " + mCount + "   " + getDataSize(mCount));
        File dir=new File(picturePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File mFile = new File(picturePath + "/" + mDateFormat.format(new Date(System.currentTimeMillis())) + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFile);
            boolean isSaved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (isSaved) {
                return mFile;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static File saveFile2U(Bitmap bitmap, String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        if (!dir.exists()) {
            return null;
        }
        File mFile = new File(path + "/" + mDateFormat.format(new Date(System.currentTimeMillis())) + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFile);
            boolean isSaved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (isSaved) {
                return mFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;

    }

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("background_path", Context.MODE_PRIVATE);

    }

    public static String getBackground(Context context) {
        String mPath = getSharePreference(context).getString("background_path", null);
        return mPath;
    }

    public static void clearBackground(Context context) {
        getSharePreference(context).edit().putString("background_path", null).apply();
    }

    public static void setBackground(Context context, Bitmap bitmap) {

        File mFile;
        Bitmap mBitmap = null;
        if (bitmap == null) {
            return;
        }

        mFile = new File(PATH_DOWNLOAD + "/background.JPEG");
        if (mFile.exists()) {
            mFile.delete();
        }
        Log.e(TAG, "saveBackgroundBitmap: " + mFile.getAbsolutePath());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFile);
            mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSharePreference(context).edit()
                .putString("background_path", mFile.getAbsolutePath())
                .apply();
    }

    public static String isUExist(Context context) {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (manager == null)
            return "";

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice usbDevice = deviceIterator.next();
            int deviceClass = usbDevice.getDeviceClass();
            if (deviceClass == 0) {
                UsbInterface anInterface = usbDevice.getInterface(0);
                int interfaceClass = anInterface.getInterfaceClass();
                //http://blog.csdn.net/u013686019/article/details/50409421
                //http://www.usb.org/developers/defined_class/#BaseClassFFh
                //通过下面的InterfaceClass 来判断到底是哪一种的，例如7就是打印机，8就是usb的U盘
                if (anInterface.getInterfaceClass() == 8) {
                    return getUPath();
                }
            }
        }
        return null;
    }

    public static String getUPath() {
        try {
            // 运行mount命令，获取命令的输出，得到系统中挂载的所有目录
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("proc") || line.contains("tmpfs") || line.contains("media") || line.contains("asec") || line.contains("secure") || line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data") || line.contains("shell") || line.contains("root") || line.contains("acct") || line.contains("misc") || line.contains("obb")) {
                    continue;
                }
                // 下面这些分区是我们需要的
                if (line.contains("fat") || line.contains("fuse") || (line.contains("ntfs"))) {
                    // 将mount命令获取的列表分割，items[0]为设备名，items[1]为挂载路径
                    String items[] = line.split(" ");
                    if (items.length > 0) {
                        String path = items[1];//.toLowerCase(Locale.getDefault());
                        // 添加一些判断，确保是sd卡，如果是otg等挂载方式，可以具体分析并添加判断条件
                        //   LogUtils.e(TAG, "getUPath: " + items[0] + "   " + items[1]+ "   " + items[2]+ "   " + items[3]+ "   " + items[4]+ "   " + items[5]);
                        //  Rk3188   /storage/361A-28B5
                        //  Q-E10   /mnt/usb_storage
                        if (path.contains("usb_storage") || (path.contains("storage") && !path.contains("emulated"))) {
                            return path;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}



