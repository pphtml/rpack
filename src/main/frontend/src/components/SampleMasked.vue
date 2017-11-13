<template>
    <div class="mono-font" v-html="markedText"></div>
</template>

<script>
    import Vuex from 'vuex'

    export default {
        computed: Vuex.mapGetters(['markedText'])
    }
</script>

<style scoped>
    div {
        /*min-height: 5em;*/
        white-space: pre;
/*        empty-cells: show;
        overflow: auto;
        resize: both;*/
        z-index: 1;
        position: absolute;
        top: 0px;
        width: 100%;
    }
</style>
