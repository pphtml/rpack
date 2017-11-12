<template>
    <v-app>
        <v-container fluid>
            <regex-regex></regex-regex>
            <div>
                <v-flex xs12>
                    <div style="position: relative;">
                        <sample-text></sample-text>
                        <sample-masked></sample-masked>
                    </div>
                </v-flex>
                <!--<sample-masked class="highlights"></sample-masked>-->
            </div>
            <sample-data-generator></sample-data-generator>
        </v-container>
    </v-app>
</template>

<script>
    import RegexRegex from './RegexRegex.vue'
    import SampleText from './SampleText.vue'
    import SampleMasked from './SampleMasked.vue'
    import SampleDataGenerator from './SampleDataGenerator.vue'

    export default {
        data () {
            return {
                drawer: false,
                items: [
                    { title: 'Home', icon: 'dashboard' },
                    { title: 'About', icon: 'question_answer' }
                ],
                right: null
            }
        },
        components: {
            'regex-regex': RegexRegex,
            'sample-text': SampleText,
            'sample-masked': SampleMasked,
            'sample-data-generator': SampleDataGenerator
        }
    }
</script>

<style>
    .mono-font {
        font-family: 'Droid Sans Mono', monospace;
    }

    mark {
        border-radius: 2px;
        background-color: #b1d5e5;
    }

/*    .mark-container {
        position: relative;
        width: 100%;
        height: 11em;
    }*/
</style>