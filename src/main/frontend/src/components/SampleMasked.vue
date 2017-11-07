<template>
    <div class="mono-font" style="font-size: 16px;" v-html="markedText"></div>
</template>

<script>
    import Vuex from 'vuex'

    export default {
        computed: Vuex.mapGetters(['markedText'])
    }
</script>

<style scoped>
    div {
        white-space: pre;
    }
</style>
