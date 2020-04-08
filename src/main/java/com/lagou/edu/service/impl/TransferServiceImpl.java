package com.lagou.edu.service.impl;

import com.lagou.edu.config.Autowired;
import com.lagou.edu.config.Service;
import com.lagou.edu.config.Transactional;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;

/**
 * @author 应癫
 */
@Service
public class TransferServiceImpl implements TransferService {



    // 最佳状态
    @Autowired
    private AccountDao accountDao;


    @Override
    @Transactional
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            accountDao.updateAccountByCardNo(from);

    }
}
