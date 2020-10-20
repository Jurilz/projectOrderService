package com.orderService.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DB {

//    var db: DataSource = connect()

    fun connect(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        config.username = "postgres"
        config.password = "postgres"
        config.setDriverClassName("org.postgresql.Driver")

        return HikariDataSource(config)
    }
}