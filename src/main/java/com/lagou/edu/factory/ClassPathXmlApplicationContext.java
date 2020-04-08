package com.lagou.edu.factory;

import com.alibaba.druid.util.StringUtils;
import com.lagou.edu.config.Autowired;
import com.lagou.edu.config.Component;
import com.lagou.edu.config.Repository;
import com.lagou.edu.config.Service;
import com.lagou.edu.utils.ClassUtils;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassPathXmlApplicationContext {

    /**
     * 包路径
     */
    private String packagePath;
    /**
     * 存放注解对象
     */
    private static Map<String,Object> beansMap = new HashMap<String,Object>();

    public ClassPathXmlApplicationContext(String packagePath) {
        this.packagePath = packagePath;
        initBeans();
        initResources();
    }

    private void initResources() {
        //遍历所有的注解对象
        Set<Map.Entry<String, Object>> entrySet = beansMap.entrySet();
        for (Map.Entry<String, Object> stringObjectEntry : entrySet) {
            Object value = stringObjectEntry.getValue();
            try {
                attriAssign(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void initBeans() {
        List<Class<?>> classes = ClassUtils.getClasses(packagePath);
        try{
            findClassExistAnnotation(classes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 判断当前包下哪些使用了注解
     * @param classes
     * @return
     */
    private Map<String,Object> findClassExistAnnotation(List<Class<?>> classes) throws IllegalAccessException, InstantiationException {
        for (Class<?> aClass : classes) {
            Service serviceAnnotation = aClass.getDeclaredAnnotation(Service.class);   //@Service
            Repository repositoryAnnotation = aClass.getDeclaredAnnotation(Repository.class);  //@Repository
            Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);  //@Component
            if(serviceAnnotation!=null ){
                String interfaceName = aClass.getInterfaces()[0].getSimpleName();  //获取实现接口的名称
                String beanId = toLowerCaseFirst(interfaceName);
                if(!StringUtils.isEmpty(serviceAnnotation.value())){  //有value值
                    beanId = serviceAnnotation.value();
                }
                Object bean = aClass.newInstance();
                beansMap.put(beanId,bean);
            }
            if(repositoryAnnotation!=null ){
                String interfaceName = aClass.getInterfaces()[0].getSimpleName();  //获取实现接口的名称
                String beanId = toLowerCaseFirst(interfaceName);
                if(!StringUtils.isEmpty(repositoryAnnotation.value())){  //有value值
                    beanId = repositoryAnnotation.value();
                }
                Object bean = aClass.newInstance();
                beansMap.put(beanId,bean);
            }
            if(componentAnnotation!=null ){
                String className = aClass.getSimpleName();  //获取实现接口的名称
                String beanId = toLowerCaseFirst(className);
                Object bean = aClass.newInstance();
                beansMap.put(beanId,bean);
            }
        }
        return beansMap;
    }


    public Object getBean(String beanId) throws Exception {
        if(StringUtils.isEmpty(beanId)){
            throw new Exception("bean不存在");
        }
        return beansMap.get(beanId);
    }


    private Object newInstance(Class<?> aclass) throws IllegalAccessException, InstantiationException {
        return aclass.newInstance();
    }


    public static String toLowerCaseFirst(String s){
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        }else{
            return (Character.toLowerCase(s.charAt(0))) + s.substring(1);
        }
    }

    /**
     * 依赖注入
     * @param obj
     * @throws IllegalAccessException
     */
    private void attriAssign(Object obj) throws IllegalAccessException {
        Class<? extends Object> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
            if(autowired != null){
                String fieldName = field.getName();
                Object bean = beansMap.get(fieldName);
                if(bean != null){
                    field.setAccessible(true);
                    field.set(obj,bean);
                }

            }

        }
    }
}
