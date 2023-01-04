import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from './Snake';
import { pkStore } from '@/store/modules/pk'
import { recordStore } from '@/store/modules/record'

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        super();
        const pkstore = pkStore();
        const rStore = recordStore();
        //this.gamemap =gamemap;
        this.pkstore = pkstore;
        this.rStore = rStore;
        this.ctx = ctx;
        this.parent = parent;

        

        this.L = 0;

        this.rows = 13;
        this.cols = 14;



        this.inner_walls_count = 20;
        this.walls = [];

        this.Snakes  = [
            new Snake({id : 0, color : "#4876ec", r : this.rows - 2, c : 1}, this),
            new Snake({id : 1, color : "#f94848", r : 1, c : this.cols - 2}, this),
        ];
    }

    // flood fill算法
    // Flood fill算法是从一个区域中提取若干个连通的点与其他相邻区域区分开
    // （或分别染成不同颜色）的经典算法。
    // 因为其思路类似洪水从一个区域扩散到所有能到达的区域而得名。
    // Flood Fill算法被用来计算需要被清除的区域。
    // 参数 ，图 ，起点的x,y 终点的x, y
    // check_connectivity(g, sx, sy, tx, ty) {
    //     if (sx == tx && sy == ty) return true;
    //     g[sx][sy] = true;

    //     let dx = [-1, 0, 1, 0],
    //         dy = [0, 1, 0, -1];
    //     for (let i = 0; i < 4; i++) {
    //         let x = sx + dx[i],
    //             y = sy + dy[i];
    //         if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
    //             return true;
    //     }

    //     return false;
    // }

    // creat_walls() {
    //     // // 墙 true 无 false
    //     console.log(this.store.state.pk.gamemap)
    //     const g = this.store.state.pk.gamemap;
    //     // for (let r = 0; r < this.cols; r++) {
    //     //     g[r] = [];
    //     //     for (let c = 0; c < this.cols; c++) {
    //     //         g[r][c] = false;
    //     //     }
    //     // }

    //     // //给四周加上墙
    //     // for (let r = 0; r < this.rows; r++) {
    //     //     g[r][0] = g[r][this.cols - 1] = true;
    //     // }

    //     // for (let c = 0; c < this.cols; c++) {
    //     //     g[0][c] = g[this.rows - 1][c] = true;
    //     // }

    //     // // 创建随机障碍物
    //     // for (let i = 0; i < this.inner_walls_count / 2; i++) {
    //     //     for (let j = 0; j < 1000; j++) {
    //     //         // 随机一个数
    //     //         let r = parseInt(Math.random() * this.rows);
    //     //         let c = parseInt(Math.random() * this.cols);
    //     //         if (g[r][c] || g[this.rows-1-r][this.cols-1-c]) continue;

    //     //         // 排除左下角和右上角
    //     //         if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
    //     //             continue;
    //     //         // 对称
    //     //         g[r][c] = g[this.rows-1-r][this.cols-1-c] = true;
    //     //         break;
    //     //     }
    //     // }

    //     // // 判断是否连通
    //     // // 复制当前状态
    //     // // JSON.parse()方法将JSON格式字符串转换为js对象(属性名没有双引号)
    //     // // 解析前要保证数据是标准的JSON格式，否则会解析出错
    //     // // JSON.stringify()先将对象转换为字符串
    //     // const copy_g = JSON.parse(JSON.stringify(g)); // 复制到JSON再转换回来
    //     // if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;

    //     for (let r = 0; r < this.rows; r++) {
    //         for (let c = 0; c < this.cols; c++) {
    //             if (g[r][c]) {
    //                 this.walls.push(new Wall(r, c, this));
    //             }
    //         }
    //     }

    //     // return true;
    // }
    create_walls() {
        
        const g = this.pkstore.gamemap;
        //console.log(g);
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    

      add_listening_events() {
        // this.ctx.canvas.focus();
        //     this.ctx.canvas.addEventListener("keydown", e => {
                
        //         let d = -1;
        //         if(e.key === 'w') d = 0;
        //         else if(e.key === 'd') d = 1;
        //         else if(e.key === 's') d = 2;
        //         else if(e.key === 'a') d = 3;
        //         // else if(e.key === 'ArrowUp') snake1.set_direction(0);
        //         // else if(e.key === 'ArrowRight') snake1.set_direction(1);
        //         // else if(e.key === 'ArrowDown') snake1.set_direction(2);
        //         // else if(e.key === 'ArrowLeft') snake1.set_direction(3);
        //         if(d >= 0) {
        //             this.pkstore.socket.send(JSON.stringify({
        //                 event: "move",
        //                 direction: d,
        //             }));
        //         }
        //     });

        if (this.rStore.is_record!=="false") {
            let k = 0;
            const a_steps = this.rStore.a_steps;
            const b_steps = this.rStore.b_steps;
            console.log(a_steps)
            const loser = this.rStore.record_loser;
            const [snake0, snake1] = this.Snakes;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                } else {
                    
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k ++ ;
            }, 300);
        } else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                
                let d = -1;
                if(e.key === 'w') d = 0;
                else if(e.key === 'd') d = 1;
                else if(e.key === 's') d = 2;
                else if(e.key === 'a') d = 3;
                // else if(e.key === 'ArrowUp') snake1.set_direction(0);
                // else if(e.key === 'ArrowRight') snake1.set_direction(1);
                // else if(e.key === 'ArrowDown') snake1.set_direction(2);
                // else if(e.key === 'ArrowLeft') snake1.set_direction(3);
                if(d >= 0) {
                    this.pkstore.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            });
        }
    }


    start() {
        // for (let i = 0; i < 1000; i++)
        //     if (this.creat_walls())
        //         break;
        // this.creat_walls();
        this.create_walls();
        this.add_listening_events();
    }

    update_size() {
        // 计算小正方形的边长
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready(){ //判断2条蛇是否都准备号下一回合
        for(const snake of this.Snakes) {
            if(snake.status !=="idle") return false;
            if(snake.direction === -1) return false;
        }
        return true;
    }

    next_step() {  // 让两条蛇进入下一回合
        for (const snake of this.Snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.Snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k -- ;
            }
            for (let i = 0; i < k; i ++ ) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }




    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }

}