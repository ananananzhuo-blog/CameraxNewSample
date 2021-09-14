package com.broaddeep.face.facedete

/**
*   @name mayong
*   @date 2021/9/7 - 10:59
*   @describe 活体识别的任务
*/
class Task(action:Action) {

    /**
     * 活体识别单个任务
     */
    suspend fun detection(path: String,callback:(isSucc:Boolean,result:String)->Unit) {
        // TODO: 2021/9/8 上传逻辑
        callback.invoke(true,"识别成功")
//        callback.invoke(true,"识别失败")
    }
}



enum class Action(type: Int, desc: String) {
    //	1-张嘴  2-眨眼  3-左转头  4-右转头  5-低头 6-抬头
    //7-说1  8-说A  9-说B  10-说C  11-说2  12-说3
    openmouth(1,"张嘴"),
    blinkeye(2,"眨眼"),
    Headleft(3,"左转头"),
    headright(4,"右转头"),
    headdown(5,"低头"),
    headup(6,"抬头"),
    say1(7,"说1"),
    saya(8,"说A"),
    sayb(9,"说B"),
    sayc(10,"说C"),
    say2(11,"说2"),
    say3(12,"说3"),
}
