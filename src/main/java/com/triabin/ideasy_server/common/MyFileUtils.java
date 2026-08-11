package com.triabin.ideasy_server.common;

import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 类描述：自定义文件工具类
 *
 * @author Triabin
 * @date 2025-12-11 14:40:53
 */
public class MyFileUtils {
    /**
     * 方法描述：获取文件类型
     *
     * @param path 要获取MimeType的文件路径
     * @return {@link String} MimeType
     * @throws IOException 文件读取异常
     * @date 2025-12-11 14:43:10
     */
    public static String getMimeType(String path) throws IOException {
        File file = new File(path);
        Tika tika = new Tika();
        return tika.detect(file);
    }

    /**
     * 方法描述：获取文件类型
     *
     * @param file 要获取MimeType的文件对象
     * @return {@link String} MimeType
     * @throws IOException 文件读取异常
     * @date 2025-12-11 14:46:15
     */
    public static String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }

    /**
     * 方法描述：复制文件
     * @param src {@link File} 要复制的文件
     * @param dest {@link File} 复制目标位置
     * @throws IOException 文件操作异常
     */
    public static void copy(File src, File dest) throws IOException {
        File parentFile = dest.getParentFile();
        if (dest.exists()) {
            // 如果目标文件已存在，则放弃拷贝
            throw new IOException("File already exists: " + dest);
        } else {
            // 确保目标路径父路径存在
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new IOException("Could not create directory: " + parentFile);
            }
        }
        if (src.isFile()) {
            // 如果是文件，则直接将文件内容写入目标路径
            try (FileInputStream is = new FileInputStream(src);
                 FileOutputStream os = new FileOutputStream(dest)) {
                byte[] buffer = new byte[5 * 1024 * 1024]; // 缓冲区，设定为5MB
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
        } else  {
            if (!dest.exists() && !dest.mkdirs()) {
                throw new IOException("Could not create directory: " + dest);
            }
            if (!dest.isDirectory()) {
                throw new IOException("Not a directory: " + dest);
            }
            File[] files = src.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File file : files) {
                copy(file, new File(dest, file.getName()));
            }
        }
    }

    /**
     * 方法描述：将要序列化的对象序列化到指定文件中
     *
     * @param objs {@link Collection} 要序列化的对象集合
     * @param path {@link String} 序列化文件路径
     * @return {@code boolean} 序列化结果
     * @throws IOException 文件操作异常
     */
    public static <T> boolean serialize(Collection<T> objs, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            // 不存在则创建文件
            File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new IOException("Cannot create directory: " + parentFile.getAbsolutePath());
            }
        } else if (file.isDirectory()) {
            throw new IOException("File '" + path + "' exists but is a directory");
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(objs);
        oos.close();
        return true;
    }

    /**
     * 方法描述：返序列化对象到内存中并删除序列化文件
     *
     * @param path  {@link String} 序列化文件路径
     * @param clazz {@link Class} 反序列化后的对象类型，例`ArrayList.class`
     * @return {@link T} 反序列化类型
     * @throws IOException            文件操作异常
     * @throws ClassNotFoundException 类型转化异常
     */
    public static <T> T deserialize(String path, Class<T> clazz) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Object o = ois.readObject();
        ois.close();
        try {
            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            System.err.printf("序列化文件【%s】删除失败，错误信息：%s%n", path, e.getMessage());
        }
        return clazz.cast(o);
    }

    /**
     * 方法描述：解压zip压缩包到指定路径
     *
     * @param zipFile {@link File} 压缩包文件
     * @param destDir {@link File} 解压目标路径
     * @throws IOException 文件操作异常
     */
    public static void unzip(File zipFile, File destDir) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.printf("开始将【%s】解压到【%s】%n", zipFile.getAbsolutePath(), destDir.getAbsolutePath());
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new IOException("Cannot create directory: " + destDir.getAbsolutePath());
        }
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File dest = new File(destDir, entry.toString());
                if (entry.isDirectory() && !dest.exists()) {
                    // 文件夹的处理方案：直接按照层级创建文件夹即可
                    dest.mkdirs();
                } else {
                    // 文件的处理方案：将文件写入到目标路径
                    try (OutputStream os = new FileOutputStream(dest)) {
                        byte[] buffer = new byte[2 * 1024 * 1024]; // 2MB缓冲区
                        int len;
                        while ((len = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                        }
                    } finally {
                        // 关闭当前entry，表示压缩包中的一个文件处理结束
                        zis.closeEntry();
                    }
                }
            }
        }
        System.out.printf("【%s】解压到【%s】完成，耗时：%d毫秒%n",
                zipFile.getAbsolutePath(), destDir.getAbsolutePath(),
                System.currentTimeMillis() - startTime);
    }

    /**
     * 方法描述：将文件（夹）压缩到指定文件
     *
     * @param file    {@link File} 要压缩的文件
     * @param zipFile {@link File} 压缩到的文件
     * @throws IOException 文件操作异常
     */
    public static void zip(File file, File zipFile) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.printf("开始将【%s】压缩到【%s】%n", file.getAbsolutePath(), zipFile.getAbsolutePath());
        if (!file.exists()) {
            throw new IllegalArgumentException("Not exists: " + file.getAbsolutePath());
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            toZip(file, zos, file.getName());
        }
        System.out.printf("【%s】压缩到【%s】完成，耗时：%d毫秒%n",
                file.getAbsolutePath(), zipFile.getAbsolutePath(),
                System.currentTimeMillis() - startTime);
    }

    /**
     * 方法描述：将文件（夹）压缩到同级文件夹下
     *
     * @param file {@link File} 要压缩的文件
     * @throws IOException 文件操作异常
     */
    public static void zip(File file) throws IOException {
        String name = file.getName();
        String zipName = file.isDirectory() ? name : name.substring(0, name.lastIndexOf('.'));
        zip(file, new File(file.getParentFile(), zipName + ".zip"));
    }

    /**
     * 方法描述：获取文件（夹）下的每一个文件，将其变成ZipEntry对象，放入压缩包中
     *
     * @param file {@link File} 要压缩的文件（夹）
     * @param zos  {@link ZipOutputStream} 压缩文件输出流
     * @param name {@link String} 压缩包内部的父级路径
     */
    private static void toZip(File file, ZipOutputStream zos, String name) throws IOException {
        if (file.isFile()) {
            fileToZipEntry(file, zos, name);
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                // 如果是文件，将其变成ZipEntry对象，放入压缩包中
                fileToZipEntry(f, zos, name + File.separator + f.getName());
            } else {
                // 如果是文件夹则修改压缩包内部父级路径后递归
                toZip(f, zos, name + File.separator + f.getName());
            }
        }
    }

    /**
     * 方法描述：工具方法，将指定文件中的数据写入到{@link ZipEntry}中
     *
     * @param file      {@link File} 要写入的文件
     * @param zos       {@link ZipOutputStream} 压缩包文件输出流
     * @param entryName {@link String} ZipEntry的名字
     * @throws IOException 文件操作异常
     */
    private static void fileToZipEntry(File file, ZipOutputStream zos, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[2 * 1024 * 1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
        }
        zos.closeEntry();
    }

    /**
     * 方法描述：计算指定文件的MD5值
     * @param path {@link String} 要计算的文件路径
     * @return {@link String} 十六进制MD5
     * @throws IOException 文件读取异常
     * @throws NoSuchAlgorithmException 算法名称异常
     */
    public static String md5(String path) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            // 转为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }
}
