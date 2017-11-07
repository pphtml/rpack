<template>
    <!--<v-text-field-->
            <!--label="Sample Text"-->
            <!--class="mono-font"-->
            <!--multi-line-->
            <!--autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false"-->
            <!--:value="sampleText" @keyup.stop="updateSampleText($event.target.value)"-->
    <!--&gt;</v-text-field>-->

    <div contenteditable="true" class="mono-font sample-text" autocomplete="off" autocorrect="off" autocapitalize="off"
         spellcheck="false" @keyup.stop="update"
    ></div>
</template>

<script>
    import Vuex from 'vuex'
    import he from 'he'

    function getPlainText(html) {
        return html.split(/<\/?mark>/).join('');
    }

//    function computePlainTextLength(html) {
//        return getPlainText(html).length;
//    }
//
    function getCaretCharacterOffsetWithin(element) {
        let caretOffset = 0;
        // debugger;
        let selection = window.getSelection();
        if (selection.rangeCount > 0) {
            let range = selection.getRangeAt(0);
            let preCaretRange = range.cloneRange();
            preCaretRange.selectNodeContents(element);
            preCaretRange.setEnd(range.endContainer, range.endOffset);
            caretOffset = preCaretRange.toString().length;
        }
        return caretOffset;
    }

    function createRange(node, chars, range) {
        if (!range) {
            range = document.createRange()
            range.selectNode(node);
            range.setStart(node, 0);
        }

        if (chars.count === 0) {
            range.setEnd(node, chars.count);
        } else if (node && chars.count >0) {
            if (node.nodeType === Node.TEXT_NODE) {
                if (node.textContent.length < chars.count) {
                    chars.count -= node.textContent.length;
                } else {
                    range.setEnd(node, chars.count);
                    chars.count = 0;
                }
            } else {
                for (var lp = 0; lp < node.childNodes.length; lp++) {
                    range = createRange(node.childNodes[lp], chars, range);

                    if (chars.count === 0) {
                        break;
                    }
                }
            }
        }

        return range;
    }

    function textFromTarget(target) {
        //let plainText = getPlainText(target.innerHTML).replace(/\<br\>$/, ' ');
        //let plainText = target.textContent.replace(/\<br\>$/, ' ');
        let plainText = target.innerText;
        //let plainText = target.textContent;
        let spacesFixed =  plainText.replace(/\u00a0/g, ' ');
//        console.info(plainText);
//        let unescaped = he.decode(plainText);
//        return unescaped;
        return spacesFixed;
        //var jq = jQuery(target);
        //debugger;
    }

    function plainToHTML(plainText) {
        let encoded = plainText; // he.encode(plainText);
        return encoded.replace(/ /g, '&nbsp;');
    }

    let encode = s => s.replace(/&/g, '&amp;').replace(/ /g, '&nbsp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

    function encodeForDiv(source) {
        let marksSolved = source.replace(/((<\/?mark>)?([\s\S]*?))((?=<\/?mark>)|$)/g, (a, b, c, d)=>`${c||''}${d?encode(d):''}`);
        let lineBreaksSolved = marksSolved.replace(/\n/g, '<br/>');
        return lineBreaksSolved;
    }

    export default {
        mounted: function(){
            this.$el.innerHTML = plainToHTML(this.mytext);
            this.$store.subscribe((mutation, state) => {
                if (mutation.type === 'updateMarkedText') {
                    //console.info('MUTATION!!! ');
                    //console.info(this.mytext);
                    this.mytext = mutation.payload;
                    let spacesUnified = mutation.payload.replace(/\u00a0/g, ' ');
                    let encodedWithMarks = encodeForDiv(spacesUnified);
                    console.info(`RES: ${encodedWithMarks}`);
                    let previousPosition = getCaretCharacterOffsetWithin(this.$el);
                    //console.info(previousPosition);
                    let textLengthBefore = textFromTarget(this.$el).length;
                    let plainText = getPlainText(this.mytext);
                    let textLengthAfter = plainText.length;
                    let allowedPaste = this.$store.getters.allowedPasteOps.includes(plainText);
                    //if (this.$store.getters.allowedPasteOps.includes(this.mytext)) {
                    //console.info(`INCLUDES ${}`);
                    //}
//                    if (this.mytext.includes(' ')) {
//                        debugger;
//                    }

                    if (allowedPaste || textLengthAfter == textLengthBefore) {
                    //if (!this.$store.getters.waitingForServer) {
                        let newText = encodedWithMarks; // plainToHTML(this.mytext);
                        console.info(`@Setting: ${newText}`);
                        this.$el.innerHTML = newText;
                        // this.$el.innerText.length;

                        if (textLengthAfter > 0) {
                            let selection = window.getSelection();
                            let range = createRange(this.$el.parentNode, { count: previousPosition });
                            if (range) {
                                range.collapse(false);
                                selection.removeAllRanges();
                                selection.addRange(range);
                            }
                        }
                    } else {
                        console.info(`ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ ${this.mytext}`);
                        console.info(`Plain text "${plainText}" not allowed`);
                    }
                }
            })
        },
        methods: {
            //...Vuex.mapActions(['updateSampleText']),
            update: function(event){
                //let plainText = getPlainText(event.target.textContent);
                // console.info(event.target.innerHTML);
                let unescaped = textFromTarget(event.target);
                console.info(`@Update ${event.target.innerHTML} -> ${unescaped}#, lenght: ${unescaped.length}`);
                //console.info(`${plainText} -> ${unescaped}`);
//                if (plainText.includes('  ')) {
//                    debugger;
//                }
//                if (event.target.innerText.length != event.target.innerText.trim().length) {
//                    debugger;
//                }
                // this.$store.dispatch('updateSampleText', event.target.innerText); 
                this.$store.dispatch('updateSampleText', unescaped);
            }
        },
        computed:  {
                mytext: {
                    get: function () {
                        //return 'Def345';
                        //console.info('GET');
                        return this.$store.getters.markedText;
                    },
                    set: function (newValue) {
                        console.info(`Setting newValue ${newValue}#`);
                    }
                }
            }
    }
</script>

<style scoped>
    div {
        min-height: 5em;
        empty-cells: show;
        background-color: #eee;
        overflow: auto;
        resize: both;
    }
</style>
