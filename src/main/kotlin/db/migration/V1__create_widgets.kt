package db.migration

import model.Test1s
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.InputStream

class V1__create_test1 : BaseJavaMigration() {


    override fun migrate(context: Context?) {
        val inputStream: InputStream = File("src/main/kotlin/db/migration/test1.txt").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        val array = inputString.split("\n")
        transaction {
            SchemaUtils.create(Test1s)
            for (i in array.indices) {
            Test1s.insert {
                it[name] = array[i]
                }
            }
        }
    }
}