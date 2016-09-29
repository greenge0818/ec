package com.prcsteel.ec.service.impl;

import com.github.orderbyhelper.OrderByHelper;
import com.github.pagehelper.PageHelper;
import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.domain.ec.generic.EntityInfo;
import com.prcsteel.ec.model.domain.ec.generic.EntityInfoCol;
import com.prcsteel.ec.model.dto.Page;
import com.prcsteel.ec.model.query.ComplexCond;
import com.prcsteel.ec.model.query.ComplexConditionNode;
import com.prcsteel.ec.persist.dao.ec.GenericMybatisDao;
import com.prcsteel.ec.service.GenericDaoService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Rolyer on 2016/4/27.
 */
@Service("genericDaoService")
public class GenericDaoServiceImpl implements GenericDaoService {
    private final Logger logger = LoggerFactory.getLogger(GenericDaoServiceImpl.class);

    @Resource
    private GenericMybatisDao genericDao;

    private Map<String, EntityInfo> cache = new HashMap<>();

    private EntityInfo getInfoNoValue(Class clasz) {

        String className = clasz.getName();
        //使用类名从缓存读取
        if (cache.get(className) != null) {
            try {
                return cache.get(className).clone();
            } catch (CloneNotSupportedException e) {
                logger.error("clone failed!", e);
            }
        }
        //如果没有则检查注解
        checkDBEntity(clasz);

        EntityInfo info = new EntityInfo();
        info.setClassName(className);

        //获取表信息
        Entity dbEntity = (Entity) clasz.getAnnotation(Entity.class);
        if (StringUtils.isNotBlank(dbEntity.database())) {
            info.setDatabase(dbEntity.database());
        }
        info.setTableName(dbEntity.value());
        Field[] fields = getDeclaredFields(clasz);
        for (Field eachField : fields) {
            if (eachField == null)
                continue;

            EntityInfoCol curCol = new EntityInfoCol();
            //读取注解
            Column dbColumn = eachField.getAnnotation(Column.class);
            if (dbColumn == null) continue;
            String entityColName = eachField.getName();//class 字段名
            String dbColName = dbColumn.value(); //db字段名
            String insertIfNull = dbColumn.insertIfNull().trim();
            String updateIfNull = dbColumn.updateIfNull().trim();
            //表字段默认与class字段相同
            if (StringUtils.isBlank(dbColName)) {
                dbColName = entityColName;
            }

            curCol.setEntityColName(entityColName);
            curCol.setDbColName(dbColName);
            if (StringUtils.isNotBlank(insertIfNull)) {
                curCol.setInsertIfNull(insertIfNull);
            }
            if (StringUtils.isNotBlank(updateIfNull)) {
                curCol.setUpdateIfNull(updateIfNull);
            }

            KeyColumn keyColumn = eachField.getAnnotation(KeyColumn.class);
            if (keyColumn != null) {
                curCol.setIsKeyColumn(true);
                curCol.setUseGeneratedKeys(keyColumn.useGeneratedKeys());
            } else {
                curCol.setIsKeyColumn(false);
                curCol.setUseGeneratedKeys(false);
            }

            info.getCols().add(curCol);
        }
        cache.put(className, info);
        return info;
    }

    private Field[] getDeclaredFields(Class clazz) {
        List<Field[]> tFields = new ArrayList<>();
        int len = 0;

        Field[] cFields = clazz.getDeclaredFields();
        tFields.add(cFields);
        len += cFields.length;

        Class<?> current = clazz;
        while (current.getSuperclass() != null) {
            current = current.getSuperclass();

            Field[] fFields = current.getDeclaredFields();
            tFields.add(fFields);

            len += cFields.length;
        }

        Field[] res = new Field[len];
        int idx = 0;
        for (Field[] fields : tFields) {
            for (Field field : fields) {
                res[idx] = field;
                idx++;
            }
        }

        return res;
    }

