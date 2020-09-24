package com.insigma.common.mybatis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.log4j.Logger;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.google.common.collect.Sets;

/**
 * ˢ��MyBatis Mapper XML �߳�
 *
 * @author ThinkGem
 * @version 2016-5-29
 */
public class MapperRefresh implements Runnable {

    public static Logger log = Logger.getLogger(MapperRefresh.class);

    private static String filename = "/config/app/app.properties";
    private static Properties prop = new Properties();

    private static boolean enabled;            // �Ƿ�����Mapperˢ���̹߳���
    private static boolean refresh;        // ˢ�����ú��Ƿ�������ˢ���߳�

    private Set<String> location;            // Mapperʵ����Դ·��

    private Resource[] mapperLocations;        // Mapper��Դ·��
    private Configuration configuration;        // MyBatis���ö���

    private Long beforeTime = 0L;            // ��һ��ˢ��ʱ��
    private static int delaySeconds;        // �ӳ�ˢ������
    private static int sleepSeconds;        // ����ʱ��
    private static String mappingPath;        // xml�ļ���ƥ���ַ�������Ҫ������Ҫ�޸�

    static {

        try {
            prop.load(MapperRefresh.class.getResourceAsStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Load mybatis-refresh ��" + filename + "�� file error.");
        }

        enabled = "true".equalsIgnoreCase(getPropString("enabled"));

        delaySeconds = getPropInt("delaySeconds");
        sleepSeconds = getPropInt("sleepSeconds");
        mappingPath = getPropString("mappingPath");

        delaySeconds = delaySeconds == 0 ? 50 : delaySeconds;
        sleepSeconds = sleepSeconds == 0 ? 3 : sleepSeconds;
        mappingPath = StringUtils.isBlank(mappingPath) ? "mappings" : mappingPath;

        log.debug("[enabled] " + enabled);
        log.debug("[delaySeconds] " + delaySeconds);
        log.debug("[sleepSeconds] " + sleepSeconds);
        log.debug("[mappingPath] " + mappingPath);
    }

    public static boolean isRefresh() {
        return refresh;
    }

    public MapperRefresh(Resource[] mapperLocations, Configuration configuration) {
        this.mapperLocations = mapperLocations;
        this.configuration = configuration;
    }

    @Override
    public void run() {

        beforeTime = System.currentTimeMillis();

        log.debug("[location] " + location);
        log.debug("[configuration] " + configuration);

        if (enabled) {
            // ����ˢ���߳�
            final MapperRefresh runnable = this;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (location == null) {
                        location = Sets.newHashSet();
                        log.debug("MapperLocation's length:" + mapperLocations.length);
                        for (Resource mapperLocation : mapperLocations) {
                            String s = mapperLocation.toString().replaceAll("\\\\", "/");
                            s = s.substring("file [".length(), s.lastIndexOf(mappingPath) + mappingPath.length());
                            if (!location.contains(s)) {
                                location.add(s);
                                log.debug("Location:" + s);
                            }
                        }
                        log.debug("Locarion's size:" + location.size());
                    }

                    try {
                        Thread.sleep(delaySeconds * 1000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    refresh = true;

                    System.out.println("========= Enabled refresh mybatis mapper =========");

                    while (true) {
                        try {
                            for (String s : location) {
                                runnable.refresh(s, beforeTime);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        try {
                            Thread.sleep(sleepSeconds * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, "MyBatis-Mapper-Refresh").start();
        }
    }

    /**
     * ִ��ˢ��
     *
     * @param filePath   ˢ��Ŀ¼
     * @param beforeTime �ϴ�ˢ��ʱ��
     * @throws NestedIOException     �����쳣
     * @throws FileNotFoundException �ļ�δ�ҵ�
     * @author ThinkGem
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void refresh(String filePath, Long beforeTime) throws Exception {

        // ����ˢ��ʱ��
        Long refrehTime = System.currentTimeMillis();

        // ��ȡ��Ҫˢ�µ�Mapper�ļ��б�
        List<File> fileList = this.getRefreshFile(new File(filePath), beforeTime);
        if (fileList.size() > 0) {
            log.debug("Refresh file: " + fileList.size());
        }
        for (int i = 0; i < fileList.size(); i++) {
            InputStream inputStream = new FileInputStream(fileList.get(i));
            String resource = fileList.get(i).getAbsolutePath();
            try {

                // ����ԭ����Դ������Ϊ�Լ���StrictMap���㣬�������¼���
                String[] mapFieldNames = new String[]{
                        "mappedStatements", "caches",
                        "resultMaps", "parameterMaps",
                        "keyGenerators", "sqlFragments"
                };
                for (String fieldName : mapFieldNames) {
                    Field field = configuration.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Map map = ((Map) field.get(configuration));
                    if (!(map instanceof StrictMap)) {
                        Map newMap = new StrictMap(StringUtils.capitalize(fieldName) + "collection");
                        for (Object key : map.keySet()) {
                            try {
                                newMap.put(key, map.get(key));
                            } catch (IllegalArgumentException ex) {
                                newMap.put(key, ex.getMessage());
                            }
                        }
                        field.set(configuration, newMap);
                    }
                }

                // �����Ѽ��ص���Դ��ʶ�������������¼��ء�
                Field loadedResourcesField = configuration.getClass().getDeclaredField("loadedResources");
                loadedResourcesField.setAccessible(true);
                Set loadedResourcesSet = ((Set) loadedResourcesField.get(configuration));
                loadedResourcesSet.remove(resource);

                //���±��������Դ�ļ���
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration,resource, configuration.getSqlFragments());
                xmlMapperBuilder.parse();
            } catch (Exception e) {
                throw new NestedIOException("Failed to parse mapping resource: '" + resource + "'", e);
            } finally {
                ErrorContext.instance().reset();
            }
            System.out.println("Refresh file: " + mappingPath + StringUtils.substringAfterLast(fileList.get(i).getAbsolutePath(), mappingPath));
            if (log.isDebugEnabled()) {
                log.debug("Refresh file: " + fileList.get(i).getAbsolutePath());
                log.debug("Refresh filename: " + fileList.get(i).getName());
            }
        }
        // ���ˢ�����ļ������޸�ˢ��ʱ�䣬�����޸�
        if (fileList.size() > 0) {
            this.beforeTime = refrehTime;
        }
    }

    /**
     * ��ȡ��Ҫˢ�µ��ļ��б�
     *
     * @param dir        Ŀ¼
     * @param beforeTime �ϴ�ˢ��ʱ��
     * @return ˢ���ļ��б�
     */
    private List<File> getRefreshFile(File dir, Long beforeTime) {
        List<File> fileList = new ArrayList<File>();

        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    fileList.addAll(this.getRefreshFile(file, beforeTime));
                } else if (file.isFile()) {
                    if (this.checkFile(file, beforeTime)) {
                        fileList.add(file);
                    }
                } else {
                    System.out.println("Error file." + file.getName());
                }
            }
        }
        return fileList;
    }

    /**
     * �ж��ļ��Ƿ���Ҫˢ��
     *
     * @param file       �ļ�
     * @param beforeTime �ϴ�ˢ��ʱ��
     * @return ��Ҫˢ�·���true�����򷵻�false
     */
    private boolean checkFile(File file, Long beforeTime) {
        if (file.lastModified() > beforeTime) {
            return true;
        }
        return false;
    }

    /**
     * ��ȡ��������
     *
     * @param key
     * @return
     */
    private static int getPropInt(String key) {
        int i = 0;
        try {
            i = Integer.parseInt(getPropString(key));
        } catch (Exception e) {
        }
        return i;
    }

    /**
     * ��ȡ�ַ�������
     *
     * @param key
     * @return
     */
    private static String getPropString(String key) {
        return prop == null ? null : prop.getProperty(key);
    }

    /**
     * ��д org.apache.ibatis.session.Configuration.StrictMap ��
     * ���� MyBatis3.4.0�汾���޸� put ������������ put���¡�
     */
    public static class StrictMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -4950446264854982944L;
        private String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }

        @Override
        @SuppressWarnings("unchecked")
        public V put(String key, V value) {
            // ThinkGem �������״̬Ϊˢ�£���ˢ��(��ɾ�������)
            if (MapperRefresh.isRefresh()) {
                remove(key);
                MapperRefresh.log.debug("refresh key:" + key.substring(key.lastIndexOf(".") + 1));
            }
            // ThinkGem end
            if (containsKey(key)) {
                throw new IllegalArgumentException(name + " already contains value for " + key);
            }
            if (key.contains(".")) {
                final String shortKey = getShortName(key);
                if (super.get(shortKey) == null) {
                    super.put(shortKey, value);
                } else {
                    super.put(shortKey, (V) new Ambiguity(shortKey));
                }
            }
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(name + " does not contain value for " + key);
            }
            if (value instanceof Ambiguity) {
                throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
                        + " (try using the full name including the namespace, or rename one of the entries)");
            }
            return value;
        }

        private String getShortName(String key) {
            final String[] keyparts = key.split("\\.");
            return keyparts[keyparts.length - 1];
        }

        protected static class Ambiguity {
            private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }
    }
}
