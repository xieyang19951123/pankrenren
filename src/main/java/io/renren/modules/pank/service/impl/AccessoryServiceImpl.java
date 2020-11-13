package io.renren.modules.pank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import io.renren.modules.pank.dao.AccessoryDao;
import io.renren.modules.pank.entity.Accessory;
import io.renren.modules.pank.service.AccessoryService;
import io.renren.modules.pank.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class AccessoryServiceImpl  extends ServiceImpl<AccessoryDao, Accessory> implements AccessoryService {

    @Autowired
    private  AccessoryDao accessoryDao;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Override
    public Accessory insertAccessory(MultipartFile file) {
        Accessory accessory = null;
        try {
            StorePath storePath = fastDFSClient.uploadFile3(file);
            String path = "http://106.75.162.179:8888/"+storePath.getGroup()+"/"+storePath.getPath();//原图文件
            String thumbImagePath = "http://106.75.162.179:8888/"+storePath.getGroup()+"/"+thumbImageConfig.getThumbImagePath(storePath.getPath());//缩略图
            accessory =new Accessory();
            accessory.setName(file.getOriginalFilename());
            accessory.setShowStatus(1);
            accessory.setUrl(path);
            accessory.setUrlMin(thumbImagePath);
            accessoryDao.insert(accessory);
        }catch (Exception e){
            e.printStackTrace();
        }
        return accessory;
    }

    @Override
    public void deleteAccessory(Accessory accessory) {
        try {
            fastDFSClient.deleteFile(accessory.getUrl());
            fastDFSClient.deleteFile(accessory.getUrlMin());
        }catch (Exception e){}finally {

            accessoryDao.deleteBatchIds(Arrays.asList(accessory.getId()));
        }

    }
}
