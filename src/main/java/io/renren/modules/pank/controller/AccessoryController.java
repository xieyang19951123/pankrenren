package io.renren.modules.pank.controller;

import io.renren.common.utils.R;
import io.renren.modules.pank.entity.Accessory;
import io.renren.modules.pank.service.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("pank/accessory")
public class AccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @RequestMapping(value = "insertAccessory",method = RequestMethod.POST)
    public R insertAccessory(@RequestParam("file") MultipartFile[] multipartFile){
        List<Accessory> accessories = new ArrayList<>();
        for(int i = 0;i<multipartFile.length;i++){
            Accessory accessory = accessoryService.insertAccessory(multipartFile[i]);
            if (accessory != null)accessories.add(accessory);

        }
        return R.ok().put("page",accessories);
    }


    @RequestMapping(value = "deleteAccessory",method = RequestMethod.POST)
    public R  deleteAccessory(@RequestBody Accessory accessory){
        accessoryService.deleteAccessory(accessory);
        return  R.ok();
    }
}
