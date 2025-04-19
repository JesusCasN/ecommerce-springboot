package com.diplomado.tienda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
class TiendaOnlineApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Creacion de la base de datos lista");
    }

}