    private EntityInfo getInfoWithValue(Object entity) {
        EntityInfo config = getInfoNoValue(entity.getClass());
        for (EntityInfoCol colConfg : config.getCols()) {
            try {
                colConfg.setValue(PropertyUtils.getProperty(entity, colConfg.getEntityColName()));
            } catch (Exception e) {
                throw new IllegalArgumentException("read property from entity error");
            }
        }
        return config;
    }

    private void checkDBEntity(Class clazz) {
        Entity dbEntity = (Entity) clazz.getAnnotation(Entity.class);
        if (dbEntity == null)
            throw new IllegalArgumentException("not dbEntity ,plase check your beans with [DBEntity] annotation");
    }

    @Override
    public int save(Object entity) {
        EntityInfo info = getInfoWithValue(entity);

        if (info.getKeyCol().getValue() == null) {
            return insert(entity);
        } else {
            return updateByKey(entity);
        }
    }

    @Override
    public int insert(Object entity) {
        EntityInfo info = getInfoWithValue(entity);
        int rows = genericDao.insert(info);
        //使用生成的自增长主键
        Object generatedKey = info.getGeneratedKey();
        EntityInfoCol col = info.getKeyCol();
        try {
            //原主键为空才填入  否则不填
            if (col.getValue() == null) {
                BeanUtils.setProperty(entity, col.getEntityColName(), generatedKey);
            }
        } catch (Exception e) {
            throw new RuntimeException("set generatedKey error", e.fillInStackTrace());
        }
        return rows;
    }

    @Override
    public <T> int batchInsert(List<T> entitys) {
        List<EntityInfo> infos = new LinkedList<>();
        for (Object each : entitys) {
            infos.add(getInfoWithValue(each));
        }
        int rows = genericDao.batchInsert(infos);
        return rows;
    }


    @Override
    public int updateByKey(Object entity) {
        EntityInfo info = getInfoWithValue(entity);
        if (!info.hasKeyCol() || info.getKeyCol().getValue() == null) {
            throw new IllegalArgumentException("update cannot done when key property is null");
        }
        int rows = genericDao.updateByKey(info);
        return rows;
    }


    @Override
    public int deleteByKey(Object entity) {
        EntityInfo info = getInfoWithValue(entity);
        if (!info.hasKeyCol()) {
            throw new IllegalArgumentException("update cannot done when key property is null");
        }
        int rows = genericDao.deleteByKey(info);
        return rows;
    }

