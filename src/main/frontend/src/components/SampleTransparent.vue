<template>
    <div class="mono-font" v-html="sampleText"></div>
</template>

<script>
    import Vuex from 'vuex'

    export default {
        computed: Vuex.mapGetters(['sampleText'])
    }
</script>

<style scoped>
    div {
        white-space: pre;
        z-index: -1;
    }
</style>
