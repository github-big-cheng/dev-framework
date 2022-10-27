package com.wisely.sys.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.sys.entity.SysFile;
import com.wisely.sys.mapper.SysFileMapper;
import com.wisely.sys.service.SysFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysFileServiceImpl implements SysFileService {


    @Resource
    SysFileMapper sysFileMapper;


    @Override
    public List<SysFile> selectListBySelective(SysFile query) {
        query.setIsDeleted(0);
        return sysFileMapper.selectListBySelective(query);
    }


    @Transactional
    @Override
    public int save(Model input) {

        Integer orgId = input.getInt("orgId"); // 机构ID
        String orgName = input.getString("orgName"); // 机构名称
        String sourceType = input.getString("sourceType"); // 业务类型
        String sourceId = input.getString("sourceId"); // 源id
        String uploaderId = input.getString("uploaderId");
        String uploader = input.getString("uploader");
        String uploadTime = input.getString("uploadTime", DateHelper.formatNow()); // 上传时间

        // 删除原文件信息
        if (!input.getBoolean("isAppend")) {
            SysFile record = new SysFile();
            record.setSourceType(sourceType);
            record.setSourceIdQueryIn(DataHelper.getString(sourceId));
            if (input.isNotBlank("subType")) {
                // 子类型支持
                record.setSubType(input.getString("subType"));
            }
            // 非追加模式，删除原附件信息
            this.delete(record);
        }

        // 保存新的文件信息
        String now = DateHelper.formatNow();

        List<Model> sysFileList = input.getModelList("files");
        sysFileList.forEach(item -> {
            SysFile itemRecord = new SysFile();

            // 文件属性
            itemRecord.setOrgId(orgId);
            itemRecord.setOrgName(orgName);
            itemRecord.setSourceType(sourceType);
            // 文件子类型
            if (input.isNotBlank("subType")) {
                itemRecord.setSubType(input.getString("subType"));
            }
            itemRecord.setSourceId(sourceId);
            itemRecord.setUploaderId(uploaderId);
            itemRecord.setUploader(uploader);
            itemRecord.setUploadTime(uploadTime); // 上传时间

            // 文件信息
            itemRecord.setFilename(item.getString("fileName"));
            itemRecord.setRealPath(item.getString("filePath")); // 文件路径
            itemRecord.setAttachSize(item.getLong("fileSize"));
            itemRecord.setExtension(item.getString("fileType")); // 文件类型

            itemRecord.setIsDeleted(0);
            itemRecord.setCreateTime(now);
            sysFileMapper.insertSelective(itemRecord);
        });

        return 0;
    }


    @Override
    public int delete(SysFile sysFile) {
        return sysFileMapper.deleteBySelective(sysFile);
    }
}
