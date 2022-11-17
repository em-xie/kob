const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor() {
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0 ;// 时间间隔每一帧
        this.has_called_start = false;
    }

    start() { //只执行一次

    }

    update() {//每一帧执行一次 ，除了第一帧之外

    }

    on_destory() {  //删除之前执行

    }

    destory(){
        this.on_destory();

        for(let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if(obj == this) {
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }

}

let last_timestamp; //上一次执行的时间
const step = tiemstamp => {
    // of 值
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_called_start) {
            obj.has_called_start= true;
            obj.start();
        }else {
            obj.timedelta =tiemstamp- last_timestamp;
            obj.update();
        }
    }
    last_timestamp = tiemstamp;
    requestAnimationFrame(step)
}

requestAnimationFrame(step)