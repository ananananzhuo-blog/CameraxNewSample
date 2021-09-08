package com.broaddeep.face.facedete

import com.broaddeep.face.utils.logE

class FaceDetectionController {
    val taskList = mutableListOf<Task>()
    var currentTask: Task? = null
    var index = 0
    fun addTaskList(tasks: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(tasks)
    }

    fun detection(path: String,callback:(result:String)->Unit) {
        if (taskList.isEmpty()) {
            logE("未初始化任务列表")
            return
        }
        currentTask = taskList[index]
        currentTask?.detection(path)
    }

}