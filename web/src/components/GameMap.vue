<template>
    <div ref="parent" class="gamemap">
<canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap"
import { ref, onMounted } from 'vue'
 //import { useStore } from "vuex";
 import { pkStore } from '@/store/modules/pk'
// import { recordStore } from '@/store/modules/record'
// onMounted 注册一个回调函数，在组件挂载完成后执行。
export default {
    setup() {

         const pkstore = pkStore();
        // const rStore = recordStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            pkstore.updateGameObject(new GameMap(canvas.value.getContext('2d'), parent.value))

         });
        return {
            parent,
            canvas
        }
    }
}

</script>


<style scoped>

div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;

    /* 居中 */
    justify-content: center;
    align-items: center;
}
</style>

