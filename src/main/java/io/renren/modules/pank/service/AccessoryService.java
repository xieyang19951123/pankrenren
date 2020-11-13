package io.renren.modules.pank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.pank.entity.Accessory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccessoryService  extends IService<Accessory> {

    Accessory insertAccessory(MultipartFile file);

    void deleteAccessory(Accessory accessory);
}
