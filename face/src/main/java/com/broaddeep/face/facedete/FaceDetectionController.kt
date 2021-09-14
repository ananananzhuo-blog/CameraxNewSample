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

   suspend fun detection(path: String,callback:(isSucc:Boolean,result:String)->Unit) {
        if (taskList.isEmpty()) {
            logE("未初始化任务列表")
            return
        }
        currentTask = taskList[index]
        currentTask?.detection(path){isSucc, result ->
            if(isSucc){
                index++
                callback.invoke(isSucc,result)
            }else{
                index++
                callback.invoke(isSucc,result)
            }
        }
    }

}