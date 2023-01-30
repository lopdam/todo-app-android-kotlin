package com.example.todo.models

class Task(id: Long, text: String, status: Int) {

    var id: Long = id
        get() = field                     // getter
        set(value) {
            field = value
        }

    var text: String = text
        get() = field                     // getter
        set(value) {
            field = value
        }

    var status: Int = status
        get() = field                     // getter
        set(value) {
            field = value
        }

    companion object {
        const val ALL = 0
        const val TODO = 1
        const val DONE = 2
    }
}