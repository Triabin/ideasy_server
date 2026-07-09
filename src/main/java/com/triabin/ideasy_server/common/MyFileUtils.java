package com.triabin.ideasy_server.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 类描述：自定义文件工具类
 *
 * @author Triabin
 * @date 2025-12-11 14:40:53
 */
public class MyFileUtils {

    private static final Logger logger = LogManager.getLogger(MyFileUtils.class);

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
     *
     * @param srcPath    要复制的文件路径
     * @param targetPath 复制目标路径
     * @return {@code boolean} 操作结果
     * @throws IOException 文件操作异常
     * @date 2026-02-26 14:29:41
     */
    public static boolean copy(String srcPath, String targetPath) throws IOException {
        if (srcPath == null || targetPath == null || srcPath.isEmpty() || targetPath.isEmpty()) {
            throw new IllegalArgumentException("路径参数不能为空");
        }
        long startTime = System.currentTimeMillis();
        InputStream is = new FileInputStream(srcPath);
        OutputStream os = new FileOutputStream(targetPath);
        try (is; os) {
            int len;
            // 一次拷贝5MB
            byte[] bytes = new byte[5 * 1024 * 1024];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }
        System.out.printf("拷贝文件[%s]到[%s]耗时：%d毫秒%n", srcPath, targetPath, (System.currentTimeMillis() - startTime));
        return true;
    }

    /**
     * 方法描述：将要序列化的对象序列化到指定文件中
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
     * @param path {@link String} 序列化文件路径
     * @param clazz {@link Class} 反序列化后的对象类型，例`ArrayList.class`
     * @return {@link T} 反序列化类型
     * @throws IOException 文件操作异常
     * @throws ClassNotFoundException 类型转化异常
     */
    public static <T> T deserialize(String path, Class<T> clazz) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Object o = ois.readObject();
        ois.close();
        try {
            File file = new File(path);
            if (!file.delete()) {
                logger.warn("Unable to delete file: {}", file.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("序列化文件【{}】删除失败", path, e);
        }
        return clazz.cast(o);
    }

    public static boolean unzip(File zipFile, File destDir) {
        long startTime = System.currentTimeMillis();
        logger.info("开始解压文件【{}】到【{}】", zipFile.getAbsolutePath(), destDir.getAbsolutePath());
        if (!zipFile.exists()) {
            logger.info("解压失败，文件【{}】不存在", zipFile.getAbsolutePath());
            return false;
        }
        if (!destDir.exists() && !destDir.mkdirs()) {
            logger.info("目标路径【{}】不存在并且创建失败", destDir.getAbsolutePath());
            return false;
        }
        // 1. 解压本质是将压缩包文件中的数据读取到内存中（输入流）
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            // 2. 获取到压缩包中的每个ZipEntry对象
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File destFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    destFile.getParentFile().mkdirs();
                }
            }
        } catch (Exception e) {
            logger.error("【{}】解压到【{}】失败", zipFile.getAbsolutePath(), destDir.getAbsolutePath(), e);
        }
        logger.info("【{}】解压到【{}】成功，耗时{}毫秒", zipFile.getAbsolutePath(), destDir.getAbsolutePath(), System.currentTimeMillis() - startTime);
        return true;
    }
}
