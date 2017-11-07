import Vue from 'vue'
import Vuex from 'vuex'
import throttle from 'lodash/throttle'
import axios from 'axios'

Vue.use(Vuex)

export const store = new Vuex.Store({
    state: {
        regex: '',
        sampleText: '',
        markedText: '',
        waitingForServer: false,
        allowedPasteOps: []
    },
    mutations: {
        updateRegex: (state, regex) => state.regex = regex,
        updateSampleText: (state, sampleText) => state.sampleText = sampleText,
        updateMarkedText: (state, markedText) => state.markedText = markedText,
        updateWaitingForServer: (state, waitingForServer) => state.waitingForServer = waitingForServer,
        putAllowedPasteOperation: (state, pastedText) => state.allowedPasteOps = [pastedText]
    },
    actions: {
        updateRegex: (state, regex) => {
            let different = store.state.regex != regex;
            if (different) {
                store.commit('updateRegex', regex);
                store.dispatch('computeMaskedOnServer');
            }
        },
        updateSampleText: (state, expression) => {
            //console.info(`STORE updating: ${expression}, Length: ${expression.length}`);
            let different = store.state.sampleText != expression;
            store.commit('updateSampleText', expression);
            if (different) {
                store.dispatch('computeMaskedOnServer');
            }
        },
        computeMaskedOnServer: throttle((state) => {
            store.commit('updateWaitingForServer', true);
            axios.get('/api/regex', {params: {text: store.state.sampleText, regex: store.state.regex}})
                .then(response => {
                    //console.info(response.data);
                    store.commit('updateWaitingForServer', false);
                    let sampleText = store.state.sampleText;
                    //if (sampleText.includes(' ')) { debugger; };
                    store.commit('updateMarkedText', response.data.result.maskedText);
                }).catch(e => {
                    store.commit('updateWaitingForServer', false);
                    console.info(`Handling of exception not implemented yet: ${e}`);
                });
        }, 100), // ms
        generateSampleData: (state) => {
            // let sampleText = 'First Second third Fourth';
            // store.commit('updateRegex', '[A-Z]\\w+');
            let sampleText = 'A   ';
            store.commit('updateRegex', '[A-Z]\\s+');
            store.commit('putAllowedPasteOperation', sampleText);
            store.dispatch('updateSampleText', sampleText);
        },
        isPasteOperationAllowed: (state, newText) => {
            let result = store.state.allowedPasteOps.includes(newText);
            return result;
        }
    },
    getters: {
        regex: (state) => state.regex,
        sampleText: (state) => state.sampleText,
        markedText: (state) => state.markedText,
        waitingForServer: (state) => state.waitingForServer,
        allowedPasteOps: (state) => state.allowedPasteOps
    }
});
