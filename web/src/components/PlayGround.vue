<template>
    <div class="playground">
        <GameMap/>
    </div>
</template>

<script>

import GameMap from "./GameMap.vue";
export default {
    components: {
        GameMap,
    }
}
</script>

<style scoped>
div.playground {
    /* 浏览器的宽高 */
    width: 60vw;
    height: 70vh;
    /* background: lightblue; */


    /* 居中 */
    margin: 40px auto;
}
</style>