<template>
    <v-btn light @click="generateSampleData()">Generate Sample Data</v-btn>
</template>

<script>
    import Vuex from 'vuex'

    export default {
        methods: Vuex.mapActions(['generateSampleData'])
    }
</script>
