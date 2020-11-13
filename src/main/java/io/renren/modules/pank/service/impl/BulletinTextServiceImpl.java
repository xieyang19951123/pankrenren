package io.renren.modules.pank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.pank.dao.BulletinTextDao;
import io.renren.modules.pank.entity.BulletinText;
import io.renren.modules.pank.service.BulletinTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BulletinTextServiceImpl extends ServiceImpl<BulletinTextDao, BulletinText> implements BulletinTextService {
}
