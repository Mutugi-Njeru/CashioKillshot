package com.mycompany.cashiokillshot.database;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class MysqlConnector {

    private static final Logger logger= LogManager.getLogger(MysqlConnector.class);

    public Connection getConnection(){
        Connection connection;
        try {
            InitialContext initialContext=new InitialContext();
            DataSource dataSource= (DataSource) initialContext.lookup("jdbc/cashio");
            connection= dataSource.getConnection();

        } catch (NamingException | SQLException ex) {
            logger.error("ERROR=> {} | Connection is null | {}", ex.getClass().getSimpleName(), ex);
            return null;
        }
        return connection;
    }
}
