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
        min-height: 5em;
        white-space: pre;
        color: transparent;
        z-index: -1;
    }
</style>
