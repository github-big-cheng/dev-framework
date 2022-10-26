package com.wisely.ucenter.caches;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterUser;
import com.wisely.ucenter.mapper.UcenterPersonMapper;
import com.wisely.ucenter.service.UcenterPersonInfoService;
import com.wisely.ucenter.service.UcenterUserService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 人员缓存
 */
@Slf4j
public class PersonCache extends BaseEntityCache<Model> implements UcenterConstants {

    @Resource
    UcenterPersonMapper ucenterPersonMapper;
    @Resource
    UcenterUserService ucenterUserService;
    @Resource
    UcenterPersonInfoService ucenterPersonInfoService;

    @Override
    public String getName() {
        return "PersonCache";
    }

    @Override
    protected List<CacheLoader<Model>> cacheLoaders() {
        return Lists.newArrayList(new PersonByIdCacheLoader(), new PersonByAccountCacheLoader());
    }

    @Override
    protected List<Model> loadData() {
        UcenterPerson query = new UcenterPerson();
        query.setIsDeleted(0);
        List<UcenterPerson> dataList = ucenterPersonMapper.selectListBySelective(query);

        List<Model> personList = Lists.newArrayList();
        if (ValidHelper.isEmpty(dataList)) {
            return personList;
        }

        //人员id
        List<Integer> personIdList = dataList.stream().map(UcenterPerson::getId).collect(Collectors.toList());
        String personIds = StringHelper.join(personIdList, ",");

        // personInfo信息
        Model<Integer, Model> personInfoModel = ucenterPersonInfoService.personInfoQuery(personIds);

        // 账号信息
        Model<Integer, Model> userModel = Model.builder();
        UcenterUser userQuery = new UcenterUser();
        userQuery.setIsDeleted(0);
        userQuery.setPersonIdQueryIn(personIds);
        List<UcenterUser> userList = ucenterUserService.list(userQuery);
        if (ValidHelper.isNotEmpty(userList)) {
            userList.forEach(user -> {
                Model item = Model.builder();
                item.set("userId", user.getId())
                        .set("account", user.getAccount())
                        .set("accountStatus", user.getStatus());
                userModel.set(user.getPersonId(), item);
            });
        }

        dataList.forEach(person -> {
            Model temp = Model.parseObject(person);
            personList.add(temp);

            // 账号信息
            temp.putAll(userModel.get(person.getId()));

            // personInfo 信息
            if (personInfoModel.containsKey(person.getId())) {
                temp.set("info", personInfoModel.get(person.getId()));
            }
        });
        return personList;
    }


    class PersonByIdCacheLoader extends CacheLoader<Model> {

        @Override
        public String key() {
            return PERSON_CACHE_KEY;
        }

        /**
         * UcenterPersonVo.id -> UcenterPersonVo
         *
         * @return
         */
        @Override
        public BiConsumer<Model<byte[], byte[]>, Model> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("id")) {
                    return;
                }
                UcenterPersonVo personVo =
                        (UcenterPersonVo) item.convertTo(UcenterPersonVo.class);
                cacheModel.set(item.getString("id").getBytes(), ProtoBufHelper.serializer(personVo));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, Model> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("id")) {
                    return;
                }
                list.add(item.getString("id").getBytes());
            };
        }
    }


    class PersonByAccountCacheLoader extends CacheLoader<Model> {

        @Override
        public String key() {
            return ACCOUNT_PERSON_CACHE_KEY;
        }

        /**
         * personVo.account -> personVo
         *
         * @return
         */
        @Override
        public BiConsumer<Model<byte[], byte[]>, Model> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("account")) {
                    return;
                }

                UcenterPersonVo personVo =
                        (UcenterPersonVo) item.convertTo(UcenterPersonVo.class);
                cacheModel.set(personVo.getAccount().getBytes(), ProtoBufHelper.serializer(personVo));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, Model> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("account")) {
                    return;
                }
                list.add(item.getString("account").getBytes());
            };
        }
    }

}