    private <T> T getFirst(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> T findOne(T entity) {
        return getFirst(findAll(entity));
    }

    @Override
    public <T> List<T> findAll(T entity) {
        return findAll(entity, null);
    }

//  @Override
//  public <T> T queryDBEntity(T entity,String... orderBys){
//      return getFirst(findAll(entity, orderBys));
//  }

    @Override
    public <T> List<T> findAll(T entity, String... orderBys) {
        return findAll(entity, 1, 0, orderBys);
    }

    @Override
    public <T> List<T> findAllTop(T entity, int top, String... orderBys) {
        return findAll(entity, 1, top, orderBys);
    }

    @Override
    public <T> Page<T> findAll(T entity, int pageNum, int pageSize) {
        return findAll(entity, pageNum, pageSize, null);
    }

    @Override
    public <T> Page<T> findAll(T entity, int pageNum, int pageSize, String... orderBys) {
        EntityInfo info = getInfoWithValue(entity);
        String orderStr = this.orderParse(entity.getClass(), orderBys);
        if (StringUtils.isNotBlank(orderStr)) {
            OrderByHelper.orderBy(orderStr);

        }
//        分页
        PageHelper.startPage(pageNum, pageSize, true, false, true);
        Page<Map<String, Object>> list = genericDao.query(info);
        Page<T> result = (Page<T>) list.clone();
        result.clear();
        for (Map<String, Object> eachBeanMap : list) {
            T eachObject = null;
            try {
                eachObject = (T) (entity.getClass().newInstance());
            } catch (Exception e1) {
                logger.error("instantiate new bean error.", e1.fillInStackTrace());
            }

            try {
                BeanUtils.copyProperties(eachObject, eachBeanMap);
            } catch (Exception e) {
                logger.error("copyProperties from Map to bean error.", e.fillInStackTrace());
            }
            result.add(eachObject);
        }
        return result;
    }

    @Override
    public <T> T findOneComplex(Class<T> clazz, ComplexCond condition) {
        return getFirst(findAllComplex(clazz, condition));
    }

    @Override
    public <T> List<T> findAllComplex(Class<T> clazz, ComplexCond condition) {
        return findAllComplex(clazz, condition, 1, 0);
    }

    @Override
    public <T> Page<T> findAllComplexTop(Class<T> clazz, ComplexCond condition, int top, String orderBys) {
        return findAllComplex(clazz, condition, 1, top, orderBys);
    }

    @Override
    public <T> Page<T> findAllComplex(Class<T> clazz, ComplexCond condition, int pageNum, int pageSize) {
        return findAllComplex(clazz, condition, pageNum, pageSize, null);
    }

    @Override
    public <T> Page<T> findAllComplex(Class<T> clazz, ComplexCond condition, int pageNum, int pageSize, String... orderBys) {
        EntityInfo info = getInfoNoValue(clazz);
        String orderStr = this.orderParse(clazz, orderBys);
        if (StringUtils.isNotBlank(orderStr)) {
            OrderByHelper.orderBy(orderStr);
        }

        //分页
        PageHelper.startPage(pageNum, pageSize, true, false, true);
        //将condition 中 col 替换为数据库对应字段
        for (ComplexConditionNode eachNode : condition.getNodes()) {
            if ("col".equals(eachNode.getLink())) {
                EntityInfoCol entityInfoCol = info.getColByEntityColName((String) eachNode.getObjects()[0]);
                eachNode.setObjects(entityInfoCol.getDbColName());
            }
        }

        Page<Map<String, Object>> list = genericDao.queryByComplex(info, condition);
        Page<T> result = (Page<T>) list.clone();
        result.clear();
        for (Map<String, Object> eachBeanMap : list) {
            T eachObject = null;
            try {
                eachObject = (T) (clazz.newInstance());
            } catch (Exception e1) {
                logger.error("instantiate new bean error.", e1.fillInStackTrace());
            }

            try {
                BeanUtils.copyProperties(eachObject, eachBeanMap);
            } catch (Exception e) {
                logger.error("copyProperties from Map to bean error.", e.fillInStackTrace());
            }
            result.add(eachObject);
        }
        return result;
    }

    /**
     * order解析 将bean property 解析为数据库 column
     *
     * @param clazz
     * @param orderBys
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    private String orderParse(Class clazz, String... orderBys) {

        if (orderBys == null) {
            return null;
        }
        EntityInfo info = getInfoNoValue(clazz);
        //排序解析处理--将bean property 解析为数据库 column
        StringBuffer orderStr = new StringBuffer();
        for (int i = 0; i < orderBys.length; i++) {
            String eachOrderBy = orderBys[i];
            if (StringUtils.isBlank(eachOrderBy)) {
                continue;
            }
            String[] orderArray = eachOrderBy.split("\\s+");
            if (orderArray.length < 1) {
                throw new IllegalArgumentException("order parse error.");
            }
            String order = orderArray[0];
            String sort;
            if (orderArray.length == 1) {
                sort = "ASC";
            } else {
                sort = orderArray[1];
                if (!StringUtils.equalsIgnoreCase(sort, "asc") && !StringUtils.equalsIgnoreCase(sort, "desc")) {
                    throw new IllegalArgumentException("order sort parse error.");
                }
            }
            EntityInfoCol infoCol = info.getColByEntityColName(order);
            orderStr.append(infoCol.getDbColName()).append("\t").append(sort);
            if (i < orderBys.length - 1) {
                orderStr.append(",");
            }
        }
        return orderStr.toString();
    }
}
