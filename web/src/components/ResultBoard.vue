<template>
    <div class="result-board">
        <div class="result-board-text" v-if="loser == 'all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="loser === 'A' && a_id === parseInt(id)">
            Lose
        </div>
        <div class="result-board-text" v-else-if="loser === 'B' && b_id === parseInt(id)">
            Lose
        </div>

        <div class="result-board-text" v-else>
            win
        </div>
        <div class="result-board-btn">
        <button @click="restart" type="button" class="btn btn-warning btn-lg">
            再来！
        </button>
        </div>
    </div>
</template>

<script>
// === 比较类型 == 变量类型不一样 -自动变成字符串
import { pkStore } from '@/store/modules/pk'
import { useUserStore } from '@/store/modules/user'
import { storeToRefs } from 'pinia'
export default{
    setup(){
        const store = pkStore();
        const useStore = useUserStore();
        const { loser, a_id,b_id } = storeToRefs(store)
        const { id } = storeToRefs(useStore)
        const restart = () =>{
            store.updateStatus("matching");
            store.updateLoser("none");
            store.updateOpponent({
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
        }
        return {
            restart,
            useStore,
            store,
            a_id,
            b_id,
            id,
            loser
        };
    }
}
</script>

<style scoped>
    div.result-board{
        height: 30vh;
        width: 30vw;
        position: absolute;
        top: 30vh;
        left: 35vw;
        background-color: rgba(50, 50, 50, 0.5);
    }
    div.result-board-text{
        text-align: center;
        color: white;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
    div.result-board-btn{
        padding-top: 7vh;
        text-align: center;
    }
</style>
