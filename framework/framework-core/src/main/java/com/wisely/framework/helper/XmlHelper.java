package com.wisely.framework.helper;

import com.wisely.framework.exception.SystemException;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XmlHelper {

    private XmlHelper() {

    }

    /**
     * 获取document
     *
     * @param inputStream
     * @param withDtd     是否加载DTD文件
     * @return
     */
    public static Document readFile(InputStream inputStream, boolean withDtd) {
        try {
            SAXReader reader = new SAXReader();
            if (!withDtd) {
                reader.setEntityResolver(new NullEntityResolver());
            }
            return reader.read(inputStream);
        } catch (Exception e) {
            throw SystemException.builder(e, "XmlHelper.readFile_error");
        }
    }

    /**
     * 获取document
     *
     * @param inputStream
     * @return
     */
    public static Document readFile(InputStream inputStream) {
        return readFile(inputStream, true);
    }

    /**
     * 获取document
     *
     * @param resourcePath
     * @return
     */
    public static Document readFile(String resourcePath) {
        return readFile(resourcePath, true);
    }

    /**
     * 获取document
     *
     * @param resourcePath
     * @param withDtd      是否读取DTD文件
     * @return
     */
    public static Document readFile(String resourcePath, boolean withDtd) {
        return readFile(ResourceHelper.getInputStream(resourcePath), withDtd);
    }


    static class NullEntityResolver implements EntityResolver {
        static String emptyDtd = "";
        static ByteArrayInputStream byteIs = new ByteArrayInputStream(emptyDtd.getBytes());

        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(byteIs);
        }
    }

}
