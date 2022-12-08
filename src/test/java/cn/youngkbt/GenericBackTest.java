package cn.youngkbt;

import cn.kbt.dbdtobean.core.DbdToBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022/11/23 21:28
 * @note
 */
@SpringBootTest
public class GenericBackTest {
    
    // @Autowired
    // DbdToBean dbdToBean;
    //
    // @Test
    // public void dbdToBeanTest() throws SQLException, IOException {
    //     dbdToBean.setCommentType("/**");
    //     Map<String, String> generic = dbdToBean.createBeanFromDataBase("generic");
    //     dbdToBean.exportToFiles(generic);
    //     dbdToBean.closeConnection();
    // }
}
