<template>
    <v-flex xs12>
        <v-text-field
                label="Regular Expression"
                class="mono-font"
                spellcheck="false"
                :value="regex" @keyup.stop="updateRegex($event.target.value)"
        ></v-text-field>
    </v-flex>
</template>

<script>
    import Vuex from 'vuex'

    export default {
        computed: Vuex.mapGetters(['regex']),
        methods: Vuex.mapActions(['updateRegex'])
    }
</script>
