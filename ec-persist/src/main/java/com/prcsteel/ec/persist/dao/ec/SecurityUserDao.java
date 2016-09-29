package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.SecurityUser;

/**
 * Created by rolyer on 16-4-19.
 */
public interface SecurityUserDao {
    SecurityUser queryByUserName(String username);
}
