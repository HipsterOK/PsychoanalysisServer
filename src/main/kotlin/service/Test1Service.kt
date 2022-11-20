package service

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import service.DatabaseFactory.dbExec

class Test1Service {

    private val listeners = mutableMapOf<Int, suspend (Test1Notification) -> Unit>()

    fun addChangeListener(id: Int, listener: suspend (Test1Notification) -> Unit) {
        listeners[id] = listener
    }

    fun removeChangeListener(id: Int) = listeners.remove(id)

    private suspend fun onChange(type: ChangeType, id: Int, entity: Test1? = null) {
        listeners.values.forEach {
            it.invoke(Notification(type, id, entity))
        }
    }

    suspend fun getAllTest1(): List<Test1> = dbExec {
        Test1s.selectAll().map { toTest1(it) }
    }

    suspend fun getTest1(id: Int): Test1? = dbExec {
        Test1s.select {
            (Test1s.id eq id)
        }.map { toTest1(it) }
            .singleOrNull()
    }

    suspend fun updateTest1(test1: NewTest1): Test1? {
        val id = test1.id
        return if (id == null) {
            addTest1(test1)
        } else {
            dbExec {
                Test1s.update({ Test1s.id eq id }) {
                    it[name] = test1.name
                }
            }
            getTest1(id).also {
                onChange(ChangeType.UPDATE, id, it)
            }
        }
    }

    suspend fun addTest1(test1: NewTest1): Test1 {
        var key = 0
        dbExec {
            key = (Test1s.insert {
                it[name] = test1.name
            } get Test1s.id)
        }
        return getTest1(key)!!.also {
            onChange(ChangeType.CREATE, key, it)
        }
    }

    suspend fun deleteTest1(id: Int): Boolean {
        return dbExec {
            Test1s.deleteWhere { Test1s.id eq id } > 0
        }.also {
            if (it) onChange(ChangeType.DELETE, id)
        }
    }

    private fun toTest1(row: ResultRow): Test1 =
        Test1(
            id = row[Test1s.id],
            name = row[Test1s.name],
        )
}